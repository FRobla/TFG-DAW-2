package com.proyecto3d.backend.apirest.model.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto3d.backend.apirest.model.entity.Pedido;

/**
 * Repositorio para la gestión de pedidos
 * Contiene todos los métodos de consulta necesarios para el servicio
 */
public interface PedidoDao extends JpaRepository<Pedido, Long> {
    
    /**
     * Busca pedidos por usuario
     */
    @Query("SELECT p FROM Pedido p WHERE p.usuario.id = :usuarioId ORDER BY p.fecha_pedido DESC")
    List<Pedido> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Busca pedidos por usuario con paginación
     */
    @Query("SELECT p FROM Pedido p WHERE p.usuario.id = :usuarioId ORDER BY p.fecha_pedido DESC")
    Page<Pedido> findByUsuarioIdWithPagination(@Param("usuarioId") Long usuarioId, Pageable pageable);

    /**
     * Busca pedidos por estado
     */
    @Query("SELECT p FROM Pedido p WHERE p.estado = :estado ORDER BY p.fecha_pedido DESC")
    List<Pedido> findByEstado(@Param("estado") String estado);

    /**
     * Busca pedidos por estado con paginación
     */
    @Query("SELECT p FROM Pedido p WHERE (:estado IS NULL OR p.estado = :estado) ORDER BY p.fecha_pedido DESC")
    Page<Pedido> findByEstadoWithPagination(@Param("estado") String estado, Pageable pageable);

    /**
     * Busca pedidos por número de pedido
     */
    @Query("SELECT p FROM Pedido p WHERE p.numero_pedido = :numeroPedido")
    Pedido findByNumeroPedido(@Param("numeroPedido") String numeroPedido);

    /**
     * Busca pedidos por rango de fechas
     */
    @Query("SELECT p FROM Pedido p WHERE p.fecha_pedido BETWEEN :fechaInicio AND :fechaFin ORDER BY p.fecha_pedido DESC")
    List<Pedido> findByFechaPedidoBetween(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);

    /**
     * Busca pedidos por rango de fechas con paginación
     */
    @Query("SELECT p FROM Pedido p WHERE p.fecha_pedido BETWEEN :fechaInicio AND :fechaFin ORDER BY p.fecha_pedido DESC")
    Page<Pedido> findByFechaPedidoBetweenWithPagination(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin, Pageable pageable);

    /**
     * Busca pedidos por método de pago
     */
    @Query("SELECT p FROM Pedido p WHERE p.metodo_pago = :metodoPago ORDER BY p.fecha_pedido DESC")
    List<Pedido> findByMetodoPago(@Param("metodoPago") String metodoPago);

    /**
     * Busca pedidos por total mayor a una cantidad
     */
    @Query("SELECT p FROM Pedido p WHERE p.total >= :totalMinimo ORDER BY p.total DESC")
    List<Pedido> findByTotalGreaterThanEqual(@Param("totalMinimo") Double totalMinimo);

    /**
     * Obtiene estadísticas de pedidos por estado
     */
    @Query("SELECT p.estado, COUNT(p) FROM Pedido p GROUP BY p.estado")
    List<Object[]> getEstadisticasPorEstado();

    /**
     * Obtiene el total de ventas por mes
     */
    @Query("SELECT YEAR(p.fecha_pedido), MONTH(p.fecha_pedido), SUM(p.total) " +
           "FROM Pedido p WHERE p.estado = 'completado' " +
           "GROUP BY YEAR(p.fecha_pedido), MONTH(p.fecha_pedido) " +
           "ORDER BY YEAR(p.fecha_pedido) DESC, MONTH(p.fecha_pedido) DESC")
    List<Object[]> getVentasPorMes();

