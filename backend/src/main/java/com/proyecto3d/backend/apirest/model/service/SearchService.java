package com.proyecto3d.backend.apirest.model.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto3d.backend.apirest.model.dao.AnuncioDao;
import com.proyecto3d.backend.apirest.model.dao.CategoriaDao;
import com.proyecto3d.backend.apirest.model.entity.Anuncio;
import com.proyecto3d.backend.apirest.model.entity.Categoria;

@Service
public class SearchService{

	@Autowired
    private AnuncioDao anuncioDao;

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

    /**
     * Búsqueda avanzada de anuncios con múltiples filtros
     */
    @Transactional(readOnly = true)
    public Page<Anuncio> searchAnunciosAvanzada(
            String query,
            String categoria,
            String ubicacion,
            String valoracion,
            Double precioMin,
            Double precioMax,
            String material,
            String tiempoEntrega,
            Pageable pageable) {

        // Procesar parámetros de categorías
        List<Long> categoriaIds = null;
        if (categoria != null && !categoria.trim().isEmpty()) {
            categoriaIds = Arrays.stream(categoria.split(","))
                    .filter(s -> !s.trim().isEmpty())
                    .map(s -> Long.parseLong(s.trim()))
                    .collect(Collectors.toList());
            
            // Convertir lista vacía en null
            if (categoriaIds.isEmpty()) {
                categoriaIds = null;
            }
        }

        // Procesar parámetros de ubicación
        List<Long> ubicacionIds = null;
        if (ubicacion != null && !ubicacion.trim().isEmpty()) {
            ubicacionIds = Arrays.stream(ubicacion.split(","))
                    .filter(s -> !s.trim().isEmpty())
                    .map(s -> Long.parseLong(s.trim()))
                    .collect(Collectors.toList());
            
            if (ubicacionIds.isEmpty()) {
                ubicacionIds = null;
            }
        }

        // Procesar parámetros de material
        List<Long> materialIds = null;
        if (material != null && !material.trim().isEmpty()) {
            materialIds = Arrays.stream(material.split(","))
                    .filter(s -> !s.trim().isEmpty())
                    .map(s -> Long.parseLong(s.trim()))
                    .collect(Collectors.toList());
            
            if (materialIds.isEmpty()) {
                materialIds = null;
            }
        }

        // Procesar valoración mínima
        Double valoracionMin = null;
        if (valoracion != null && !valoracion.trim().isEmpty()) {
            try {
                valoracionMin = Double.parseDouble(valoracion.trim());
            } catch (NumberFormatException e) {
                // Si no se puede parsear, ignorar el filtro
                valoracionMin = null;
            }
        }

        // Procesar tiempo de entrega - mapear IDs a valores reales
        List<String> tiempoEntregaValores = null;
        if (tiempoEntrega != null && !tiempoEntrega.trim().isEmpty()) {
            List<String> tiempoIds = Arrays.asList(tiempoEntrega.split(","));
            tiempoEntregaValores = new ArrayList<>();
            
            for (String tiempoId : tiempoIds) {
                switch (tiempoId.trim()) {
                    case "1":
                        tiempoEntregaValores.add("24 horas");
                        break;
                    case "2":
                        tiempoEntregaValores.add("2-3 días");
                        break;
                    case "3":
                        tiempoEntregaValores.add("3-4 días");
                        tiempoEntregaValores.add("4-5 días");
                        tiempoEntregaValores.add("4-6 días");
                        tiempoEntregaValores.add("5-7 días");
                        break;
                    case "4":
                        tiempoEntregaValores.add("7-10 días");
                        break;
                }
            }
            
            // Convertir lista vacía en null
            if (tiempoEntregaValores.isEmpty()) {
                tiempoEntregaValores = null;
            }
        }

        return anuncioDao.searchAnunciosAvanzada(
                query,
                categoriaIds,
                ubicacionIds,
                materialIds,
                valoracionMin,
                precioMin,
                precioMax,
                tiempoEntregaValores,
                pageable);
    }
    
    /*
    public List<Impresora> searchImpresoras(String query) {
    	
        List<String> terms = parseQuery(query);
        
        if (terms.isEmpty()) {
            return List.of();
        }
        
        Set<Impresora> allResults = new HashSet<>();
        
        // Buscar impresoraes por cada término
        for (String term : terms) {
        	allResults.addAll(impresoraDao.findByModeloContainingIgnoreCase(term));
            //allResults.addAll(impresoraDao.findByTerm(term));
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
    
    
    //Todos los anuncios asociados  a un impresora
    @Transactional(readOnly = true)
    public List<Anuncio> findAnunciosByImpresoraId(Long impresoraId) {
        // impresora existe
        Impresora impresora = impresoraDao.findById(impresoraId)
            .orElseThrow(() -> new RuntimeException("Impresora no encontrada con ID: " + impresoraId));
        // anuncios asociados a este impresora (ManyToOne)
        return anuncioDao.findByImpresoraId(impresora.getId(), Pageable.unpaged()).getContent();
    } */
    
    //Todos los anuncios asociados a una categoria
    @Transactional(readOnly = true)
    public List<Anuncio> findAnunciosByCategoriaId(Long categoriaId) {
        // categoria exista
        Categoria categoria = categoriaDao.findById(categoriaId)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + categoriaId));
        
        // anuncios asociados a esta categoria
        return anuncioDao.findByCategoriasContaining(categoria);
    }
    /* 
    //Busqueda por año de publicacion
    @Transactional(readOnly = true)
    public List<Anuncio> findAnunciosByAnio(String anio) {
        return anuncioDao.findByAnio(anio);
    } */
}