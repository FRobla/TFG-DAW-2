package com.proyecto3d.backend.apirest.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
import org.springframework.web.bind.annotation.RestController;

import com.proyecto3d.backend.apirest.model.entity.Impresora;
import com.proyecto3d.backend.apirest.model.service.ImpresoraService;

/**
 * Controlador para la gestión de impresoras
 */
@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class ImpresoraController {

    @Autowired
    private ImpresoraService impresoraService;

    /**
     * Obtiene todas las impresoras
     * @return Lista de impresoras
     */
    @GetMapping("/impresoras")
    public List<Impresora> index() {
        return impresoraService.findAll();
    }

    /**
     * Obtiene una impresora por su ID
     * @param id ID de la impresora
     * @return Respuesta con la impresora o error
     */
    @GetMapping("/impresora/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Optional<Impresora> impresora = null;
        Map<String, Object> response = new HashMap<>();

        try {
            impresora = impresoraService.findById(id);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (!impresora.isPresent()) {
            response.put("mensaje", "La impresora con ID: ".concat(id.toString()).concat(" no existe en la base de datos"));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(impresora.get(), HttpStatus.OK);
    }

    /**
     * Crea una nueva impresora
     * @param impresora Datos de la impresora
     * @return Respuesta con la impresora creada o error
     */
    @PostMapping("/impresora")
    public ResponseEntity<?> create(@RequestBody Impresora impresora) {
        Impresora impresoraNueva = null;
        Map<String, Object> response = new HashMap<>();

        try {
            impresoraNueva = impresoraService.save(impresora);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al insertar en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La impresora ha sido creada con éxito");
        response.put("impresora", impresoraNueva);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Actualiza una impresora existente
     * @param impresora Datos actualizados
     * @param id ID de la impresora
     * @return Respuesta con la impresora actualizada o error
     */
    @PutMapping("/impresora/{id}")
    public ResponseEntity<?> update(@RequestBody Impresora impresora, @PathVariable Long id) {
        Optional<Impresora> impresoraActual = impresoraService.findById(id);
        Impresora impresoraActualizada = null;
        Map<String, Object> response = new HashMap<>();

        if (!impresoraActual.isPresent()) {
            response.put("mensaje", "La impresora con ID: ".concat(id.toString()).concat(" no existe en la base de datos"));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        try {
            Impresora imp = impresoraActual.get();
            imp.setModelo(impresora.getModelo());
            imp.setMarca(impresora.getMarca());
            imp.setTecnologia(impresora.getTecnologia());
            imp.setVolumen_impresion_x(impresora.getVolumen_impresion_x());
            imp.setVolumen_impresion_y(impresora.getVolumen_impresion_y());
            imp.setVolumen_impresion_z(impresora.getVolumen_impresion_z());
            imp.setPrecision_valor(impresora.getPrecision_valor());
            imp.setDescripcion(impresora.getDescripcion());
            imp.setImagen(impresora.getImagen());

            impresoraActualizada = impresoraService.save(imp);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La impresora ha sido actualizada con éxito");
        response.put("impresora", impresoraActualizada);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Elimina una impresora
     * @param id ID de la impresora
     * @return Respuesta con mensaje de éxito o error
     */
    @DeleteMapping("/impresora/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            impresoraService.delete(id);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al eliminar la impresora de la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La impresora ha sido eliminada con éxito");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
} 