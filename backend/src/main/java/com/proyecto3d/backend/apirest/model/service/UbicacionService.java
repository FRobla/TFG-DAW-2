package com.proyecto3d.backend.apirest.model.service;

import java.util.List;
import java.util.Map;

import com.proyecto3d.backend.apirest.model.entity.Ubicacion;

public interface UbicacionService {
	
	public List<Ubicacion> findAll();
	
	public Ubicacion findById(Long id);
	
	public List<Ubicacion> findByActivoTrue();
	
	public Ubicacion save(Ubicacion ubicacion);
	
	public void delete(Ubicacion ubicacion);
	
    public void deleteAll();
    
    public List<Map<String, Object>> getUbicacionesConConteoAnuncios();
    
    public List<Ubicacion> findByTerm(String term);
} 