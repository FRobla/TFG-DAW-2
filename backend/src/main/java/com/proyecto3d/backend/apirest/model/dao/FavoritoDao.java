package com.proyecto3d.backend.apirest.model.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto3d.backend.apirest.model.entity.Favorito;

public interface FavoritoDao extends JpaRepository<Favorito, Long> {
	
	// Obtener favoritos por usuario
	List<Favorito> findByUsuarioIdOrderByFechaMarcadoDesc(Long usuarioId);
	
	// Obtener favoritos por usuario (paginado)
	Page<Favorito> findByUsuarioIdOrderByFechaMarcadoDesc(Long usuarioId, Pageable pageable);
	
	// Obtener favoritos por usuario con JOIN FETCH (paginado)
	@Query("SELECT f FROM Favorito f " +
           "JOIN FETCH f.anuncio a " +
           "JOIN FETCH f.usuario u " +
           "WHERE f.usuario.id = :usuarioId " +
           "ORDER BY f.fechaMarcado DESC")
	Page<Favorito> findByUsuarioIdWithJoinFetch(@Param("usuarioId") Long usuarioId, Pageable pageable);
	
	// Obtener favoritos por anuncio
	List<Favorito> findByAnuncioId(Long anuncioId);
	
	// Verificar si un anuncio está marcado como favorito por un usuario
	boolean existsByAnuncioIdAndUsuarioId(Long anuncioId, Long usuarioId);
	
	// Obtener un favorito específico por anuncio y usuario
	Optional<Favorito> findByAnuncioIdAndUsuarioId(Long anuncioId, Long usuarioId);
	
	// Contar favoritos por anuncio
	@Query("SELECT COUNT(f) FROM Favorito f WHERE f.anuncio.id = :anuncioId")
	Long countByAnuncioId(@Param("anuncioId") Long anuncioId);
	
	// Contar favoritos por usuario
	@Query("SELECT COUNT(f) FROM Favorito f WHERE f.usuario.id = :usuarioId")
	Long countByUsuarioId(@Param("usuarioId") Long usuarioId);
	
	// Obtener anuncios más marcados como favoritos
	@Query("SELECT f.anuncio.id, COUNT(f) as cantidad " +
           "FROM Favorito f " +
           "GROUP BY f.anuncio.id " +
           "ORDER BY cantidad DESC")
    List<Object[]> findAnunciosMasFavoritos();
    
    // Eliminar favorito por anuncio y usuario
    void deleteByAnuncioIdAndUsuarioId(Long anuncioId, Long usuarioId);
} 