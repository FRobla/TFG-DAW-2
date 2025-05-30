package com.proyecto3d.backend.apirest.model.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto3d.backend.apirest.model.entity.Carrito;

/**
 * Repositorio para la entidad Carrito
 */
@Repository
public interface CarritoDAO extends JpaRepository<Carrito, Long> {

    /**
     * Busca todos los elementos del carrito de un usuario específico
     * @param usuarioId ID del usuario
     * @return Lista de elementos del carrito
     */
    @Query("SELECT c FROM Carrito c WHERE c.usuario.id = :usuarioId ORDER BY c.fechaAgregado DESC")
    List<Carrito> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Busca un elemento específico del carrito por usuario y anuncio
     * @param usuarioId ID del usuario
     * @param anuncioId ID del anuncio
     * @param materialSeleccionado Material seleccionado
     * @return Optional con el elemento del carrito si existe
     */
    @Query("SELECT c FROM Carrito c WHERE c.usuario.id = :usuarioId AND c.anuncio.id = :anuncioId AND c.materialSeleccionado = :materialSeleccionado")
    Optional<Carrito> findByUsuarioIdAndAnuncioIdAndMaterial(@Param("usuarioId") Long usuarioId, 
                                                            @Param("anuncioId") Long anuncioId,
                                                            @Param("materialSeleccionado") String materialSeleccionado);

    /**
     * Elimina todos los elementos del carrito de un usuario
     * @param usuarioId ID del usuario
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Carrito c WHERE c.usuario.id = :usuarioId")
    void deleteByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Cuenta el número de elementos en el carrito de un usuario
     * @param usuarioId ID del usuario
     * @return Número de elementos en el carrito
     */
    @Query("SELECT COUNT(c) FROM Carrito c WHERE c.usuario.id = :usuarioId")
    Long countByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Calcula el precio total del carrito de un usuario
     * @param usuarioId ID del usuario
     * @return Precio total del carrito
     */
    @Query("SELECT COALESCE(SUM(c.precioTotal), 0.0) FROM Carrito c WHERE c.usuario.id = :usuarioId")
    Double calcularTotalCarrito(@Param("usuarioId") Long usuarioId);
} 