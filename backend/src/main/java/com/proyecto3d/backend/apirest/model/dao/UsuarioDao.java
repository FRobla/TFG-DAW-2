package com.proyecto3d.backend.apirest.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.proyecto3d.backend.apirest.model.entity.Usuario;

public interface UsuarioDao extends JpaRepository<Usuario, Long> {

	@Query("SELECT u FROM Usuario u WHERE u.email = :email")
	Usuario findByEmail(String email);

}
