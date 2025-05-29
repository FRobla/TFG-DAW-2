package com.proyecto3d.backend.apirest.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.proyecto3d.backend.apirest.model.entity.Favorito;

public interface FavoritoService {
	
	public List<Favorito> findAll();
	
	public Favorito findById(Long id);
	
	public List<Favorito> findByUsuarioId(Long usuarioId);
	
	public Page<Favorito> findByUsuarioId(Long usuarioId, Pageable pageable);
	
	public Favorito save(Favorito favorito);
	
	public void delete(Favorito favorito);
	
    public void deleteAll();
    
    public boolean existeFavoritoUsuarioAnuncio(Long anuncioId, Long usuarioId);
    
    public Favorito toggleFavorito(Long anuncioId, Long usuarioId);
    
    public Long getNumeroFavoritosByAnuncioId(Long anuncioId);
    
    public Long getNumeroFavoritosByUsuarioId(Long usuarioId);
    
    public List<Map<String, Object>> getAnunciosMasFavoritos();
} 