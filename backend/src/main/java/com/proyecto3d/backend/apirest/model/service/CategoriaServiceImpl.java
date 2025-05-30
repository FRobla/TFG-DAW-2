package com.proyecto3d.backend.apirest.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto3d.backend.apirest.model.dao.CategoriaDao;
import com.proyecto3d.backend.apirest.model.dao.AnuncioDao;
import com.proyecto3d.backend.apirest.model.entity.Categoria;
import com.proyecto3d.backend.apirest.model.entity.Anuncio;

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

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Categoria> findCategoriasByAnuncioId(Long id) {
		// Obtener el anuncio
		Anuncio anuncio = anuncioDao.findById(id).orElse(null);
		// Obtener las categorias del anuncio
		return (List<Categoria>) anuncio.getCategorias();
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
            anuncio.getCategorias().remove(categoria); // Eliminar la categoria de los anuncios
            anuncioDao.save(anuncio); // Guardar el anuncio actualizado
        }
		// Eliminar la categoria
		categoriaDao.delete(categoria);
	}
	
    @Override
    @Transactional
    public void deleteAll() {
        // Obtener todos los categorias
        List<Categoria> categorias = findAll();
        
        // Para cada categoria, desasociar todos sus anuncios
        for (Categoria categoria : categorias) {
            // Obtener una nueva colección para evitar ConcurrentModificationException
            Set<Anuncio> anunciosDelCategoria = new HashSet<>(categoria.getAnuncios());
            
            // Eliminar la asociación entre el categoria y cada anuncio
            for (Anuncio anuncio : anunciosDelCategoria) {
                anuncio.getCategorias().remove(categoria);
                anuncioDao.save(anuncio); // Guardar el anuncio con la relación actualizada
            }
        }
        
        // Una vez que todas las asociaciones han sido eliminadas, eliminar todos los categorias
        categoriaDao.deleteAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCategoriasConConteoAnuncios() {
        // Usar la consulta optimizada del DAO que hace un JOIN y GROUP BY
        List<Object[]> resultados = categoriaDao.findCategoriasConConteoAnuncios();
        List<Map<String, Object>> categoriasConConteo = new ArrayList<>();
        
        for (Object[] resultado : resultados) {
            Long categoriaId = (Long) resultado[0];
            String categoriaNombre = (String) resultado[1];
            Long cantidad = (Long) resultado[2];
            
            // Obtener la categoría completa para incluir la descripción
            Categoria categoria = findById(categoriaId);
            
            Map<String, Object> categoriaInfo = new HashMap<>();
            categoriaInfo.put("id", categoriaId);
            categoriaInfo.put("nombre", categoriaNombre);
            categoriaInfo.put("descripcion", categoria != null ? categoria.getDescripcion() : "");
            categoriaInfo.put("cantidad", cantidad);
            
            categoriasConConteo.add(categoriaInfo);
        }
        
        return categoriasConConteo;
    }

}
