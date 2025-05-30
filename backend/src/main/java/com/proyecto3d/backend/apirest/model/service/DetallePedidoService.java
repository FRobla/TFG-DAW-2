package com.proyecto3d.backend.apirest.model.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.proyecto3d.backend.apirest.model.entity.DetallePedido;

/**
 * Interfaz de servicio para la gestión de detalles de pedidos
 * Define los métodos de negocio para las operaciones de detalles de pedidos
 */
public interface DetallePedidoService {

    /**
     * Operaciones CRUD básicas
     */
    public List<DetallePedido> findAll();
    
    public DetallePedido findById(Long id);
    
    public DetallePedido save(DetallePedido detallePedido);
    
    public void delete(DetallePedido detallePedido);
    
    public void deleteById(Long id);

    /**
     * Búsquedas específicas
     */
    public List<DetallePedido> findByPedidoId(Long pedidoId);
    
    public List<DetallePedido> findByAnuncioId(Long anuncioId);
    
    public Page<DetallePedido> findByAnuncioIdWithPagination(Long anuncioId, Pageable pageable);
    
    public List<DetallePedido> findByMaterialId(Long materialId);
    
    public List<DetallePedido> findByEstadoItem(String estadoItem);
    
    public Page<DetallePedido> findByEstadoItemWithPagination(String estadoItem, Pageable pageable);
    
    public List<DetallePedido> findByUsuarioId(Long usuarioId);
    
    public List<DetallePedido> findByProveedorId(Long proveedorId);
    
    public Page<DetallePedido> findByProveedorIdWithPagination(Long proveedorId, Pageable pageable);

    /**
     * Búsquedas con filtros combinados
     */
    public Page<DetallePedido> findWithFilters(Long pedidoId, Long anuncioId, Long materialId, String estadoItem, Pageable pageable);

    /**
     * Métodos de negocio específicos
     */
    public DetallePedido crearDetallePedido(DetallePedido detallePedido);
    
    public DetallePedido actualizarEstadoItem(Long detalleId, String nuevoEstado);
    
    public DetallePedido actualizarCantidad(Long detalleId, Integer nuevaCantidad);
    
    public DetallePedido actualizarEspecificaciones(Long detalleId, String especificaciones);
    
    public DetallePedido actualizarNotas(Long detalleId, String notas);

    /**
     * Estadísticas y reportes
     */
    public Long countByAnuncioId(Long anuncioId);
    
    public Double getTotalVendidoPorAnuncio(Long anuncioId);
    
    public List<Object[]> getProductosMasVendidos();
    
    public List<Object[]> getMaterialesMasUtilizados();
    
    public List<Object[]> getIngresosPorProveedor();

    /**
     * Gestión específica por proveedor
     */
    public List<DetallePedido> findDetallesPendientesByProveedor(Long proveedorId);
    
    public List<DetallePedido> marcarItemsComoCompletados(List<Long> detalleIds);
    
    public DetallePedido marcarItemComoCompletado(Long detalleId);

    /**
     * Métodos de validación y utilidad
     */
    public boolean tieneDetallesPendientes(Long pedidoId);
    
    public boolean todosLosDetallesCompletados(Long pedidoId);
    
    public Double calcularSubtotalPorPedido(Long pedidoId);
    
    public Integer contarArticulosPorPedido(Long pedidoId);

    /**
     * Gestión de materiales y especificaciones
     */
    public DetallePedido cambiarMaterial(Long detalleId, Long nuevoMaterialId);
    
    public List<DetallePedido> findByMaterialAndProveedor(Long materialId, Long proveedorId);

} 