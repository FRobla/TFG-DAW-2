package com.evidenlibrary.backend.apirest.model.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evidenlibrary.backend.apirest.model.dao.CategoriaDao;
import com.evidenlibrary.backend.apirest.model.dao.AnuncioDao;
import com.evidenlibrary.backend.apirest.model.entity.Categoria;
import com.evidenlibrary.backend.apirest.model.entity.Anuncio;

@Service
public class CategoriaServiceImpl implements CategoriaService {
	
	@Autowired
	private CategoriaDao categoriaDao;
	@Autowired
	private AnuncioDao anuncioDao;

	@Override
	@Transactional(readOnly = true)
	public List<Categoria> findAll() {
		return (List<Categoria>) categoriaDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Categoria findById(Long id) {
		return categoriaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Categoria> findCategoriasByAnuncioId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Categoria> findCategoriasByAutorId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public Categoria save(Categoria categoria) {
		return categoriaDao.save(categoria);
	}

	
	@Override
	@Transactional
	public void delete(Categoria categoria) {
		//Eliminar realciones en la tabla intermedia(anuncio_categoria)
		for (Anuncio anuncio : categoria.getAnuncios()) {
            anuncio.getCategorias().remove(categoria); // Eliminar el género de los anuncios
            anuncioDao.save(anuncio); // Guardar el anuncio actualizado
        }
		// Eliminar el género
		categoriaDao.delete(categoria);
	}
	
    @Override
    @Transactional
    public void deleteAll() {
        // Obtener todos los géneros
        List<Categoria> categorias = findAll();
        
        // Para cada género, desasociar todos sus anuncios
        for (Categoria categoria : categorias) {
            // Obtener una nueva colección para evitar ConcurrentModificationException
            Set<Anuncio> anunciosDelCategoria = new HashSet<>(categoria.getAnuncios());
            
            // Eliminar la asociación entre el género y cada anuncio
            for (Anuncio anuncio : anunciosDelCategoria) {
                anuncio.getCategorias().remove(categoria);
                anuncioDao.save(anuncio); // Guardar el anuncio con la relación actualizada
            }
        }
        
        // Una vez que todas las asociaciones han sido eliminadas, eliminar todos los géneros
        categoriaDao.deleteAll();
    }

}
