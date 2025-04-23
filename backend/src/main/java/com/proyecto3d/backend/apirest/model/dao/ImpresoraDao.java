package com.proyecto3d.backend.apirest.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto3d.backend.apirest.model.entity.Impresora;

public interface ImpresoraDao extends JpaRepository<Impresora, Long> {
	
    //Busqueda
	List<Impresora> findByNombreContainingIgnoreCase(String nombre);
    
    @Query("SELECT DISTINCT a FROM Impresora a " +
            "WHERE " +
            "(:term IS NULL OR :term = '') OR " +
            "LOWER(a.nombre) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(a.modelo) LIKE LOWER(CONCAT('%', :term, '%'))")
     List<Impresora> findByTerm(@Param("term") String term);
}
