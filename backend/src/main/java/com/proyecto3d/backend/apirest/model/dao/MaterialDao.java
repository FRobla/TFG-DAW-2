package com.proyecto3d.backend.apirest.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto3d.backend.apirest.model.entity.Material;

public interface MaterialDao extends JpaRepository<Material, Long> {
	
	// Búsqueda por nombre
	List<Material> findByNombreContainingIgnoreCase(String nombre);
    
    // Búsqueda por término general
    @Query("SELECT DISTINCT m FROM Material m " +
            "WHERE " +
            "(:term IS NULL OR :term = '') OR " +
            "LOWER(m.nombre) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(m.descripcion) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Material> findByTerm(@Param("term") String term);
    
    // Obtener materiales activos
    List<Material> findByActivoTrue();
    
    // Contar anuncios por material
    @Query("SELECT m.id, m.nombre, COUNT(a) as cantidad " +
           "FROM Material m LEFT JOIN m.anuncios a " +
           "WHERE m.activo = true " +
           "GROUP BY m.id, m.nombre " +
           "ORDER BY cantidad DESC")
    List<Object[]> findMaterialesConConteoAnuncios();
} 