    /**
     * Busca pedidos con filtros combinados
     */
    @Query("SELECT p FROM Pedido p WHERE " +
           "(:usuarioId IS NULL OR p.usuario.id = :usuarioId) AND " +
           "(:estado IS NULL OR p.estado = :estado) AND " +
           "(:fechaInicio IS NULL OR p.fecha_pedido >= :fechaInicio) AND " +
           "(:fechaFin IS NULL OR p.fecha_pedido <= :fechaFin) " +
           "ORDER BY p.fecha_pedido DESC")
    Page<Pedido> findWithFilters(@Param("usuarioId") Long usuarioId,
                                 @Param("estado") String estado,
                                 @Param("fechaInicio") Date fechaInicio,
                                 @Param("fechaFin") Date fechaFin,
                                 Pageable pageable);

    /**
     * Cuenta pedidos por usuario
     */
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.usuario.id = :usuarioId")
    Long countByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Busca pedidos pendientes de entrega
     */
    @Query("SELECT p FROM Pedido p WHERE p.estado IN ('pendiente', 'en_proceso', 'enviado') " +
           "ORDER BY p.fecha_entrega_estimada ASC")
    List<Pedido> findPedidosPendientes();

    /**
     * Busca pedidos por ciudad
     */
    @Query("SELECT p FROM Pedido p WHERE p.ciudad = :ciudad ORDER BY p.fecha_pedido DESC")
    List<Pedido> findByCiudad(@Param("ciudad") String ciudad);

    /**
     * Busca pedidos por provincia
     */
    @Query("SELECT p FROM Pedido p WHERE p.provincia = :provincia ORDER BY p.fecha_pedido DESC")
    List<Pedido> findByProvincia(@Param("provincia") String provincia);

    /**
     * Busca pedidos por código postal
     */
    @Query("SELECT p FROM Pedido p WHERE p.codigo_postal = :codigoPostal ORDER BY p.fecha_pedido DESC")
    List<Pedido> findByCodigoPostal(@Param("codigoPostal") String codigoPostal);

    /**
     * Busca pedidos pendientes por usuario
     */
    @Query("SELECT p FROM Pedido p WHERE p.usuario.id = :usuarioId AND p.estado IN ('pendiente', 'en_proceso') ORDER BY p.fecha_pedido DESC")
    List<Pedido> findPedidosPendientesByUsuario(@Param("usuarioId") Long usuarioId);

    /**
     * Busca pedidos completados por usuario
     */
    @Query("SELECT p FROM Pedido p WHERE p.usuario.id = :usuarioId AND p.estado = 'completado' ORDER BY p.fecha_pedido DESC")
    List<Pedido> findPedidosCompletadosByUsuario(@Param("usuarioId") Long usuarioId);

    /**
     * Obtiene el total gastado por usuario
     */
    @Query("SELECT SUM(p.total) FROM Pedido p WHERE p.usuario.id = :usuarioId AND p.estado = 'completado'")
    Double getTotalGastadoByUsuario(@Param("usuarioId") Long usuarioId);

    /**
     * Busca pedidos por referencia de pago
     */
    @Query("SELECT p FROM Pedido p WHERE p.referencia_pago = :referenciaPago")
    Pedido findByReferenciaPago(@Param("referenciaPago") String referenciaPago);

    /**
     * Busca pedidos que requieren atención (vencidos)
     */
    @Query("SELECT p FROM Pedido p WHERE p.estado IN ('pendiente', 'en_proceso') AND p.fecha_entrega_estimada < CURRENT_DATE ORDER BY p.fecha_entrega_estimada ASC")
    List<Pedido> findPedidosVencidos();

    /**
     * Obtiene estadísticas de ventas por método de pago
     */
    @Query("SELECT p.metodo_pago, COUNT(p), SUM(p.total) FROM Pedido p WHERE p.estado = 'completado' GROUP BY p.metodo_pago")
    List<Object[]> getEstadisticasPorMetodoPago();

    /**
     * Busca pedidos entre fechas de entrega
     */
    @Query("SELECT p FROM Pedido p WHERE p.fecha_entrega_estimada BETWEEN :fechaInicio AND :fechaFin ORDER BY p.fecha_entrega_estimada ASC")
    List<Pedido> findByFechaEntregaBetween(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);
} 