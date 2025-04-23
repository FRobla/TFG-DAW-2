package com.proyecto3d.backend.apirest.model.service;

import java.util.List;

import com.proyecto3d.backend.apirest.model.entity.Usuario;

public interface UsuarioService {

	public List<Usuario> findAll();
	
	public Usuario findById(Long id);
	
	public List<Usuario> findUsuarioByPedidoId(Long id);
	
	public Usuario save(Usuario usuario);
	
	public void delete(Usuario usuario);

	public Usuario findByEmail(String email);
	
}
