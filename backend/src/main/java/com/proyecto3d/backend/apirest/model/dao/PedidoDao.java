package com.proyecto3d.backend.apirest.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto3d.backend.apirest.model.entity.Pedido;

public interface PedidoDao extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuarioId(Long usuarioId);
}
