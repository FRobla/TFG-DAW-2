package com.proyecto3d.backend.apirest.model.dao;

import org.springframework.data.repository.CrudRepository;

import com.proyecto3d.backend.apirest.model.entity.Impresora;

/**
 * Interfaz DAO para acceso a datos de impresoras
 */
public interface ImpresoraDao extends CrudRepository<Impresora, Long> {
    // Se hereda la funcionalidad básica de CRUD
} 