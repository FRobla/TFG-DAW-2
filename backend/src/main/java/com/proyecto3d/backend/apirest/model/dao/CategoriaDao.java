package com.proyecto3d.backend.apirest.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto3d.backend.apirest.model.entity.Categoria;

public interface CategoriaDao extends JpaRepository<Categoria, Long> {
	
	//Busqueda
	
	List<Categoria> findByNombreContainingIgnoreCase(String nombre);
    
    @Query("SELECT DISTINCT g FROM Categoria g " +
            "WHERE " +
            "(:term IS NULL OR :term = '') OR " +
            "LOWER(g.nombre) LIKE LOWER(CONCAT('%', :term, '%'))")
     List<Categoria> findByTerm(@Param("term") String term);
}
