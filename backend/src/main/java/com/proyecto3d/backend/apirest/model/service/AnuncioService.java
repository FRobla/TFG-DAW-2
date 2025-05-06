package com.proyecto3d.backend.apirest.model.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.proyecto3d.backend.apirest.model.entity.Anuncio;

public interface AnuncioService {

	public List<Anuncio> findAll();

	// Filtros
	public Page<Anuncio> findAllPaginado(Pageable pageable); // Paginación

	// public Page<Anuncio> findByImpresoraIdPaginado(Long autorId, Pageable pageable); // Paginacion con autor

	public Page<Anuncio> findByCategoriaIdPaginado(Long categoriaId, Pageable pageable); // Paginacion con categoria

	// public List<Anuncio> getMejorValorados(); // Mejor valorados

	// public Anuncio obtenerAnuncioConValoracionMedia(Long id); // Calcula media

	public Anuncio findById(Long id);

	// public List<Anuncio> findAnunciosByImpresoraId(Long id);

	// public List<Anuncio> findAnunciosByFavoritoId(Long id);

	// public List<Anuncio> findAnunciosByCarritoId(Long id);

	public Anuncio save(Anuncio anuncio);

	public void delete(Anuncio anuncio);

	public void deleteAll();

	public Anuncio findByTitulo(String titulo);

}
