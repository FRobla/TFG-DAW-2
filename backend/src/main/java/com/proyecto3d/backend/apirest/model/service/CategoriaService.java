package com.proyecto3d.backend.apirest.model.service;

import java.util.List;

import com.proyecto3d.backend.apirest.model.entity.Categoria;

public interface CategoriaService {
	
	public List<Categoria> findAll();
	
	public Categoria findById(Long id);
	
	public List<Categoria> findCategoriasByAnuncioId(Long id);
	
	public Categoria save(Categoria categoria);
	
	public void delete(Categoria categoria);
	
    public void deleteAll();
}
