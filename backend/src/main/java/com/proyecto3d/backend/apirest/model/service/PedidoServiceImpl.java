package com.proyecto3d.backend.apirest.model.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto3d.backend.apirest.model.dao.PedidoDao;
import com.proyecto3d.backend.apirest.model.dao.CarritoDAO;
import com.proyecto3d.backend.apirest.model.dao.UsuarioDao;
import com.proyecto3d.backend.apirest.model.entity.Pedido;
import com.proyecto3d.backend.apirest.model.entity.DetallePedido;
import com.proyecto3d.backend.apirest.model.entity.Carrito;
import com.proyecto3d.backend.apirest.model.entity.Usuario;

/**
 * Implementación del servicio para la gestión de pedidos
 * Contiene toda la lógica de negocio para las operaciones de pedidos
 */
@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoDao pedidoDao;

    @Autowired
    private CarritoDAO carritoDAO;

    @Autowired
    private UsuarioDao usuarioDao;

    /**
     * Operaciones CRUD básicas
     */
    @Override
    @Transactional(readOnly = true)
    public List<Pedido> findAll() {
        return pedidoDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Pedido findById(Long id) {
        Optional<Pedido> pedido = pedidoDao.findById(id);
        return pedido.orElse(null);
    }

    @Override
    @Transactional
    public Pedido save(Pedido pedido) {
        // Validaciones antes de guardar
        if (pedido.getEstado() == null) {
            pedido.setEstado("pendiente");
        }
        if (pedido.getFecha_pedido() == null) {
            pedido.setFecha_pedido(new Date());
        }
        
        // Calcular totales antes de guardar
        pedido.calcularTotal();
        
        return pedidoDao.save(pedido);
    }

    @Override
    @Transactional
    public void delete(Pedido pedido) {
        pedidoDao.delete(pedido);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        pedidoDao.deleteById(id);
    }

    /**
     * Búsquedas específicas
     */
    @Override
    @Transactional(readOnly = true)
    public List<Pedido> findByUsuarioId(Long usuarioId) {
        return pedidoDao.findByUsuarioId(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pedido> findByUsuarioIdWithPagination(Long usuarioId, Pageable pageable) {
        return pedidoDao.findByUsuarioIdWithPagination(usuarioId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> findByEstado(String estado) {
        return pedidoDao.findByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pedido> findByEstadoWithPagination(String estado, Pageable pageable) {
        return pedidoDao.findByEstadoWithPagination(estado, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Pedido findByNumeroPedido(String numeroPedido) {
        return pedidoDao.findByNumeroPedido(numeroPedido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> findByFechaPedidoBetween(Date fechaInicio, Date fechaFin) {
        return pedidoDao.findByFechaPedidoBetween(fechaInicio, fechaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pedido> findByFechaPedidoBetweenWithPagination(Date fechaInicio, Date fechaFin, Pageable pageable) {
        return pedidoDao.findByFechaPedidoBetweenWithPagination(fechaInicio, fechaFin, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> findByMetodoPago(String metodoPago) {
        return pedidoDao.findByMetodoPago(metodoPago);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> findByTotalGreaterThanEqual(Double totalMinimo) {
        return pedidoDao.findByTotalGreaterThanEqual(totalMinimo);
    }

    /**
     * Búsquedas con filtros combinados
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Pedido> findWithFilters(Long usuarioId, String estado, Date fechaInicio, Date fechaFin, Pageable pageable) {
        return pedidoDao.findWithFilters(usuarioId, estado, fechaInicio, fechaFin, pageable);
    }

    /**
     * Métodos de negocio específicos
     */
    @Override
    @Transactional
    public Pedido crearPedido(Pedido pedido) {
        // Validaciones específicas para la creación
        if (pedido.getUsuario() == null) {
            throw new IllegalArgumentException("El pedido debe tener un usuario asociado");
        }
        
        if (pedido.getDetallesPedido() == null || pedido.getDetallesPedido().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos un detalle");
        }

        // Establecer valores por defecto
        pedido.setEstado("pendiente");
        pedido.setFecha_pedido(new Date());
        
        // Generar número de pedido si no existe
        if (pedido.getNumero_pedido() == null) {
            String numeroPedido;
            do {
                numeroPedido = "PED" + System.currentTimeMillis();
            } while (existsByNumeroPedido(numeroPedido));
            pedido.setNumero_pedido(numeroPedido);
        }

        // Calcular totales
        pedido.calcularTotal();

        return pedidoDao.save(pedido);
    }

    @Override
    @Transactional
    public Pedido actualizarEstado(Long pedidoId, String nuevoEstado) {
        Pedido pedido = findById(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido no encontrado con ID: " + pedidoId);
        }

        // Validar transiciones de estado
        if (!esTransicionValida(pedido.getEstado(), nuevoEstado)) {
            throw new IllegalArgumentException("Transición de estado no válida: " + pedido.getEstado() + " -> " + nuevoEstado);
        }

        pedido.setEstado(nuevoEstado);

        return pedidoDao.save(pedido);
    }

    @Override
    @Transactional
    public Pedido procesarPago(Long pedidoId, String metodoPago, String referenciaPago) {
        Pedido pedido = findById(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido no encontrado con ID: " + pedidoId);
        }

        if (!"pendiente".equals(pedido.getEstado())) {
            throw new IllegalArgumentException("Solo se pueden procesar pagos de pedidos pendientes");
        }

        pedido.setMetodo_pago(metodoPago);
        pedido.setReferencia_pago(referenciaPago);
        pedido.setEstado("en_proceso");

        return pedidoDao.save(pedido);
    }

    @Override
    @Transactional
    public Pedido completarPedido(Long pedidoId) {
        Pedido pedido = findById(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido no encontrado con ID: " + pedidoId);
        }

        if (!"en_proceso".equals(pedido.getEstado())) {
            throw new IllegalArgumentException("Solo se pueden completar pedidos en proceso");
        }

        pedido.setEstado("completado");
        pedido.setFecha_entrega_real(new Date());

        return pedidoDao.save(pedido);
    }

    @Override
    @Transactional
    public Pedido cancelarPedido(Long pedidoId, String motivo) {
        Pedido pedido = findById(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido no encontrado con ID: " + pedidoId);
        }

        if (!pedido.puedeSerCancelado()) {
            throw new IllegalArgumentException("El pedido no puede ser cancelado en su estado actual: " + pedido.getEstado());
        }

        pedido.setEstado("cancelado");
        if (motivo != null && !motivo.trim().isEmpty()) {
            String notasActuales = pedido.getNotas_internas() != null ? pedido.getNotas_internas() : "";
            pedido.setNotas_internas(notasActuales + "\nMotivo de cancelación: " + motivo);
        }

        return pedidoDao.save(pedido);
    }

    /**
     * Estadísticas y reportes
     */
    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getEstadisticasPorEstado() {
        return pedidoDao.getEstadisticasPorEstado();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getVentasPorMes() {
        return pedidoDao.getVentasPorMes();
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByUsuarioId(Long usuarioId) {
        return pedidoDao.countByUsuarioId(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> findPedidosPendientes() {
        return pedidoDao.findPedidosPendientes();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> findByCiudad(String ciudad) {
        return pedidoDao.findByCiudad(ciudad);
    }

    /**
     * Métodos de validación y utilidad
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsByNumeroPedido(String numeroPedido) {
        return pedidoDao.findByNumeroPedido(numeroPedido) != null;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean puedeSerCancelado(Long pedidoId) {
        Pedido pedido = findById(pedidoId);
        return pedido != null && pedido.puedeSerCancelado();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean estaCompletado(Long pedidoId) {
        Pedido pedido = findById(pedidoId);
        return pedido != null && pedido.estaCompletado();
    }

    @Override
    @Transactional(readOnly = true)
    public Double calcularTotalPedido(Long pedidoId) {
        Pedido pedido = findById(pedidoId);
        if (pedido != null) {
            pedido.calcularTotal();
            return pedido.getTotal();
        }
        return 0.0;
    }

    /**
     * Gestión de fechas de entrega
     */
    @Override
    @Transactional
    public Pedido actualizarFechaEntregaEstimada(Long pedidoId, Date fechaEntrega) {
        Pedido pedido = findById(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido no encontrado con ID: " + pedidoId);
        }

        pedido.setFecha_entrega_estimada(fechaEntrega);
        return pedidoDao.save(pedido);
    }

    @Override
    @Transactional
    public Pedido marcarComoEntregado(Long pedidoId) {
        Pedido pedido = findById(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido no encontrado con ID: " + pedidoId);
        }

        pedido.setFecha_entrega_real(new Date());
        if (!"completado".equals(pedido.getEstado())) {
            pedido.setEstado("completado");
        }

        return pedidoDao.save(pedido);
    }

    /**
     * Gestión de notas
     */
    @Override
    @Transactional
    public Pedido actualizarNotasCliente(Long pedidoId, String notas) {
        Pedido pedido = findById(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido no encontrado con ID: " + pedidoId);
        }

        pedido.setNotas_cliente(notas);
        return pedidoDao.save(pedido);
    }

    @Override
    @Transactional
    public Pedido actualizarNotasInternas(Long pedidoId, String notas) {
        Pedido pedido = findById(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido no encontrado con ID: " + pedidoId);
        }
        
        pedido.setNotas_internas(notas);
        return pedidoDao.save(pedido);
    }

    @Override
    @Transactional
    public Pedido crearPedidoDesdeCarrito(Long usuarioId, String metodoPago, String referenciaPago, 
                                        String direccionEnvio, String codigoPostal, String ciudad, 
                                        String provincia, String notasCliente, String estado, 
                                        String notasInternas) {
        
        // Validar que el usuario existe
        Usuario usuario = usuarioDao.findById(usuarioId)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId));
        
        // Obtener elementos del carrito
        List<Carrito> elementosCarrito = carritoDAO.findByUsuarioId(usuarioId);
        if (elementosCarrito.isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío. No se puede crear el pedido.");
        }
        
        // Crear el pedido principal
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setEstado(estado);
        pedido.setMetodo_pago(metodoPago);
        pedido.setReferencia_pago(referenciaPago);
        pedido.setDireccion_envio(direccionEnvio);
        pedido.setCodigo_postal(codigoPostal);
        pedido.setCiudad(ciudad);
        pedido.setProvincia(provincia);
        pedido.setNotas_cliente(notasCliente);
        pedido.setNotas_internas(notasInternas);
        pedido.setFecha_pedido(new Date());
        
        // Generar número de pedido único
        String numeroPedido;
        do {
            numeroPedido = "PED" + System.currentTimeMillis();
        } while (existsByNumeroPedido(numeroPedido));
        pedido.setNumero_pedido(numeroPedido);
        
        // Crear lista de detalles del pedido a partir del carrito
        List<DetallePedido> detallesPedido = new ArrayList<>();
        double subtotal = 0.0;
        
        for (Carrito elementoCarrito : elementosCarrito) {
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setAnuncio(elementoCarrito.getAnuncio());
            detalle.setCantidad(elementoCarrito.getCantidad());
            detalle.setPrecio_unitario(elementoCarrito.getPrecioUnitario());
            detalle.setSubtotal(elementoCarrito.getPrecioTotal());
            
            // Construir especificaciones con los detalles del carrito
            StringBuilder especificaciones = new StringBuilder();
            especificaciones.append("Material: ").append(elementoCarrito.getMaterialSeleccionado());
            if (elementoCarrito.getColorSeleccionado() != null) {
                especificaciones.append(", Color: ").append(elementoCarrito.getColorSeleccionado());
            }
            if (elementoCarrito.getAcabadoPremium()) {
                especificaciones.append(", Acabado Premium: Sí");
            }
            if (elementoCarrito.getUrgente()) {
                especificaciones.append(", Urgente: Sí");
            }
            if (elementoCarrito.getEnvioGratis()) {
                especificaciones.append(", Envío Gratis: Sí");
            }
            
            detalle.setEspecificaciones(especificaciones.toString());
            detalle.setEstado_item("pendiente");
            
            detallesPedido.add(detalle);
            subtotal += elementoCarrito.getPrecioTotal();
        }
        
        // Establecer totales del pedido
        pedido.setSubtotal(subtotal);
        pedido.setTotal(subtotal); // Por ahora sin impuestos ni descuentos adicionales
        pedido.setDetallesPedido(detallesPedido);
        
        // Guardar el pedido (esto guardará también los detalles por cascada)
        Pedido pedidoGuardado = pedidoDao.save(pedido);
        
        // Vaciar el carrito después de crear el pedido exitosamente
        carritoDAO.deleteByUsuarioId(usuarioId);
        
        return pedidoGuardado;
    }

    /**
     * Método auxiliar para validar transiciones de estado
     * Estados permitidos: pendiente, en_proceso, completado, cancelado
     * Transiciones válidas:
     * - pendiente -> en_proceso (cuando se confirma el pago manualmente)
     * - pendiente -> cancelado (cuando se cancela el pago)
     * - en_proceso -> completado (cuando se completa el pedido manualmente)
     * - en_proceso -> cancelado (si se requiere cancelar después del pago)
     */
    private boolean esTransicionValida(String estadoActual, String nuevoEstado) {
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
                return false; // Estados finales, no permiten transiciones
            default:
                return false;
        }
    }
} 