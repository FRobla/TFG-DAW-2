package com.proyecto3d.backend.apirest.model.service;

import java.util.List;
import java.util.Optional;

import com.proyecto3d.backend.apirest.model.entity.Impresora;

/**
 * Interfaz para el servicio de gestión de impresoras
 */
public interface ImpresoraService {
    
    /**
     * Obtiene todas las impresoras
     * @return Lista de impresoras
     */
    public List<Impresora> findAll();
    
    /**
     * Busca una impresora por su ID
     * @param id ID de la impresora
     * @return Impresora encontrada o vacío
     */
    public Optional<Impresora> findById(Long id);
    
    /**
     * Guarda o actualiza una impresora
     * @param impresora Datos de la impresora
     * @return Impresora guardada
     */
    public Impresora save(Impresora impresora);
    
    /**
     * Elimina una impresora por su ID
     * @param id ID de la impresora
     */
    public void delete(Long id);
} 