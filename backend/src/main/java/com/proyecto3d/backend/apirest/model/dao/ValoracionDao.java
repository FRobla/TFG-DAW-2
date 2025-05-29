package com.proyecto3d.backend.apirest.model.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto3d.backend.apirest.model.entity.Valoracion;

public interface ValoracionDao extends JpaRepository<Valoracion, Long> {
	
	// Obtener valoraciones por anuncio
	List<Valoracion> findByAnuncioIdOrderByFechaValoracionDesc(Long anuncioId);
	
	// Obtener valoraciones por anuncio (paginado)
	Page<Valoracion> findByAnuncioIdOrderByFechaValoracionDesc(Long anuncioId, Pageable pageable);
	
	// Obtener valoraciones por usuario
	List<Valoracion> findByUsuarioIdOrderByFechaValoracionDesc(Long usuarioId);
	
	// Obtener valoraciones por puntuación
	List<Valoracion> findByPuntuacion(Integer puntuacion);
	
	// Obtener valoraciones verificadas
	List<Valoracion> findByVerificadoTrue();
	
	// Calcular valoración media de un anuncio
	@Query("SELECT AVG(v.puntuacion) FROM Valoracion v WHERE v.anuncio.id = :anuncioId")
	Double findValoracionMediaByAnuncioId(@Param("anuncioId") Long anuncioId);
	
	// Contar valoraciones por anuncio
	@Query("SELECT COUNT(v) FROM Valoracion v WHERE v.anuncio.id = :anuncioId")
	Long countByAnuncioId(@Param("anuncioId") Long anuncioId);
	
	// Obtener distribución de valoraciones por puntuación
	@Query("SELECT v.puntuacion, COUNT(v) as cantidad " +
           "FROM Valoracion v " +
           "GROUP BY v.puntuacion " +
           "ORDER BY v.puntuacion DESC")
    List<Object[]> findDistribucionValoraciones();
    
    // Verificar si un usuario ya valoró un anuncio
    boolean existsByAnuncioIdAndUsuarioId(Long anuncioId, Long usuarioId);
    
    // Obtener valoraciones con respuesta del proveedor
    List<Valoracion> findByRespuestaProveedorIsNotNull();
} 