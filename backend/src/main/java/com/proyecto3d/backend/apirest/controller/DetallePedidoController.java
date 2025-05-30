package com.proyecto3d.backend.apirest.controller;

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

import com.proyecto3d.backend.apirest.model.entity.DetallePedido;
import com.proyecto3d.backend.apirest.model.service.DetallePedidoService;

/**
 * Controlador REST para la gestión de detalles de pedidos
 * Proporciona endpoints para todas las operaciones CRUD y funcionalidades específicas
 */
@CrossOrigin(origins = { "http://localhost:4200" }, methods = { RequestMethod.GET, RequestMethod.POST,
        RequestMethod.PUT, RequestMethod.DELETE }, allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class DetallePedidoController {

    @Autowired
    private DetallePedidoService detallePedidoService;

    /**
     * Obtiene todos los detalles de pedidos
     */
    @GetMapping("/detalles-pedido")
    public List<DetallePedido> index() {
        return detallePedidoService.findAll();
    }

    /**
     * Obtiene un detalle de pedido por ID
     */
    @GetMapping("/detalle-pedido/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        DetallePedido detallePedido;
        Map<String, Object> response = new HashMap<>();

        try {
            detallePedido = detallePedidoService.findById(id);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (detallePedido == null) {
            response.put("mensaje", "El detalle de pedido con ID: " + id + " no existe en la base de datos");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(detallePedido, HttpStatus.OK);
    }

    /**
     * Obtiene detalles por pedido
     */
    @GetMapping("/detalles-pedido/pedido/{pedidoId}")
    public ResponseEntity<?> getDetallesByPedido(@PathVariable Long pedidoId) {
        List<DetallePedido> detalles;
        Map<String, Object> response = new HashMap<>();

        try {
            detalles = detallePedidoService.findByPedidoId(pedidoId);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("detalles", detalles);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obtiene detalles por anuncio
     */
    @GetMapping("/detalles-pedido/anuncio/{anuncioId}")
    public ResponseEntity<?> getDetallesByAnuncio(@PathVariable Long anuncioId) {
        List<DetallePedido> detalles;
        Map<String, Object> response = new HashMap<>();

        try {
            detalles = detallePedidoService.findByAnuncioId(anuncioId);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("detalles", detalles);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obtiene detalles por anuncio con paginación
     */
    @GetMapping("/detalles-pedido/anuncio/{anuncioId}/page/{page}/size/{size}")
    public ResponseEntity<?> getDetallesByAnuncioWithPagination(
            @PathVariable Long anuncioId,
            @PathVariable Integer page,
            @PathVariable Integer size) {
        Page<DetallePedido> detalles;
        Map<String, Object> response = new HashMap<>();

        try {
            detalles = detallePedidoService.findByAnuncioIdWithPagination(anuncioId, PageRequest.of(page, size));
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(detalles, HttpStatus.OK);
    }

    /**
     * Obtiene detalles por usuario
     */
    @GetMapping("/detalles-pedido/usuario/{usuarioId}")
    public ResponseEntity<?> getDetallesByUsuario(@PathVariable Long usuarioId) {
        List<DetallePedido> detalles;
        Map<String, Object> response = new HashMap<>();

        try {
            detalles = detallePedidoService.findByUsuarioId(usuarioId);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("detalles", detalles);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obtiene detalles por proveedor
     */
    @GetMapping("/detalles-pedido/proveedor/{proveedorId}")
    public ResponseEntity<?> getDetallesByProveedor(@PathVariable Long proveedorId) {
        List<DetallePedido> detalles;
        Map<String, Object> response = new HashMap<>();

        try {
            detalles = detallePedidoService.findByProveedorId(proveedorId);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("detalles", detalles);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obtiene detalles por proveedor con paginación
     */
    @GetMapping("/detalles-pedido/proveedor/{proveedorId}/page/{page}/size/{size}")
    public ResponseEntity<?> getDetallesByProveedorWithPagination(
            @PathVariable Long proveedorId,
            @PathVariable Integer page,
            @PathVariable Integer size) {
        Page<DetallePedido> detalles;
        Map<String, Object> response = new HashMap<>();

        try {
            detalles = detallePedidoService.findByProveedorIdWithPagination(proveedorId, PageRequest.of(page, size));
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(detalles, HttpStatus.OK);
    }

    /**
     * Obtiene detalles por estado del item
     */
    @GetMapping("/detalles-pedido/estado/{estadoItem}")
    public ResponseEntity<?> getDetallesByEstadoItem(@PathVariable String estadoItem) {
        List<DetallePedido> detalles;
        Map<String, Object> response = new HashMap<>();

        try {
            detalles = detallePedidoService.findByEstadoItem(estadoItem);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("detalles", detalles);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obtiene detalles por estado del item con paginación
     */
    @GetMapping("/detalles-pedido/estado/{estadoItem}/page/{page}/size/{size}")
    public ResponseEntity<?> getDetallesByEstadoItemWithPagination(
            @PathVariable String estadoItem,
            @PathVariable Integer page,
            @PathVariable Integer size) {
        Page<DetallePedido> detalles;
        Map<String, Object> response = new HashMap<>();

        try {
            detalles = detallePedidoService.findByEstadoItemWithPagination(estadoItem, PageRequest.of(page, size));
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(detalles, HttpStatus.OK);
    }

    /**
     * Busca detalles con filtros combinados
     */
    @GetMapping("/detalles-pedido/filtros")
    public ResponseEntity<?> getDetallesWithFilters(
            @RequestParam(required = false) Long pedidoId,
            @RequestParam(required = false) Long anuncioId,
            @RequestParam(required = false) Long materialId,
            @RequestParam(required = false) String estadoItem,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        
        Page<DetallePedido> detalles;
        Map<String, Object> response = new HashMap<>();
        
        try {
            detalles = detallePedidoService.findWithFilters(pedidoId, anuncioId, materialId, estadoItem, PageRequest.of(page, size));
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(detalles, HttpStatus.OK);
    }

    /**
     * Obtiene detalles pendientes por proveedor
     */
    @GetMapping("/detalles-pedido/proveedor/{proveedorId}/pendientes")
    public ResponseEntity<?> getDetallesPendientesByProveedor(@PathVariable Long proveedorId) {
        List<DetallePedido> detalles;
        Map<String, Object> response = new HashMap<>();

        try {
            detalles = detallePedidoService.findDetallesPendientesByProveedor(proveedorId);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("detalles", detalles);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Crea un nuevo detalle de pedido
     */
    @PostMapping("/detalle-pedido")
    public ResponseEntity<?> create(@RequestBody DetallePedido detallePedido, BindingResult result) {
        DetallePedido nuevoDetalle;
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            List<String> errores = result.getFieldErrors().stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errores", errores);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            nuevoDetalle = detallePedidoService.crearDetallePedido(detallePedido);
        } catch (IllegalArgumentException e) {
            response.put("mensaje", "Error de validación");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al insertar en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El detalle de pedido ha sido creado con éxito");
        response.put("detalle", nuevoDetalle);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Actualiza un detalle de pedido existente
     */
    @PutMapping("/detalle-pedido/{id}")
    public ResponseEntity<?> update(@RequestBody DetallePedido detallePedido, BindingResult result, @PathVariable Long id) {
        DetallePedido currentDetalle = detallePedidoService.findById(id);
        DetallePedido detalleActualizado;
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            List<String> errores = result.getFieldErrors().stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errores", errores);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (currentDetalle == null) {
            response.put("mensaje", "No se pudo editar, el detalle de pedido con ID: " + id + " no existe en la base de datos");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        try {
            // Solo permitir actualizar ciertos campos
            if (detallePedido.getCantidad() != null && detallePedido.getCantidad() > 0) {
                currentDetalle.setCantidad(detallePedido.getCantidad());
            }
            if (detallePedido.getEspecificaciones() != null) {
                currentDetalle.setEspecificaciones(detallePedido.getEspecificaciones());
            }
            if (detallePedido.getNotas() != null) {
                currentDetalle.setNotas(detallePedido.getNotas());
            }
            if (detallePedido.getMaterial() != null) {
                currentDetalle.setMaterial(detallePedido.getMaterial());
            }

            // Recalcular subtotal
            currentDetalle.calcularSubtotal();
            
            detalleActualizado = detallePedidoService.save(currentDetalle);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El detalle de pedido ha sido actualizado con éxito");
        response.put("detalle", detalleActualizado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Actualiza el estado de un detalle de pedido
     */
    @PutMapping("/detalle-pedido/{id}/estado/{estado}")
    public ResponseEntity<?> updateEstadoItem(@PathVariable Long id, @PathVariable String estado) {
        DetallePedido detalleActualizado;
        Map<String, Object> response = new HashMap<>();

        try {
            detalleActualizado = detallePedidoService.actualizarEstadoItem(id, estado);
        } catch (IllegalArgumentException e) {
            response.put("mensaje", "Error de validación");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El estado del detalle ha sido actualizado con éxito");
        response.put("detalle", detalleActualizado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Actualiza la cantidad de un detalle de pedido
     */
    @PutMapping("/detalle-pedido/{id}/cantidad/{cantidad}")
    public ResponseEntity<?> updateCantidad(@PathVariable Long id, @PathVariable Integer cantidad) {
        DetallePedido detalleActualizado;
        Map<String, Object> response = new HashMap<>();

        try {
            detalleActualizado = detallePedidoService.actualizarCantidad(id, cantidad);
        } catch (IllegalArgumentException e) {
            response.put("mensaje", "Error de validación");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La cantidad del detalle ha sido actualizada con éxito");
        response.put("detalle", detalleActualizado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Actualiza las especificaciones de un detalle de pedido
     */
    @PutMapping("/detalle-pedido/{id}/especificaciones")
    public ResponseEntity<?> updateEspecificaciones(@PathVariable Long id, @RequestBody Map<String, String> especInfo) {
        DetallePedido detalleActualizado;
        Map<String, Object> response = new HashMap<>();

        String especificaciones = especInfo.get("especificaciones");

        try {
            detalleActualizado = detallePedidoService.actualizarEspecificaciones(id, especificaciones);
        } catch (IllegalArgumentException e) {
            response.put("mensaje", "Error de validación");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "Las especificaciones del detalle han sido actualizadas con éxito");
        response.put("detalle", detalleActualizado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Actualiza las notas de un detalle de pedido
     */
    @PutMapping("/detalle-pedido/{id}/notas")
    public ResponseEntity<?> updateNotas(@PathVariable Long id, @RequestBody Map<String, String> notasInfo) {
        DetallePedido detalleActualizado;
        Map<String, Object> response = new HashMap<>();

        String notas = notasInfo.get("notas");

        try {
            detalleActualizado = detallePedidoService.actualizarNotas(id, notas);
        } catch (IllegalArgumentException e) {
            response.put("mensaje", "Error de validación");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "Las notas del detalle han sido actualizadas con éxito");
        response.put("detalle", detalleActualizado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Marca un detalle como completado
     */
    @PutMapping("/detalle-pedido/{id}/completar")
    public ResponseEntity<?> marcarComoCompletado(@PathVariable Long id) {
        DetallePedido detalleActualizado;
        Map<String, Object> response = new HashMap<>();

        try {
            detalleActualizado = detallePedidoService.marcarItemComoCompletado(id);
        } catch (IllegalArgumentException e) {
            response.put("mensaje", "Error de validación");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El detalle ha sido marcado como completado con éxito");
        response.put("detalle", detalleActualizado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Marca múltiples detalles como completados
     */
    @PutMapping("/detalles-pedido/completar-multiple")
    public ResponseEntity<?> marcarMultiplesComoCompletados(@RequestBody Map<String, List<Long>> idsInfo) {
        List<DetallePedido> detallesActualizados;
        Map<String, Object> response = new HashMap<>();

        List<Long> detalleIds = idsInfo.get("detalleIds");

        if (detalleIds == null || detalleIds.isEmpty()) {
            response.put("mensaje", "La lista de IDs de detalles es requerida");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            detallesActualizados = detallePedidoService.marcarItemsComoCompletados(detalleIds);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "Los detalles han sido marcados como completados con éxito");
        response.put("detalles", detallesActualizados);
        response.put("total", detallesActualizados.size());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Cambia el material de un detalle de pedido
     */
    @PutMapping("/detalle-pedido/{id}/material/{materialId}")
    public ResponseEntity<?> cambiarMaterial(@PathVariable Long id, @PathVariable Long materialId) {
        DetallePedido detalleActualizado;
        Map<String, Object> response = new HashMap<>();

        try {
            detalleActualizado = detallePedidoService.cambiarMaterial(id, materialId);
        } catch (IllegalArgumentException e) {
            response.put("mensaje", "Error de validación");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El material del detalle ha sido cambiado con éxito");
        response.put("detalle", detalleActualizado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Elimina un detalle de pedido
     */
    @DeleteMapping("/detalle-pedido/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            DetallePedido detalle = detallePedidoService.findById(id);
            if (detalle == null) {
                response.put("mensaje", "El detalle de pedido con ID: " + id + " no existe en la base de datos");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            // Solo permitir eliminar detalles pendientes
            if (!"pendiente".equals(detalle.getEstado_item())) {
                response.put("mensaje", "Solo se pueden eliminar detalles pendientes");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            detallePedidoService.delete(detalle);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al eliminar de la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El detalle de pedido ha sido eliminado con éxito");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obtiene estadísticas: productos más vendidos
     */
    @GetMapping("/detalles-pedido/estadisticas/productos-mas-vendidos")
    public ResponseEntity<?> getProductosMasVendidos() {
        List<Object[]> productos;
        Map<String, Object> response = new HashMap<>();

        try {
            productos = detallePedidoService.getProductosMasVendidos();
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("productos", productos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obtiene estadísticas: materiales más utilizados
     */
    @GetMapping("/detalles-pedido/estadisticas/materiales-mas-utilizados")
    public ResponseEntity<?> getMaterialesMasUtilizados() {
        List<Object[]> materiales;
        Map<String, Object> response = new HashMap<>();

        try {
            materiales = detallePedidoService.getMaterialesMasUtilizados();
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("materiales", materiales);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obtiene estadísticas: ingresos por proveedor
     */
    @GetMapping("/detalles-pedido/estadisticas/ingresos-por-proveedor")
    public ResponseEntity<?> getIngresosPorProveedor() {
        List<Object[]> ingresos;
        Map<String, Object> response = new HashMap<>();

        try {
            ingresos = detallePedidoService.getIngresosPorProveedor();
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("ingresos", ingresos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obtiene el total vendido por anuncio
     */
    @GetMapping("/detalles-pedido/anuncio/{anuncioId}/total-vendido")
    public ResponseEntity<?> getTotalVendidoPorAnuncio(@PathVariable Long anuncioId) {
        Double totalVendido;
        Map<String, Object> response = new HashMap<>();

        try {
            totalVendido = detallePedidoService.getTotalVendidoPorAnuncio(anuncioId);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("totalVendido", totalVendido);
        response.put("anuncioId", anuncioId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Verifica si un pedido tiene detalles pendientes
     */
    @GetMapping("/detalles-pedido/pedido/{pedidoId}/tiene-pendientes")
    public ResponseEntity<?> tieneDetallesPendientes(@PathVariable Long pedidoId) {
        boolean tienePendientes;
        Map<String, Object> response = new HashMap<>();

        try {
            tienePendientes = detallePedidoService.tieneDetallesPendientes(pedidoId);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("tienePendientes", tienePendientes);
        response.put("pedidoId", pedidoId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Verifica si todos los detalles de un pedido están completados
     */
    @GetMapping("/detalles-pedido/pedido/{pedidoId}/todos-completados")
    public ResponseEntity<?> todosLosDetallesCompletados(@PathVariable Long pedidoId) {
        boolean todosCompletados;
        Map<String, Object> response = new HashMap<>();

        try {
            todosCompletados = detallePedidoService.todosLosDetallesCompletados(pedidoId);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("todosCompletados", todosCompletados);
        response.put("pedidoId", pedidoId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
} 