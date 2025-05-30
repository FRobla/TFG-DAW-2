package com.proyecto3d.backend.apirest.model.service;

import java.util.List;
import java.util.Optional;

import com.proyecto3d.backend.apirest.model.dto.CarritoDTO;
import com.proyecto3d.backend.apirest.model.entity.Carrito;

/**
 * Interfaz del servicio para la gestión del carrito
 */
public interface CarritoService {

    /**
     * Añade un elemento al carrito
     * @param usuarioId ID del usuario
     * @param anuncioId ID del anuncio
     * @param cantidad Cantidad a añadir
     * @param materialSeleccionado Material seleccionado
     * @param colorSeleccionado Color seleccionado (opcional)
     * @param acabadoPremium Si incluye acabado premium
     * @param urgente Si es urgente
     * @param envioGratis Si incluye envío gratis
     * @return El elemento del carrito creado o actualizado
     */
    CarritoDTO anadirAlCarrito(Long usuarioId, Long anuncioId, Integer cantidad, 
                             String materialSeleccionado, String colorSeleccionado,
                             Boolean acabadoPremium, Boolean urgente, Boolean envioGratis);

    /**
     * Obtiene todos los elementos del carrito de un usuario
     * @param usuarioId ID del usuario
     * @return Lista de DTOs del carrito
     */
    List<CarritoDTO> obtenerCarritoUsuario(Long usuarioId);

    /**
     * Actualiza la cantidad de un elemento del carrito
     * @param carritoId ID del elemento del carrito
     * @param nuevaCantidad Nueva cantidad
     * @return El elemento del carrito actualizado
     */
    CarritoDTO actualizarCantidad(Long carritoId, Integer nuevaCantidad);

    /**
     * Actualiza los servicios adicionales de un elemento del carrito
     * @param carritoId ID del elemento del carrito
     * @param acabadoPremium Si incluye acabado premium
     * @param urgente Si es urgente
     * @param envioGratis Si incluye envío gratis
     * @return El elemento del carrito actualizado
     */
    CarritoDTO actualizarServiciosAdicionales(Long carritoId, Boolean acabadoPremium, 
                                            Boolean urgente, Boolean envioGratis);

    /**
     * Elimina un elemento del carrito
     * @param carritoId ID del elemento del carrito
     */
    void eliminarElementoCarrito(Long carritoId);

    /**
     * Vacía todo el carrito de un usuario
     * @param usuarioId ID del usuario
     */
    void vaciarCarrito(Long usuarioId);

    /**
     * Obtiene el número de elementos en el carrito de un usuario
     * @param usuarioId ID del usuario
     * @return Número de elementos
     */
    Long contarElementosCarrito(Long usuarioId);

    /**
     * Calcula el precio total del carrito de un usuario
     * @param usuarioId ID del usuario
     * @return Precio total del carrito
     */
    Double calcularTotalCarrito(Long usuarioId);
} 