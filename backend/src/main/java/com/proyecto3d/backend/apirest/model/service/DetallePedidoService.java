package com.proyecto3d.backend.apirest.model.service;

import java.util.List;

import com.proyecto3d.backend.apirest.model.entity.DetallePedido;

public interface DetallePedidoService {

	public List<DetallePedido> findAll();
	
	public DetallePedido findById(Long id);
	
	public List<DetallePedido> findByAnuncioId(Long anuncioId);
	
	public List<DetallePedido> findByPedidoId(Long pedidoId);
	
	public DetallePedido save(DetallePedido detallePedido);
	
	public void delete(DetallePedido detallePedido);
	
	public void deleteAll();
}
