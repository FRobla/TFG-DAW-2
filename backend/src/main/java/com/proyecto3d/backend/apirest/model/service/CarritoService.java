package com.proyecto3d.backend.apirest.model.service;

import java.util.List;

import com.proyecto3d.backend.apirest.model.entity.Carrito;

public interface CarritoService {

	public List<Carrito> findAll();
	
	public Carrito findById(Long id);
	
	public Carrito findCarritoByUsuarioId(Long id);
	
	public Carrito save(Carrito carrito);
	
	public void delete(Carrito carrito);
	
	public List<Carrito> findByUsuarioId(Long usuarioId);
}
