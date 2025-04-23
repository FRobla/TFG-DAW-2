package com.proyecto3d.backend.apirest.model.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto3d.backend.apirest.model.dao.AnuncioDao;
import com.proyecto3d.backend.apirest.model.dao.CategoriaDao;
import com.proyecto3d.backend.apirest.model.dao.ImpresoraDao;
import com.proyecto3d.backend.apirest.model.entity.Anuncio;
import com.proyecto3d.backend.apirest.model.entity.Categoria;
import com.proyecto3d.backend.apirest.model.entity.Impresora;

@Service
public class SearchService{
	@Autowired
    private AnuncioDao anuncioDao;
    @Autowired
    private ImpresoraDao autorDao;
    @Autowired
    private CategoriaDao categoriaDao;
    
    //Metodo dividir las consultas
    private List<String> parseQuery(String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }
        
        return Arrays.asList(query.toLowerCase().split("\\s+"))
                .stream()
                .filter(term -> !term.isEmpty())
                .collect(Collectors.toList());
    }
    
    public List<Anuncio> searchAnuncios(String query) {
    	// Dividir la consulta 
        List<String> terms = parseQuery(query);

        if (terms.isEmpty()) {
        	return List.of(); // Lista vacía si no hay términos
        }
       
        Set<Anuncio> allResults = new HashSet<>();
        
        for (String term : terms) { 
        	
        	//Busqueda por titulo
        	allResults.addAll(anuncioDao.findByTituloContainingIgnoreCase(term));
        	
            /*
        	//Busqueda por año
        	allResults.addAll(anuncioDao.findByAnio(term)); */
        	
            //allResults.addAll(anuncioDao.findByTerm(term));
        }
        
        return new ArrayList<>(allResults);
    }
    
    public List<Impresora> searchImpresoras(String query) {
    	
        List<String> terms = parseQuery(query);
        
        if (terms.isEmpty()) {
            return List.of();
        }
        
        Set<Impresora> allResults = new HashSet<>();
        
        // Buscar autores por cada término
        for (String term : terms) {
        	allResults.addAll(autorDao.findByNombreContainingIgnoreCase(term));
            //allResults.addAll(autorDao.findByTerm(term));
        }
        
        return new ArrayList<>(allResults);
    }
    
    public List<Categoria> searchCategorias(String query) {
    	
        List<String> terms = parseQuery(query);
        
        if (terms.isEmpty()) {
            return List.of(); 
        }
        
        Set<Categoria> allResults = new HashSet<>();
        
        // Buscar géneros por cada término
        for (String term : terms) {
        	allResults.addAll(categoriaDao.findByNombreContainingIgnoreCase(term));           
        	//allResults.addAll(categoriaDao.findByTerm(term));
        }
        
        return new ArrayList<>(allResults);
    }
    
    
    //Todos los anuncios asociados  a un autor
    @Transactional(readOnly = true)
    public List<Anuncio> findAnunciosByImpresoraId(Long autorId) {
        // autor existe
        Impresora autor = autorDao.findById(autorId)
            .orElseThrow(() -> new RuntimeException("Impresora no encontrada con ID: " + autorId));
        // anuncios asociados a este autor (ManyToOne)
        return anuncioDao.findByImpresoraId(autor.getId(), Pageable.unpaged()).getContent();
    }
    
    //Todos los anuncios asociados a un género 
    @Transactional(readOnly = true)
    public List<Anuncio> findAnunciosByCategoriaId(Long categoriaId) {
        // género exista
        Categoria categoria = categoriaDao.findById(categoriaId)
            .orElseThrow(() -> new RuntimeException("Género no encontrado con ID: " + categoriaId));
        
        // anuncios asociados a este género
        return anuncioDao.findByCategoriasContaining(categoria);
    }
    /* 
    //Busqueda por año de publicacion
    @Transactional(readOnly = true)
    public List<Anuncio> findAnunciosByAnio(String anio) {
        return anuncioDao.findByAnio(anio);
    } */
}