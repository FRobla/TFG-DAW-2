package com.proyecto3d.backend.apirest.model.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto3d.backend.apirest.model.entity.DetallePedido;

/**
 * DAO para la entidad DetallePedido
 * Define las operaciones de acceso a datos para los detalles de pedidos
 */
public interface DetallePedidoDao extends JpaRepository<DetallePedido, Long> {

    /**
     * Busca detalles de pedido por ID de pedido
     */
    @Query("SELECT dp FROM DetallePedido dp WHERE dp.pedido.id = :pedidoId ORDER BY dp.id")
    List<DetallePedido> findByPedidoId(@Param("pedidoId") Long pedidoId);

    /**
     * Busca detalles de pedido por ID de anuncio
     */
    @Query("SELECT dp FROM DetallePedido dp WHERE dp.anuncio.id = :anuncioId ORDER BY dp.pedido.fecha_pedido DESC")
    List<DetallePedido> findByAnuncioId(@Param("anuncioId") Long anuncioId);

    /**
     * Busca detalles de pedido por ID de anuncio con paginación
     */
    @Query("SELECT dp FROM DetallePedido dp WHERE dp.anuncio.id = :anuncioId ORDER BY dp.pedido.fecha_pedido DESC")
    Page<DetallePedido> findByAnuncioIdWithPagination(@Param("anuncioId") Long anuncioId, Pageable pageable);

    /**
     * Busca detalles de pedido por material
     */
    @Query("SELECT dp FROM DetallePedido dp WHERE dp.material.id = :materialId ORDER BY dp.pedido.fecha_pedido DESC")
    List<DetallePedido> findByMaterialId(@Param("materialId") Long materialId);

    /**
     * Busca detalles de pedido por estado del item
     */
    @Query("SELECT dp FROM DetallePedido dp WHERE dp.estado_item = :estadoItem ORDER BY dp.pedido.fecha_pedido DESC")
    List<DetallePedido> findByEstadoItem(@Param("estadoItem") String estadoItem);

    /**
     * Busca detalles de pedido por estado del item con paginación
     */
    @Query("SELECT dp FROM DetallePedido dp WHERE dp.estado_item = :estadoItem ORDER BY dp.pedido.fecha_pedido DESC")
    Page<DetallePedido> findByEstadoItemWithPagination(@Param("estadoItem") String estadoItem, Pageable pageable);

    /**
     * Busca detalles de pedido por usuario (a través del pedido)
     */
    @Query("SELECT dp FROM DetallePedido dp WHERE dp.pedido.usuario.id = :usuarioId ORDER BY dp.pedido.fecha_pedido DESC")
    List<DetallePedido> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Busca detalles de pedido por proveedor (a través del anuncio)
     */
    @Query("SELECT dp FROM DetallePedido dp WHERE dp.anuncio.usuario.id = :proveedorId ORDER BY dp.pedido.fecha_pedido DESC")
    List<DetallePedido> findByProveedorId(@Param("proveedorId") Long proveedorId);

    /**
     * Busca detalles de pedido por proveedor con paginación
     */
    @Query("SELECT dp FROM DetallePedido dp WHERE dp.anuncio.usuario.id = :proveedorId ORDER BY dp.pedido.fecha_pedido DESC")
    Page<DetallePedido> findByProveedorIdWithPagination(@Param("proveedorId") Long proveedorId, Pageable pageable);

    /**
     * Cuenta detalles de pedido por anuncio
     */
    @Query("SELECT COUNT(dp) FROM DetallePedido dp WHERE dp.anuncio.id = :anuncioId")
    Long countByAnuncioId(@Param("anuncioId") Long anuncioId);

    /**
     * Calcula el total vendido por anuncio
     */
    @Query("SELECT SUM(dp.subtotal) FROM DetallePedido dp WHERE dp.anuncio.id = :anuncioId AND dp.pedido.estado = 'completado'")
    Double getTotalVendidoPorAnuncio(@Param("anuncioId") Long anuncioId);

    /**
     * Obtiene los productos más vendidos
     */
    @Query("SELECT dp.anuncio.id, dp.anuncio.titulo, SUM(dp.cantidad) as total_vendido " +
           "FROM DetallePedido dp WHERE dp.pedido.estado = 'completado' " +
           "GROUP BY dp.anuncio.id, dp.anuncio.titulo " +
           "ORDER BY total_vendido DESC")
    List<Object[]> getProductosMasVendidos();

    /**
     * Obtiene estadísticas de materiales más utilizados
     */
    @Query("SELECT dp.material.nombre, COUNT(dp) as cantidad_pedidos " +
           "FROM DetallePedido dp WHERE dp.material IS NOT NULL AND dp.pedido.estado = 'completado' " +
           "GROUP BY dp.material.nombre " +
           "ORDER BY cantidad_pedidos DESC")
    List<Object[]> getMaterialesMasUtilizados();

    /**
     * Busca detalles pendientes por proveedor
     */
    @Query("SELECT dp FROM DetallePedido dp WHERE dp.anuncio.usuario.id = :proveedorId " +
           "AND dp.estado_item IN ('pendiente', 'en_proceso') " +
           "ORDER BY dp.pedido.fecha_pedido ASC")
    List<DetallePedido> findDetallesPendientesByProveedor(@Param("proveedorId") Long proveedorId);

    /**
     * Busca detalles por filtros combinados
     */
    @Query("SELECT dp FROM DetallePedido dp WHERE " +
           "(:pedidoId IS NULL OR dp.pedido.id = :pedidoId) AND " +
           "(:anuncioId IS NULL OR dp.anuncio.id = :anuncioId) AND " +
           "(:materialId IS NULL OR dp.material.id = :materialId) AND " +
           "(:estadoItem IS NULL OR dp.estado_item = :estadoItem) " +
           "ORDER BY dp.pedido.fecha_pedido DESC")
    Page<DetallePedido> findWithFilters(@Param("pedidoId") Long pedidoId,
                                        @Param("anuncioId") Long anuncioId,
                                        @Param("materialId") Long materialId,
                                        @Param("estadoItem") String estadoItem,
                                        Pageable pageable);

    /**
     * Calcula ingresos por proveedor
     */
    @Query("SELECT dp.anuncio.usuario.id, dp.anuncio.usuario.nombre, dp.anuncio.usuario.apellido, " +
           "SUM(dp.subtotal) as total_ingresos " +
           "FROM DetallePedido dp WHERE dp.pedido.estado = 'completado' " +
           "GROUP BY dp.anuncio.usuario.id, dp.anuncio.usuario.nombre, dp.anuncio.usuario.apellido " +
           "ORDER BY total_ingresos DESC")
    List<Object[]> getIngresosPorProveedor();

} 