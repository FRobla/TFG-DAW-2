package com.proyecto3d.backend.apirest.model.service;

import java.util.List;

import com.proyecto3d.backend.apirest.model.entity.Material;

public interface MaterialService {
    
    public List<Material> findAll();

    public Material findById(Long id);

    public Material save(Material material);

    public void delete(Material material);

    public void deleteById(Long id);

    public Material findByNombre(String nombre);

    public List<Material> findByNombreContaining(String nombre);

    public List<Material> findByNombreContainingIgnoreCase(String nombre);

    public List<Material> findByNombreContainingAndFabricanteContaining(String nombre, String fabricante);

    public List<Material> findByNombreContainingAndColorContaining(String nombre, String color);

    public List<Material> findByNombreContainingAndTipoContaining(String nombre, String tipo);

    public List<Material> findByNombreContainingAndPrecioKg(String nombre, Double precioKg);
}
