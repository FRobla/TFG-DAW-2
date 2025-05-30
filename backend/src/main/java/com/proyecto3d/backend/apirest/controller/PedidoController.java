package com.proyecto3d.backend.apirest.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto3d.backend.apirest.model.entity.Pedido;
import com.proyecto3d.backend.apirest.model.service.PedidoService;

/**
 * Controlador REST para la gestión de pedidos
 * Proporciona endpoints para todas las operaciones CRUD y funcionalidades específicas
 */
@CrossOrigin(origins = { "http://localhost:4200" }, methods = { RequestMethod.GET, RequestMethod.POST,
        RequestMethod.PUT, RequestMethod.DELETE }, allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    /**
     * Obtiene todos los pedidos
     */
    @GetMapping("/pedidos")
    public List<Pedido> index() {
        return pedidoService.findAll();
    }

    /**
     * Obtiene pedidos con paginación
     */
    @GetMapping("/pedidos/page/{page}")
    public Page<Pedido> index(@PathVariable Integer page) {
        return pedidoService.findByEstadoWithPagination(null, PageRequest.of(page, 10));
    }

    /**
     * Obtiene pedidos con paginación personalizada
     */
    @GetMapping("/pedidos/page/{page}/size/{size}")
    public Page<Pedido> index(@PathVariable Integer page, @PathVariable Integer size) {
        return pedidoService.findByEstadoWithPagination(null, PageRequest.of(page, size));
    }

    /**
     * Obtiene un pedido por ID
     */
    @GetMapping("/pedido/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Pedido pedido;
        Map<String, Object> response = new HashMap<>();

        try {
            pedido = pedidoService.findById(id);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (pedido == null) {
            response.put("mensaje", "El pedido con ID: " + id + " no existe en la base de datos");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(pedido, HttpStatus.OK);
    }

    /**
     * Busca un pedido por número de pedido
     */
    @GetMapping("/pedido/numero/{numeroPedido}")
    public ResponseEntity<?> showByNumeroPedido(@PathVariable String numeroPedido) {
        Pedido pedido;
        Map<String, Object> response = new HashMap<>();

        try {
            pedido = pedidoService.findByNumeroPedido(numeroPedido);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (pedido == null) {
            response.put("mensaje", "El pedido con número: " + numeroPedido + " no existe en la base de datos");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(pedido, HttpStatus.OK);
    }

    /**
     * Obtiene pedidos por usuario
     */
    @GetMapping("/pedidos/usuario/{usuarioId}")
    public ResponseEntity<?> getPedidosByUsuario(@PathVariable Long usuarioId) {
        List<Pedido> pedidos;
        Map<String, Object> response = new HashMap<>();

        try {
            pedidos = pedidoService.findByUsuarioId(usuarioId);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("pedidos", pedidos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obtiene pedidos por usuario con paginación
     */
    @GetMapping("/pedidos/usuario/{usuarioId}/page/{page}/size/{size}")
    public ResponseEntity<?> getPedidosByUsuarioWithPagination(
            @PathVariable Long usuarioId,
            @PathVariable Integer page,
            @PathVariable Integer size) {
        Page<Pedido> pedidos;
        Map<String, Object> response = new HashMap<>();

        try {
            pedidos = pedidoService.findByUsuarioIdWithPagination(usuarioId, PageRequest.of(page, size));
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    /**
     * Obtiene pedidos por estado
     */
    @GetMapping("/pedidos/estado/{estado}")
    public ResponseEntity<?> getPedidosByEstado(@PathVariable String estado) {
        List<Pedido> pedidos;
        Map<String, Object> response = new HashMap<>();

        try {
            pedidos = pedidoService.findByEstado(estado);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("pedidos", pedidos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obtiene pedidos por estado con paginación
     */
    @GetMapping("/pedidos/estado/{estado}/page/{page}/size/{size}")
    public ResponseEntity<?> getPedidosByEstadoWithPagination(
            @PathVariable String estado,
            @PathVariable Integer page,
            @PathVariable Integer size) {
        Page<Pedido> pedidos;
        Map<String, Object> response = new HashMap<>();

        try {
            pedidos = pedidoService.findByEstadoWithPagination(estado, PageRequest.of(page, size));
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    /**
     * Busca pedidos con filtros combinados
     */
    @GetMapping("/pedidos/filtros")
    public ResponseEntity<?> getPedidosWithFilters(
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        
        Page<Pedido> pedidos;
        Map<String, Object> response = new HashMap<>();
        
        try {
            Date fechaInicioDate = null;
            Date fechaFinDate = null;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            
            if (fechaInicio != null && !fechaInicio.isEmpty()) {
                fechaInicioDate = formatter.parse(fechaInicio);
            }
            if (fechaFin != null && !fechaFin.isEmpty()) {
                fechaFinDate = formatter.parse(fechaFin);
            }
            
            pedidos = pedidoService.findWithFilters(usuarioId, estado, fechaInicioDate, fechaFinDate, PageRequest.of(page, size));
        } catch (ParseException e) {
            response.put("mensaje", "Formato de fecha inválido. Use yyyy-MM-dd");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    /**
     * Crea un nuevo pedido
     */
    @PostMapping("/pedido")
    public ResponseEntity<?> create(@RequestBody Pedido pedido, BindingResult result) {
        Pedido nuevoPedido;
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            List<String> errores = result.getFieldErrors().stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errores", errores);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            nuevoPedido = pedidoService.crearPedido(pedido);
        } catch (IllegalArgumentException e) {
            response.put("mensaje", "Error de validación");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al insertar en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El pedido ha sido creado con éxito");
        response.put("pedido", nuevoPedido);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Actualiza un pedido existente
     */
    @PutMapping("/pedido/{id}")
    public ResponseEntity<?> update(@RequestBody Pedido pedido, BindingResult result, @PathVariable Long id) {
        Pedido currentPedido = pedidoService.findById(id);
        Pedido pedidoActualizado;
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            List<String> errores = result.getFieldErrors().stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errores", errores);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (currentPedido == null) {
            response.put("mensaje", "No se pudo editar, el pedido con ID: " + id + " no existe en la base de datos");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        try {
            // Actualizar solo campos permitidos
            if (pedido.getDireccion_envio() != null && !pedido.getDireccion_envio().isEmpty()) {
                currentPedido.setDireccion_envio(pedido.getDireccion_envio());
            }
            if (pedido.getCodigo_postal() != null) {
                currentPedido.setCodigo_postal(pedido.getCodigo_postal());
            }
            if (pedido.getCiudad() != null && !pedido.getCiudad().isEmpty()) {
                currentPedido.setCiudad(pedido.getCiudad());
            }
            if (pedido.getProvincia() != null && !pedido.getProvincia().isEmpty()) {
                currentPedido.setProvincia(pedido.getProvincia());
            }
            if (pedido.getNotas_cliente() != null) {
                currentPedido.setNotas_cliente(pedido.getNotas_cliente());
            }
            if (pedido.getFecha_entrega_estimada() != null) {
                currentPedido.setFecha_entrega_estimada(pedido.getFecha_entrega_estimada());
            }

            pedidoActualizado = pedidoService.save(currentPedido);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El pedido ha sido actualizado con éxito");
        response.put("pedido", pedidoActualizado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Actualiza el estado de un pedido
     */
    @PutMapping("/pedido/{id}/estado/{estado}")
    public ResponseEntity<?> updateEstado(@PathVariable Long id, @PathVariable String estado) {
        Pedido pedidoActualizado;
        Map<String, Object> response = new HashMap<>();

        try {
            pedidoActualizado = pedidoService.actualizarEstado(id, estado);
        } catch (IllegalArgumentException e) {
            response.put("mensaje", "Error de validación");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El estado del pedido ha sido actualizado con éxito");
        response.put("pedido", pedidoActualizado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Procesa el pago de un pedido
     */
    @PutMapping("/pedido/{id}/pago")
    public ResponseEntity<?> procesarPago(@PathVariable Long id, @RequestBody Map<String, String> pagoInfo) {
        Pedido pedidoActualizado;
        Map<String, Object> response = new HashMap<>();

        String metodoPago = pagoInfo.get("metodoPago");
        String referenciaPago = pagoInfo.get("referenciaPago");

        if (metodoPago == null || metodoPago.isEmpty()) {
            response.put("mensaje", "El método de pago es requerido");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            pedidoActualizado = pedidoService.procesarPago(id, metodoPago, referenciaPago);
        } catch (IllegalArgumentException e) {
            response.put("mensaje", "Error de validación");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El pago ha sido procesado con éxito");
        response.put("pedido", pedidoActualizado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Completa un pedido
     */
    @PutMapping("/pedido/{id}/completar")
    public ResponseEntity<?> completarPedido(@PathVariable Long id) {
        Pedido pedidoActualizado;
        Map<String, Object> response = new HashMap<>();

        try {
            pedidoActualizado = pedidoService.completarPedido(id);
        } catch (IllegalArgumentException e) {
            response.put("mensaje", "Error de validación");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El pedido ha sido completado con éxito");
        response.put("pedido", pedidoActualizado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Cancela un pedido
     */
    @PutMapping("/pedido/{id}/cancelar")
    public ResponseEntity<?> cancelarPedido(@PathVariable Long id, @RequestBody(required = false) Map<String, String> cancelInfo) {
        Pedido pedidoActualizado;
        Map<String, Object> response = new HashMap<>();

        String motivo = cancelInfo != null ? cancelInfo.get("motivo") : null;

        try {
            pedidoActualizado = pedidoService.cancelarPedido(id, motivo);
        } catch (IllegalArgumentException e) {
            response.put("mensaje", "Error de validación");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El pedido ha sido cancelado con éxito");
        response.put("pedido", pedidoActualizado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Elimina un pedido
     */
    @DeleteMapping("/pedido/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Pedido pedido = pedidoService.findById(id);
            if (pedido == null) {
                response.put("mensaje", "El pedido con ID: " + id + " no existe en la base de datos");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            // Solo permitir eliminar pedidos cancelados o pendientes
            if (!"cancelado".equals(pedido.getEstado()) && !"pendiente".equals(pedido.getEstado())) {
                response.put("mensaje", "Solo se pueden eliminar pedidos cancelados o pendientes");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            pedidoService.delete(pedido);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al eliminar de la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El pedido ha sido eliminado con éxito");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obtiene estadísticas de pedidos por estado
     */
    @GetMapping("/pedidos/estadisticas/estados")
    public ResponseEntity<?> getEstadisticasPorEstado() {
        List<Object[]> estadisticas;
        Map<String, Object> response = new HashMap<>();

        try {
            estadisticas = pedidoService.getEstadisticasPorEstado();
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("estadisticas", estadisticas);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obtiene estadísticas de ventas por mes
     */
    @GetMapping("/pedidos/estadisticas/ventas-mes")
    public ResponseEntity<?> getVentasPorMes() {
        List<Object[]> ventas;
        Map<String, Object> response = new HashMap<>();

        try {
            ventas = pedidoService.getVentasPorMes();
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("ventas", ventas);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obtiene pedidos pendientes
     */
    @GetMapping("/pedidos/pendientes")
    public ResponseEntity<?> getPedidosPendientes() {
        List<Pedido> pedidos;
        Map<String, Object> response = new HashMap<>();

        try {
            pedidos = pedidoService.findPedidosPendientes();
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("pedidos", pedidos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Actualiza notas del cliente
     */
    @PutMapping("/pedido/{id}/notas-cliente")
    public ResponseEntity<?> updateNotasCliente(@PathVariable Long id, @RequestBody Map<String, String> notasInfo) {
        Pedido pedidoActualizado;
        Map<String, Object> response = new HashMap<>();

        String notas = notasInfo.get("notas");

        try {
            pedidoActualizado = pedidoService.actualizarNotasCliente(id, notas);
        } catch (IllegalArgumentException e) {
            response.put("mensaje", "Error de validación");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "Las notas del cliente han sido actualizadas con éxito");
        response.put("pedido", pedidoActualizado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Actualiza notas internas (solo admin)
     */
    @PutMapping("/pedido/{id}/notas-internas")
    public ResponseEntity<?> updateNotasInternas(@PathVariable Long id, @RequestBody Map<String, String> notasInfo) {
        Pedido pedidoActualizado;
        Map<String, Object> response = new HashMap<>();

        String notas = notasInfo.get("notas");

        try {
            pedidoActualizado = pedidoService.actualizarNotasInternas(id, notas);
        } catch (IllegalArgumentException e) {
            response.put("mensaje", "Error de validación");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "Las notas internas han sido actualizadas con éxito");
        response.put("pedido", pedidoActualizado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Exporta pedidos a CSV
     */
    @GetMapping("/pedidos/export")
    public ResponseEntity<String> exportarPedidos() {
        try {
            String csvContent = pedidoService.exportarPedidosCSV();
            return ResponseEntity.ok()
                    .header("Content-Type", "text/csv; charset=UTF-8")
                    .header("Content-Disposition", "attachment; filename=\"pedidos.csv\"")
                    .body(csvContent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al exportar pedidos: " + e.getMessage());
        }
    }
} 