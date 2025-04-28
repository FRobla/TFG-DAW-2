package com.proyecto3d.backend.apirest.model.service;

import java.util.List;

import com.proyecto3d.backend.apirest.model.entity.DetalleCarrito;
import com.proyecto3d.backend.apirest.model.entity.Anuncio;

public interface DetalleCarritoService {

	public List<DetalleCarrito> findAll();
	
	public DetalleCarrito findById(Long id);
	
	public List<Anuncio> findAnunciosByCarritoId(Long id);
	
	public DetalleCarrito save(DetalleCarrito detalleCarrito);

	
	public void delete(DetalleCarrito detalleCarrito);

	public List<DetalleCarrito> findByCarritoId(Long carritoId);
}
