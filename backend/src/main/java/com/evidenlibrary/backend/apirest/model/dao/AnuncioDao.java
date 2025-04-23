package com.evidenlibrary.backend.apirest.model.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.evidenlibrary.backend.apirest.model.entity.Impresora;
import com.evidenlibrary.backend.apirest.model.entity.Categoria;
import com.evidenlibrary.backend.apirest.model.entity.Anuncio;

public interface AnuncioDao extends JpaRepository<Anuncio, Long> {
	
	Optional<Anuncio> findByTitulo(String titulo);

	//Ranking
	@Query("SELECT l FROM Anuncio l LEFT JOIN l.valoraciones v GROUP BY l.id ORDER BY AVG(v.puntuacion) DESC LIMIT 10")
    List<Anuncio> findTop10MejorValorados();
	
	//Filtrar por impresora y categoria
	Page<Anuncio> findByImpresorasId(Long impresoraId, Pageable pageable);
	Page<Anuncio> findByCategoriasId(Long categoriaId, Pageable pageable);
	
	//Busqueda	
	List<Anuncio> findByTituloContainingIgnoreCase(String titulo);
	List<Anuncio> findByAnio(String anio);
	List<Anuncio> findByImpresorasContaining(Impresora impresoraId);    
    List<Anuncio> findByCategoriasContaining(Categoria categoriaId);
	
    @Query("SELECT DISTINCT l FROM Anuncio l " +
    	       "WHERE " +
    	       "(:term IS NULL OR :term = '') OR " +
    	       "LOWER(l.titulo) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
    	       //"LOWER(l.anio_publicacion) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
    	       "LOWER(l.descripcion) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Anuncio> findByTerm(@Param("term") String term);

	@Query("SELECT l FROM Anuncio l JOIN l.categorias g JOIN l.impresoras a WHERE g.id =:categoriaId AND a.id = :impresoraId")
	Page<Anuncio> findByCategoriasIdAndImpresorasId(@Param("categoriaId") Long categoriaId, @Param("impresoraId") Long impresoraId, Pageable pageable);


}

