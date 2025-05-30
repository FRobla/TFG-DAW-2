package com.proyecto3d.backend.apirest.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto3d.backend.apirest.model.dto.CarritoDTO;
import com.proyecto3d.backend.apirest.model.entity.Carrito;
import com.proyecto3d.backend.apirest.model.service.CarritoService;

/**
 * Controlador REST para la gestión del carrito de compras
 */
@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = "http://localhost:4200")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    /**
     * Añade un elemento al carrito
     * POST /api/carrito/anadir
     */
    @PostMapping("/anadir")
    public ResponseEntity<?> anadirAlCarrito(@RequestBody Map<String, Object> request) {
        try {
            // Validar que los datos requeridos estén presentes
            if (request.get("usuarioId") == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "usuarioId es requerido"));
            }
            if (request.get("anuncioId") == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "anuncioId es requerido"));
            }
            if (request.get("cantidad") == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "cantidad es requerida"));
            }
            if (request.get("materialSeleccionado") == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "materialSeleccionado es requerido"));
            }

            Long usuarioId = Long.valueOf(request.get("usuarioId").toString());
            Long anuncioId = Long.valueOf(request.get("anuncioId").toString());
            Integer cantidad = Integer.valueOf(request.get("cantidad").toString());
            String materialSeleccionado = request.get("materialSeleccionado").toString();
            String colorSeleccionado = request.get("colorSeleccionado") != null ? 
                request.get("colorSeleccionado").toString() : null;
            Boolean acabadoPremium = request.get("acabadoPremium") != null ? 
                Boolean.valueOf(request.get("acabadoPremium").toString()) : false;
            Boolean urgente = request.get("urgente") != null ? 
                Boolean.valueOf(request.get("urgente").toString()) : false;
            Boolean envioGratis = request.get("envioGratis") != null ? 
                Boolean.valueOf(request.get("envioGratis").toString()) : false;

            CarritoDTO carritoElemento = carritoService.anadirAlCarrito(
                usuarioId, anuncioId, cantidad, materialSeleccionado, colorSeleccionado,
                acabadoPremium, urgente, envioGratis);

            return ResponseEntity.ok().body(Map.of(
                "mensaje", "Elemento añadido al carrito exitosamente",
                "elemento", carritoElemento
            ));

        } catch (NumberFormatException e) {
            // Error de formato de números
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Error en el formato de los datos numéricos: " + e.getMessage()));
        } catch (RuntimeException e) {
            // Manejo específico de errores de negocio
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Error al añadir al carrito: " + e.getMessage()));
        } catch (Exception e) {
            // Error genérico
            e.printStackTrace(); // Para debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }
    }

    /**
     * Obtiene el carrito de un usuario
     * GET /api/carrito/usuario/{usuarioId}
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> obtenerCarritoUsuario(@PathVariable Long usuarioId) {
        try {
            List<CarritoDTO> carrito = carritoService.obtenerCarritoUsuario(usuarioId);
            
            return ResponseEntity.ok().body(Map.of(
                "carrito", carrito,
                "totalElementos", carrito.size(),
                "totalPrecio", carritoService.calcularTotalCarrito(usuarioId)
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al obtener el carrito: " + e.getMessage()));
        }
    }

    /**
     * Actualiza la cantidad de un elemento del carrito
     * PUT /api/carrito/{carritoId}/cantidad
     */
    @PutMapping("/{carritoId}/cantidad")
    public ResponseEntity<?> actualizarCantidad(@PathVariable Long carritoId, 
                                               @RequestParam Integer nuevaCantidad) {
        try {
            CarritoDTO carritoActualizado = carritoService.actualizarCantidad(carritoId, nuevaCantidad);
            
            return ResponseEntity.ok().body(Map.of(
                "mensaje", "Cantidad actualizada exitosamente",
                "elemento", carritoActualizado
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Error al actualizar cantidad: " + e.getMessage()));
        }
    }

    /**
     * Actualiza los servicios adicionales de un elemento del carrito
     * PUT /api/carrito/{carritoId}/servicios
     */
    @PutMapping("/{carritoId}/servicios")
    public ResponseEntity<?> actualizarServiciosAdicionales(@PathVariable Long carritoId,
                                                           @RequestBody Map<String, Object> request) {
        try {
            Boolean acabadoPremium = request.get("acabadoPremium") != null ? 
                Boolean.valueOf(request.get("acabadoPremium").toString()) : false;
            Boolean urgente = request.get("urgente") != null ? 
                Boolean.valueOf(request.get("urgente").toString()) : false;
            Boolean envioGratis = request.get("envioGratis") != null ? 
                Boolean.valueOf(request.get("envioGratis").toString()) : false;

            CarritoDTO carritoActualizado = carritoService.actualizarServiciosAdicionales(
                carritoId, acabadoPremium, urgente, envioGratis);

            return ResponseEntity.ok().body(Map.of(
                "mensaje", "Servicios adicionales actualizados exitosamente",
                "elemento", carritoActualizado
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Error al actualizar servicios: " + e.getMessage()));
        }
    }

    /**
     * Elimina un elemento del carrito
     * DELETE /api/carrito/{carritoId}
     */
    @DeleteMapping("/{carritoId}")
    public ResponseEntity<?> eliminarElementoCarrito(@PathVariable Long carritoId) {
        try {
            carritoService.eliminarElementoCarrito(carritoId);
            
            return ResponseEntity.ok().body(Map.of(
                "mensaje", "Elemento eliminado del carrito exitosamente"
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Error al eliminar elemento: " + e.getMessage()));
        }
    }

    /**
     * Vacía todo el carrito de un usuario
     * DELETE /api/carrito/vaciar/{usuarioId}
     */
    @DeleteMapping("/vaciar/{usuarioId}")
    public ResponseEntity<?> vaciarCarrito(@PathVariable Long usuarioId) {
        try {
            carritoService.vaciarCarrito(usuarioId);
            
            return ResponseEntity.ok().body(Map.of(
                "mensaje", "Carrito vaciado exitosamente"
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al vaciar carrito: " + e.getMessage()));
        }
    }

    /**
     * Obtiene el número de elementos en el carrito de un usuario
     * GET /api/carrito/count/{usuarioId}
     */
    @GetMapping("/count/{usuarioId}")
    public ResponseEntity<?> contarElementosCarrito(@PathVariable Long usuarioId) {
        try {
            Long count = carritoService.contarElementosCarrito(usuarioId);
            
            return ResponseEntity.ok().body(Map.of(
                "count", count
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al contar elementos: " + e.getMessage()));
        }
    }

    /**
     * Calcula el precio total del carrito de un usuario
     * GET /api/carrito/total/{usuarioId}
     */
    @GetMapping("/total/{usuarioId}")
    public ResponseEntity<?> calcularTotalCarrito(@PathVariable Long usuarioId) {
        try {
            Double total = carritoService.calcularTotalCarrito(usuarioId);
            
            return ResponseEntity.ok().body(Map.of(
                "total", total
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al calcular total: " + e.getMessage()));
        }
    }
} 