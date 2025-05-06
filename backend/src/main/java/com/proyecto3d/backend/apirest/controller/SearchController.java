package com.proyecto3d.backend.apirest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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