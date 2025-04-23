package com.proyecto3d.backend.apirest.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto3d.backend.apirest.model.entity.Usuario;

public interface UsuarioDao extends JpaRepository<Usuario, Long> {

	Usuario findByEmail(String email);

}
