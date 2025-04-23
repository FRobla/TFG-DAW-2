package com.proyecto3d.backend.apirest.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto3d.backend.apirest.model.entity.DetalleCarrito;

public interface DetalleCarritoDao extends JpaRepository<DetalleCarrito, Long> {
    List<DetalleCarrito> findByCarritoId(Long carritoId);
}
