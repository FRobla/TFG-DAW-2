package com.proyecto3d.backend.apirest.model.service;

import java.util.List;
import java.util.Map;

import com.proyecto3d.backend.apirest.model.entity.Material;

public interface MaterialService {
	
	public List<Material> findAll();
	
	public Material findById(Long id);
	
	public List<Material> findByActivoTrue();
	
	public Material save(Material material);
	
	public void delete(Material material);
	
    public void deleteAll();
    
    public List<Map<String, Object>> getMaterialesConConteoAnuncios();
    
    public List<Material> findByTerm(String term);
} 