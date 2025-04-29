package com.proyecto3d.backend.apirest.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto3d.backend.apirest.model.entity.Valoracion;

public interface ValoracionDao extends JpaRepository<Valoracion, Long> {
    List<Valoracion> findByAnuncioId(Long anuncioId);
    List<Valoracion> findByUsuarioId(Long usuarioId);
}
