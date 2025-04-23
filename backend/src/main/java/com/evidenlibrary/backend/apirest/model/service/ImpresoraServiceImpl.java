package com.evidenlibrary.backend.apirest.model.service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.evidenlibrary.backend.apirest.model.dao.ImpresoraDao;
import com.evidenlibrary.backend.apirest.model.dao.AnuncioDao;
import com.evidenlibrary.backend.apirest.model.entity.Impresora;
import com.evidenlibrary.backend.apirest.model.entity.Anuncio;

@Service
public class ImpresoraServiceImpl implements ImpresoraService {
	@Autowired
	private ImpresoraDao impresoraDao;
	@Autowired
	private AnuncioDao anuncioDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Impresora> findAll() {
		return (List<Impresora>) impresoraDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Impresora findById(Long id) {
		return impresoraDao.findById(id).orElse(null);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Impresora findByAnuncioId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	@Transactional
	public Impresora save(Impresora impresora) {
		return impresoraDao.save(impresora);
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Override
	@Transactional
	public void delete(Impresora impresora) {
	    // Eliminar la impresora de los anuncios asociados
	    for (Anuncio anuncio : impresora.getAnuncios()) {
	        anuncio.getCategorias().remove(impresora); // Eliminar la impresora de la lista de impresoras del anuncio
	        anuncioDao.save(anuncio);  // Guardar el anuncio con la relación actualizada
	    }
	    // Luego, eliminar la impresora de la base de datos
	    impresoraDao.delete(impresora);
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Override
    @Transactional
    public void deleteAll() {
        // Obtener todos los impresoras
        List<Impresora> impresoras = findAll();
        
        // Para cada impresora, desasociar todos sus anuncios
        for (Impresora impresora : impresoras) {
            // Obtener una nueva colección para evitar excepciones de concurrencia
            Set<Anuncio> anunciosDeCategoria = new HashSet<>(impresora.getAnuncios());
            
            // Eliminar la asociación entre la impresora y cada anuncio
            for (Anuncio anuncio : anunciosDeCategoria) {
                anuncio.getCategorias().remove(impresora);
                anuncioDao.save(anuncio); // Guardar el anuncio con la relación actualizada
            }
        }
        
        // Una vez que todas las asociaciones han sido eliminadas, eliminar todos los impresoras
        impresoraDao.deleteAll();
    }
}