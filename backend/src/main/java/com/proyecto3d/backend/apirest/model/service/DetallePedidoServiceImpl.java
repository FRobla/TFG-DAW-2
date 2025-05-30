package com.proyecto3d.backend.apirest.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto3d.backend.apirest.model.dao.DetallePedidoDao;
import com.proyecto3d.backend.apirest.model.dao.MaterialDao;
import com.proyecto3d.backend.apirest.model.entity.DetallePedido;
import com.proyecto3d.backend.apirest.model.entity.Material;

/**
 * Implementación del servicio para la gestión de detalles de pedidos
 * Contiene toda la lógica de negocio para las operaciones de detalles de pedidos
 */
@Service
public class DetallePedidoServiceImpl implements DetallePedidoService {

    @Autowired
    private DetallePedidoDao detallePedidoDao;

    @Autowired
    private MaterialDao materialDao;

    /**
     * Operaciones CRUD básicas
     */
    @Override
    @Transactional(readOnly = true)
    public List<DetallePedido> findAll() {
        return detallePedidoDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public DetallePedido findById(Long id) {
        Optional<DetallePedido> detallePedido = detallePedidoDao.findById(id);
        return detallePedido.orElse(null);
    }

    @Override
    @Transactional
    public DetallePedido save(DetallePedido detallePedido) {
        // Validaciones antes de guardar
        if (detallePedido.getEstado_item() == null) {
            detallePedido.setEstado_item("pendiente");
        }
        
        // Calcular subtotal antes de guardar
        detallePedido.calcularSubtotal();
        
        return detallePedidoDao.save(detallePedido);
    }

    @Override
    @Transactional
    public void delete(DetallePedido detallePedido) {
        detallePedidoDao.delete(detallePedido);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        detallePedidoDao.deleteById(id);
    }

    /**
     * Búsquedas específicas
     */
    @Override
    @Transactional(readOnly = true)
    public List<DetallePedido> findByPedidoId(Long pedidoId) {
        return detallePedidoDao.findByPedidoId(pedidoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetallePedido> findByAnuncioId(Long anuncioId) {
        return detallePedidoDao.findByAnuncioId(anuncioId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DetallePedido> findByAnuncioIdWithPagination(Long anuncioId, Pageable pageable) {
        return detallePedidoDao.findByAnuncioIdWithPagination(anuncioId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetallePedido> findByMaterialId(Long materialId) {
        return detallePedidoDao.findByMaterialId(materialId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetallePedido> findByEstadoItem(String estadoItem) {
        return detallePedidoDao.findByEstadoItem(estadoItem);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DetallePedido> findByEstadoItemWithPagination(String estadoItem, Pageable pageable) {
        return detallePedidoDao.findByEstadoItemWithPagination(estadoItem, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetallePedido> findByUsuarioId(Long usuarioId) {
        return detallePedidoDao.findByUsuarioId(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetallePedido> findByProveedorId(Long proveedorId) {
        return detallePedidoDao.findByProveedorId(proveedorId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DetallePedido> findByProveedorIdWithPagination(Long proveedorId, Pageable pageable) {
        return detallePedidoDao.findByProveedorIdWithPagination(proveedorId, pageable);
    }

    /**
     * Búsquedas con filtros combinados
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DetallePedido> findWithFilters(Long pedidoId, Long anuncioId, Long materialId, String estadoItem, Pageable pageable) {
        return detallePedidoDao.findWithFilters(pedidoId, anuncioId, materialId, estadoItem, pageable);
    }

    /**
     * Métodos de negocio específicos
     */
    @Override
    @Transactional
    public DetallePedido crearDetallePedido(DetallePedido detallePedido) {
        // Validaciones específicas para la creación
        if (detallePedido.getPedido() == null) {
            throw new IllegalArgumentException("El detalle debe tener un pedido asociado");
        }
        
        if (detallePedido.getAnuncio() == null) {
            throw new IllegalArgumentException("El detalle debe tener un anuncio asociado");
        }

        if (detallePedido.getCantidad() == null || detallePedido.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        if (detallePedido.getPrecio_unitario() == null || detallePedido.getPrecio_unitario() <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a 0");
        }

        // Establecer valores por defecto
        if (detallePedido.getEstado_item() == null) {
            detallePedido.setEstado_item("pendiente");
        }

        // Calcular subtotal
        detallePedido.calcularSubtotal();

        return detallePedidoDao.save(detallePedido);
    }

    @Override
    @Transactional
    public DetallePedido actualizarEstadoItem(Long detalleId, String nuevoEstado) {
        DetallePedido detalle = findById(detalleId);
        if (detalle == null) {
            throw new IllegalArgumentException("Detalle de pedido no encontrado con ID: " + detalleId);
        }

        // Validar transiciones de estado
        if (!esTransicionValidaDetalle(detalle.getEstado_item(), nuevoEstado)) {
            throw new IllegalArgumentException("Transición de estado no válida: " + detalle.getEstado_item() + " -> " + nuevoEstado);
        }

        detalle.setEstado_item(nuevoEstado);
        return detallePedidoDao.save(detalle);
    }

    @Override
    @Transactional
    public DetallePedido actualizarCantidad(Long detalleId, Integer nuevaCantidad) {
        DetallePedido detalle = findById(detalleId);
        if (detalle == null) {
            throw new IllegalArgumentException("Detalle de pedido no encontrado con ID: " + detalleId);
        }

        if (nuevaCantidad == null || nuevaCantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        // Solo permitir cambios si el detalle está pendiente
        if (!"pendiente".equals(detalle.getEstado_item())) {
            throw new IllegalArgumentException("Solo se puede cambiar la cantidad de items pendientes");
        }

        detalle.setCantidad(nuevaCantidad);
        detalle.calcularSubtotal();
        
        return detallePedidoDao.save(detalle);
    }

    @Override
    @Transactional
    public DetallePedido actualizarEspecificaciones(Long detalleId, String especificaciones) {
        DetallePedido detalle = findById(detalleId);
        if (detalle == null) {
            throw new IllegalArgumentException("Detalle de pedido no encontrado con ID: " + detalleId);
        }

        detalle.setEspecificaciones(especificaciones);
        return detallePedidoDao.save(detalle);
    }

    @Override
    @Transactional
    public DetallePedido actualizarNotas(Long detalleId, String notas) {
        DetallePedido detalle = findById(detalleId);
        if (detalle == null) {
            throw new IllegalArgumentException("Detalle de pedido no encontrado con ID: " + detalleId);
        }

        detalle.setNotas(notas);
        return detallePedidoDao.save(detalle);
    }

    /**
     * Estadísticas y reportes
     */
    @Override
    @Transactional(readOnly = true)
    public Long countByAnuncioId(Long anuncioId) {
        return detallePedidoDao.countByAnuncioId(anuncioId);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getTotalVendidoPorAnuncio(Long anuncioId) {
        Double total = detallePedidoDao.getTotalVendidoPorAnuncio(anuncioId);
        return total != null ? total : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getProductosMasVendidos() {
        return detallePedidoDao.getProductosMasVendidos();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getMaterialesMasUtilizados() {
        return detallePedidoDao.getMaterialesMasUtilizados();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getIngresosPorProveedor() {
        return detallePedidoDao.getIngresosPorProveedor();
    }

    /**
     * Gestión específica por proveedor
     */
    @Override
    @Transactional(readOnly = true)
    public List<DetallePedido> findDetallesPendientesByProveedor(Long proveedorId) {
        return detallePedidoDao.findDetallesPendientesByProveedor(proveedorId);
    }

    @Override
    @Transactional
    public List<DetallePedido> marcarItemsComoCompletados(List<Long> detalleIds) {
        List<DetallePedido> detallesActualizados = new ArrayList<>();
        
        for (Long detalleId : detalleIds) {
            try {
                DetallePedido detalle = marcarItemComoCompletado(detalleId);
                detallesActualizados.add(detalle);
            } catch (Exception e) {
                // Continuar con los demás si hay error en uno
                System.err.println("Error al completar detalle " + detalleId + ": " + e.getMessage());
            }
        }
        
        return detallesActualizados;
    }

    @Override
    @Transactional
    public DetallePedido marcarItemComoCompletado(Long detalleId) {
        DetallePedido detalle = findById(detalleId);
        if (detalle == null) {
            throw new IllegalArgumentException("Detalle de pedido no encontrado con ID: " + detalleId);
        }

        if (!"en_proceso".equals(detalle.getEstado_item()) && !"pendiente".equals(detalle.getEstado_item())) {
            throw new IllegalArgumentException("Solo se pueden completar items en proceso o pendientes");
        }

        detalle.setEstado_item("completado");
        return detallePedidoDao.save(detalle);
    }

    /**
     * Métodos de validación y utilidad
     */
    @Override
    @Transactional(readOnly = true)
    public boolean tieneDetallesPendientes(Long pedidoId) {
        List<DetallePedido> detalles = findByPedidoId(pedidoId);
        return detalles.stream().anyMatch(d -> "pendiente".equals(d.getEstado_item()) || "en_proceso".equals(d.getEstado_item()));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean todosLosDetallesCompletados(Long pedidoId) {
        List<DetallePedido> detalles = findByPedidoId(pedidoId);
        return !detalles.isEmpty() && detalles.stream().allMatch(d -> "completado".equals(d.getEstado_item()));
    }

    @Override
    @Transactional(readOnly = true)
    public Double calcularSubtotalPorPedido(Long pedidoId) {
        List<DetallePedido> detalles = findByPedidoId(pedidoId);
        return detalles.stream().mapToDouble(DetallePedido::getSubtotal).sum();
    }

    @Override
    @Transactional(readOnly = true)
    public Integer contarArticulosPorPedido(Long pedidoId) {
        List<DetallePedido> detalles = findByPedidoId(pedidoId);
        return detalles.stream().mapToInt(DetallePedido::getCantidad).sum();
    }

    /**
     * Gestión de materiales y especificaciones
     */
    @Override
    @Transactional
    public DetallePedido cambiarMaterial(Long detalleId, Long nuevoMaterialId) {
        DetallePedido detalle = findById(detalleId);
        if (detalle == null) {
            throw new IllegalArgumentException("Detalle de pedido no encontrado con ID: " + detalleId);
        }

        // Solo permitir cambios si el detalle está pendiente
        if (!"pendiente".equals(detalle.getEstado_item())) {
            throw new IllegalArgumentException("Solo se puede cambiar el material de items pendientes");
        }

        // Buscar el nuevo material
        Optional<Material> materialOpt = materialDao.findById(nuevoMaterialId);
        if (!materialOpt.isPresent()) {
            throw new IllegalArgumentException("Material no encontrado con ID: " + nuevoMaterialId);
        }

        detalle.setMaterial(materialOpt.get());
        return detallePedidoDao.save(detalle);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetallePedido> findByMaterialAndProveedor(Long materialId, Long proveedorId) {
        // Esta consulta requiere combinar filtros, usamos el método general
        return detallePedidoDao.findByMaterialId(materialId).stream()
                .filter(d -> d.getAnuncio().getUsuario().getId().equals(proveedorId))
                .toList();
    }

    /**
     * Método auxiliar para validar transiciones de estado en detalles
     */
    private boolean esTransicionValidaDetalle(String estadoActual, String nuevoEstado) {
        if (estadoActual == null || nuevoEstado == null) {
            return false;
        }

        switch (estadoActual) {
            case "pendiente":
                return "en_proceso".equals(nuevoEstado) || "cancelado".equals(nuevoEstado);
            case "en_proceso":
                return "completado".equals(nuevoEstado) || "cancelado".equals(nuevoEstado);
            case "completado":
            case "cancelado":
                return false; // Estados finales
            default:
                return false;
        }
    }
} 