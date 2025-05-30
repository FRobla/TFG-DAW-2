package com.proyecto3d.backend.apirest.model.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.proyecto3d.backend.apirest.model.entity.Pedido;

/**
 * Interfaz de servicio para la gestión de pedidos
 * Define los métodos de negocio para las operaciones de pedidos
 */
public interface PedidoService {

    /**
     * Operaciones CRUD básicas
     */
    public List<Pedido> findAll();
    
    public Pedido findById(Long id);
    
    public Pedido save(Pedido pedido);
    
    public void delete(Pedido pedido);
    
    public void deleteById(Long id);

    /**
     * Búsquedas específicas
     */
    public List<Pedido> findByUsuarioId(Long usuarioId);
    
    public Page<Pedido> findByUsuarioIdWithPagination(Long usuarioId, Pageable pageable);
    
    public List<Pedido> findByEstado(String estado);
    
    public Page<Pedido> findByEstadoWithPagination(String estado, Pageable pageable);
    
    public Pedido findByNumeroPedido(String numeroPedido);
    
    public List<Pedido> findByFechaPedidoBetween(Date fechaInicio, Date fechaFin);
    
    public Page<Pedido> findByFechaPedidoBetweenWithPagination(Date fechaInicio, Date fechaFin, Pageable pageable);
    
    public List<Pedido> findByMetodoPago(String metodoPago);
    
    public List<Pedido> findByTotalGreaterThanEqual(Double totalMinimo);

    /**
     * Búsquedas con filtros combinados
     */
    public Page<Pedido> findWithFilters(Long usuarioId, String estado, Date fechaInicio, Date fechaFin, Pageable pageable);

    /**
     * Métodos de negocio específicos
     */
    public Pedido crearPedido(Pedido pedido);
    
    public Pedido actualizarEstado(Long pedidoId, String nuevoEstado);
    
    public Pedido procesarPago(Long pedidoId, String metodoPago, String referenciaPago);
    
    public Pedido completarPedido(Long pedidoId);
    
    public Pedido cancelarPedido(Long pedidoId, String motivo);

    /**
     * Estadísticas y reportes
     */
    public List<Object[]> getEstadisticasPorEstado();
    
    public List<Object[]> getVentasPorMes();
    
    public Long countByUsuarioId(Long usuarioId);
    
    public List<Pedido> findPedidosPendientes();
    
    public List<Pedido> findByCiudad(String ciudad);

    /**
     * Métodos de validación y utilidad
     */
    public boolean existsByNumeroPedido(String numeroPedido);
    
    public boolean puedeSerCancelado(Long pedidoId);
    
    public boolean estaCompletado(Long pedidoId);
    
    public Double calcularTotalPedido(Long pedidoId);

    /**
     * Gestión de fechas de entrega
     */
    public Pedido actualizarFechaEntregaEstimada(Long pedidoId, Date fechaEntrega);
    
    public Pedido marcarComoEntregado(Long pedidoId);

    /**
     * Gestión de notas
     */
    public Pedido actualizarNotasCliente(Long pedidoId, String notas);
    
    public Pedido actualizarNotasInternas(Long pedidoId, String notas);

} 