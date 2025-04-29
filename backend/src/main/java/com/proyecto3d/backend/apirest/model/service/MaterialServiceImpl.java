package com.proyecto3d.backend.apirest.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto3d.backend.apirest.model.dao.MaterialDao;
import com.proyecto3d.backend.apirest.model.entity.Material;

@Service
public class MaterialServiceImpl implements MaterialService {
    
    @Autowired
    private MaterialDao materialDao;

    @Override
    public List<Material> findAll() {
        return (List<Material>) materialDao.findAll();
    }

    @Override
    public Material findById(Long id) {
        return materialDao.findById(id).orElse(null);
    }

    @Override
    public Material save(Material material) {
        return materialDao.save(material);
    }

    @Override
    public void delete(Material material) {
        materialDao.delete(material);
    }

    @Override
    public void deleteById(Long id) {
        materialDao.deleteById(id);
    }

    @Override
    public Material findByNombre(String nombre) {
        return materialDao.findByNombre(nombre).orElse(null);
    }

    @Override
    public List<Material> findByNombreContaining(String nombre) {
        return materialDao.findByNombreContaining(nombre);
    }
    
    @Override
    public List<Material> findByNombreContainingIgnoreCase(String nombre) {
        return materialDao.findByNombreContainingIgnoreCase(nombre);
    }
    
    @Override
    public List<Material> findByNombreContainingAndFabricanteContaining(String nombre, String fabricante) {
        return materialDao.findByNombreContainingAndFabricanteContaining(nombre, fabricante);
    }
    
    @Override
    public List<Material> findByNombreContainingAndColorContaining(String nombre, String color) {
        return materialDao.findByNombreContainingAndColorContaining(nombre, color);
    }

    @Override
    public List<Material> findByNombreContainingAndTipoContaining(String nombre, String tipo) {
        return materialDao.findByNombreContainingAndTipoContaining(nombre, tipo);
    }

    @Override
    public List<Material> findByNombreContainingAndPrecioKg(String nombre, Double precioKg) {
        return materialDao.findByNombreContainingAndPrecioKg(nombre, precioKg);
    }

}
