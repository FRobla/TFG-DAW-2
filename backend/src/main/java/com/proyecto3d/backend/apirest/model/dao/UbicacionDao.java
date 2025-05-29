package com.proyecto3d.backend.apirest.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto3d.backend.apirest.model.entity.Ubicacion;

public interface UbicacionDao extends JpaRepository<Ubicacion, Long> {
	
	// Búsqueda por nombre
	List<Ubicacion> findByNombreContainingIgnoreCase(String nombre);
    
    // Búsqueda por término general
    @Query("SELECT DISTINCT u FROM Ubicacion u " +
            "WHERE " +
            "(:term IS NULL OR :term = '') OR " +
            "LOWER(u.nombre) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(u.provincia) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(u.comunidad_autonoma) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Ubicacion> findByTerm(@Param("term") String term);
    
    // Obtener ubicaciones activas
    List<Ubicacion> findByActivoTrue();
    
    // Contar usuarios por ubicación
    @Query("SELECT u.id, u.nombre, COUNT(us) as cantidad " +
           "FROM Ubicacion u LEFT JOIN u.usuarios us " +
           "WHERE u.activo = true " +
           "GROUP BY u.id, u.nombre " +
           "ORDER BY cantidad DESC")
    List<Object[]> findUbicacionesConConteoUsuarios();
    
    // Contar anuncios por ubicación (a través de usuarios)
    @Query("SELECT u.id, u.nombre, COUNT(a) as cantidad " +
           "FROM Ubicacion u " +
           "LEFT JOIN u.usuarios us " +
           "LEFT JOIN us.anuncios a " +
           "WHERE u.activo = true AND (a.estado = 'activo' OR a.estado IS NULL) " +
           "GROUP BY u.id, u.nombre " +
           "ORDER BY cantidad DESC")
    List<Object[]> findUbicacionesConConteoAnuncios();
} 