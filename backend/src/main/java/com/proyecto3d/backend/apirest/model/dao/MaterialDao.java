package com.proyecto3d.backend.apirest.model.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto3d.backend.apirest.model.entity.Material;

public interface MaterialDao extends JpaRepository<Material, Long> {

    Optional<Material> findByNombre(String nombre);
    
    List<Material> findByNombreContaining(String nombre);
    
    List<Material> findByNombreContainingIgnoreCase(String nombre);
    
    List<Material> findByNombreContainingAndFabricanteContaining(String nombre, String fabricante);
    
    List<Material> findByNombreContainingAndColorContaining(String nombre, String color);
    
    List<Material> findByNombreContainingAndTipoContaining(String nombre, String tipo);
    
    List<Material> findByNombreContainingAndPrecioKg(String nombre, Double precioKg);
    
    List<Material> findByNombreContainingAndStock(String nombre, Integer stock);
    
}
