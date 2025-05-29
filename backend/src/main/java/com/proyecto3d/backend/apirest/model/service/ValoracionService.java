package com.proyecto3d.backend.apirest.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.proyecto3d.backend.apirest.model.entity.Valoracion;

public interface ValoracionService {
	
	public List<Valoracion> findAll();
	
	public Valoracion findById(Long id);
	
	public List<Valoracion> findByAnuncioId(Long anuncioId);
	
	public Page<Valoracion> findByAnuncioId(Long anuncioId, Pageable pageable);
	
	public List<Valoracion> findByUsuarioId(Long usuarioId);
	
	public Valoracion save(Valoracion valoracion);
	
	public void delete(Valoracion valoracion);
	
    public void deleteAll();
    
    public Double getValoracionMediaByAnuncioId(Long anuncioId);
    
    public Long getNumeroValoracionesByAnuncioId(Long anuncioId);
    
    public List<Map<String, Object>> getDistribucionValoraciones();
    
    public List<Map<String, Object>> getValoracionesConConteoAnuncios();
    
    public boolean existeValoracionUsuarioAnuncio(Long anuncioId, Long usuarioId);
} 