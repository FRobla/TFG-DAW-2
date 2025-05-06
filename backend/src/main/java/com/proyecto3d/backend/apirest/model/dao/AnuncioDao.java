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
	
    @Query("SELECT DISTINCT l FROM Anuncio l " +
    	       "WHERE " +
    	       "(:term IS NULL OR :term = '') OR " +
    	       "LOWER(l.titulo) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
    	       "LOWER(l.descripcion) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Anuncio> findByTerm(@Param("term") String term);

}
