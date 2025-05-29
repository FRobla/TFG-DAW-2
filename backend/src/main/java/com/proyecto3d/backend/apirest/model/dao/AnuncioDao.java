package com.proyecto3d.backend.apirest.model.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto3d.backend.apirest.model.entity.Anuncio;
import com.proyecto3d.backend.apirest.model.entity.Categoria;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnuncioDao extends JpaRepository<Anuncio, Long> {
	
	Optional<Anuncio> findByTitulo(String titulo);
	
	//Filtrar por categoria
	@Query("SELECT a FROM Anuncio a JOIN a.categorias c WHERE c.id = :categoriaId")
	Page<Anuncio> findByCategoriaId(@Param("categoriaId") Long categoriaId, Pageable pageable);
	
	//Filtrar por impresora
	Page<Anuncio> findByImpresoraId(Long impresoraId, Pageable pageable);
	
	//Busqueda	
	List<Anuncio> findByTituloContainingIgnoreCase(String titulo);
	List<Anuncio> findByCategoriasContaining(Categoria categoriaId);
	
	// Contar anuncios por categoría y estado
	long countByCategoriasContainingAndEstado(Categoria categoria, String estado);
	
    @Query("SELECT DISTINCT l FROM Anuncio l " +
    	       "WHERE " +
    	       "(:term IS NULL OR :term = '') OR " +
    	       "LOWER(l.titulo) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
    	       "LOWER(l.descripcion) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Anuncio> findByTerm(@Param("term") String term);

    // Búsqueda avanzada con múltiples filtros
    @Query("SELECT DISTINCT a FROM Anuncio a " +
           "LEFT JOIN a.usuario u " +
           "LEFT JOIN u.ubicacion ub " +
           "LEFT JOIN a.materiales m " +
           "WHERE (:query IS NULL OR :query = '' OR " +
           "LOWER(a.titulo) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(a.descripcion) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "AND (:categoriaIds IS NULL OR EXISTS (SELECT c FROM a.categorias c WHERE c.id IN :categoriaIds)) " +
           "AND (:ubicacionIds IS NULL OR ub.id IN :ubicacionIds) " +
           "AND (:materialIds IS NULL OR m.id IN :materialIds) " +
           "AND (:valoracionMin IS NULL OR a.valoracion_media >= :valoracionMin) " +
           "AND (:precioMin IS NULL OR a.precio_base >= :precioMin) " +
           "AND (:precioMax IS NULL OR a.precio_base <= :precioMax) " +
           "AND (:tiempoEntregaValores IS NULL OR a.tiempo_estimado IN :tiempoEntregaValores) " +
           "AND a.estado = 'activo'")
    Page<Anuncio> searchAnunciosAvanzada(
        @Param("query") String query,
        @Param("categoriaIds") List<Long> categoriaIds,
        @Param("ubicacionIds") List<Long> ubicacionIds,
        @Param("materialIds") List<Long> materialIds,
        @Param("valoracionMin") Double valoracionMin,
        @Param("precioMin") Double precioMin,
        @Param("precioMax") Double precioMax,
        @Param("tiempoEntregaValores") List<String> tiempoEntregaValores,
        Pageable pageable);

}
