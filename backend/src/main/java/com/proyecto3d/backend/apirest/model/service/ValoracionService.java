package com.proyecto3d.backend.apirest.model.service;

import java.util.List;

import com.proyecto3d.backend.apirest.model.entity.Valoracion;

public interface ValoracionService {

	public List<Valoracion> findAll();
	
	public Valoracion findById(Long id);
	
	public List<Valoracion> findByAnuncioId(Long anuncioId);
	
	public List<Valoracion> findByUsuarioId(Long usuarioId);
	
	public Valoracion save(Valoracion valoracion);
	
	public void delete(Valoracion valoracion);
	
}
