package com.proyecto3d.backend.apirest.model.service;

import java.util.List;

import com.proyecto3d.backend.apirest.model.entity.Favorito;
import com.proyecto3d.backend.apirest.model.entity.Anuncio;

public interface FavoritoService {

	public List<Favorito> findAll();
	
	public Favorito findById(Long id);
	
	public List<Anuncio> findAnunciosByFavoritos(Long id);
	
	public Favorito save(Favorito detalles);
	
	public void deleteAnuncioByFavorito(Long id);
	
	public void delete(Favorito detalles);

	public List<Favorito> findByUsuarioId(Long usuarioId);
}
