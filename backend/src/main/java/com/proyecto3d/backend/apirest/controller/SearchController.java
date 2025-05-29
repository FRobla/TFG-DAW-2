package com.proyecto3d.backend.apirest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto3d.backend.apirest.model.entity.Anuncio;
import com.proyecto3d.backend.apirest.model.service.SearchService;


@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class SearchController {
	
	@Autowired
    private SearchService searchService;

	
	@GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam String query,
            @RequestParam(required = false, defaultValue = "true") boolean searchAnuncios,
            @RequestParam(required = false, defaultValue = "true") boolean searchImpresoras,
            @RequestParam(required = false, defaultValue = "true") boolean searchCategorias) {

		Map<String,Object> results = new HashMap<>();
        
        if (searchAnuncios) {
            results.put("anuncios", searchService.searchAnuncios(query));
        }
        
        return ResponseEntity.ok(results);
	}

	@GetMapping("/search/anuncios/avanzada")
	public ResponseEntity<?> searchAvanzada(
			@RequestParam(required = false) String q,
			@RequestParam(required = false) String categoria,
			@RequestParam(required = false) String ubicacion,
			@RequestParam(required = false) String valoracion,
			@RequestParam(required = false) Double precioMin,
			@RequestParam(required = false) Double precioMax,
			@RequestParam(required = false) String material,
			@RequestParam(required = false) String tiempoEntrega,
			@RequestParam(required = false, defaultValue = "0") int page,
			@RequestParam(required = false, defaultValue = "9") int size,
			@RequestParam(required = false, defaultValue = "relevancia") String orden) {

		try {
			// Crear el objeto Pageable con ordenación
			Sort sort = Sort.unsorted();
			switch (orden) {
				case "precio_asc":
					sort = Sort.by("precio_base").ascending();
					break;
				case "precio_desc":
					sort = Sort.by("precio_base").descending();
					break;
				case "fecha_asc":
					sort = Sort.by("fecha_publicacion").ascending();
					break;
				case "fecha_desc":
					sort = Sort.by("fecha_publicacion").descending();
					break;
				case "titulo":
					sort = Sort.by("titulo").ascending();
					break;
				default:
					// relevancia - ordenar por fecha descendente por defecto
					sort = Sort.by("fecha_publicacion").descending();
					break;
			}

			Pageable pageable = PageRequest.of(page, size, sort);

			// Realizar la búsqueda avanzada
			Page<Anuncio> resultados = searchService.searchAnunciosAvanzada(
				q, categoria, ubicacion, valoracion, precioMin, precioMax, 
				material, tiempoEntrega, pageable);

			Map<String, Object> response = new HashMap<>();
			response.put("content", resultados.getContent());
			response.put("totalElements", resultados.getTotalElements());
			response.put("totalPages", resultados.getTotalPages());
			response.put("currentPage", resultados.getNumber());
			response.put("size", resultados.getSize());
			response.put("numberOfElements", resultados.getNumberOfElements());
			response.put("first", resultados.isFirst());
			response.put("last", resultados.isLast());

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			// Log del error para debugging
			System.err.println("Error en búsqueda avanzada: " + e.getMessage());
			e.printStackTrace();
			
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("error", "Error en la búsqueda: " + e.getMessage());
			errorResponse.put("detalle", e.getClass().getSimpleName());
			return ResponseEntity.status(500).body(errorResponse);
		}
	}
	
	@GetMapping("/search/anuncios/categoria/{categoriaId}")
	public ResponseEntity<List<Anuncio>> getAnunciosByCategoria(@PathVariable Long categoriaId) {
		List<Anuncio> anuncios = searchService.findAnunciosByCategoriaId(categoriaId);
		return ResponseEntity.ok(anuncios);
	}
	
	/*
	@GetMapping("/search/anuncios/anio/{anio}")
	public ResponseEntity<List<Anuncio>> getAnunciosByAnio(@PathVariable String anio) {
		List<Anuncio> anuncios = searchService.findAnunciosByAnio(anio);
		return ResponseEntity.ok(anuncios);
	}  */
}