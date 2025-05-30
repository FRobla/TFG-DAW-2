package com.proyecto3d.backend.apirest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto3d.backend.apirest.model.entity.Ubicacion;
import com.proyecto3d.backend.apirest.model.service.UbicacionService;

/**
 * Controlador REST para la gestión de ubicaciones geográficas
 */
@CrossOrigin(origins = { "http://localhost:4200" }, methods = { RequestMethod.GET, RequestMethod.POST,
		RequestMethod.PUT, RequestMethod.DELETE }, allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class UbicacionController {

	@Autowired
	private UbicacionService ubicacionService;

	// Obtener todas las ubicaciones
	@GetMapping("/ubicaciones")
	public List<Ubicacion> index() {
		return ubicacionService.findAll();
	}

	// Obtener todas las ubicaciones activas
	@GetMapping("/ubicaciones/activas")
	public List<Ubicacion> getActiveUbicaciones() {
		return ubicacionService.findByActivoTrue();
	}

	// Obtener ubicaciones donde hay usuarios con anuncios publicados
	@GetMapping("/ubicaciones/con-anuncios")
	public List<Ubicacion> getUbicacionesConAnuncios() {
		return ubicacionService.findUbicacionesConAnuncios();
	}

	// Buscar ubicaciones por término
	@GetMapping("/ubicaciones/buscar")
	public List<Ubicacion> searchUbicaciones(@RequestParam String term) {
		return ubicacionService.findByTerm(term);
	}

	// Obtener ubicacion por ID
	@GetMapping("/ubicacion/{id}")
	public ResponseEntity<?> show(@PathVariable(name = "id") Long id) {
		Ubicacion ubicacion;
		Map<String, Object> response = new HashMap<>();

		try {
			ubicacion = ubicacionService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (ubicacion == null) {
			response.put("mensaje", "La ubicación con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(ubicacion, HttpStatus.OK);
	}

	// Obtener conteos de todas las ubicaciones con anuncios
	@GetMapping("/ubicaciones/conteos")
	public ResponseEntity<?> getUbicacionesConConteos() {
		Map<String, Object> response = new HashMap<>();
		
		try {
			List<Map<String, Object>> ubicacionesConConteos = ubicacionService.getUbicacionesConConteoAnuncios();
			response.put("ubicaciones", ubicacionesConConteos);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al obtener conteos de ubicaciones");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Crear ubicacion
	@PostMapping("/ubicacion")
	public ResponseEntity<?> create(@RequestBody Ubicacion ubicacion, BindingResult result) {
		Ubicacion nuevaUbicacion;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errores = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errores", errores);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			nuevaUbicacion = ubicacionService.save(ubicacion);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La ubicación ha sido creada con éxito");
		response.put("ubicacion", nuevaUbicacion);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// Actualizar ubicacion
	@PutMapping("/ubicacion/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> update(@RequestBody Ubicacion ubicacion, BindingResult result,
			@PathVariable(name = "id") Long id) {

		Ubicacion currentUbicacion = ubicacionService.findById(id);
		Ubicacion nuevaUbicacion;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errores = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errores", errores);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		if (currentUbicacion == null) {
			response.put("mensaje", "No se pudo editar, la ubicación con ID: "
					.concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		try {
			currentUbicacion.setNombre(ubicacion.getNombre());
			currentUbicacion.setProvincia(ubicacion.getProvincia());
			currentUbicacion.setComunidad_autonoma(ubicacion.getComunidad_autonoma());
			currentUbicacion.setCodigo_postal(ubicacion.getCodigo_postal());
			currentUbicacion.setActivo(ubicacion.getActivo());

			nuevaUbicacion = ubicacionService.save(currentUbicacion);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La ubicación ha sido actualizada con éxito");
		response.put("ubicacion", nuevaUbicacion);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// Eliminar ubicacion
	@DeleteMapping("/ubicacion/{id}")
	public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
		Map<String, Object> response = new HashMap<>();

		try {
			Ubicacion ubicacion = ubicacionService.findById(id);
			if (ubicacion != null) {
				ubicacionService.delete(ubicacion);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar la ubicación de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La ubicación ha sido eliminada con éxito");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Eliminar todas las ubicaciones
	@DeleteMapping("/ubicaciones")
	public ResponseEntity<?> deleteAll() {
		Map<String, Object> response = new HashMap<>();

		try {
			ubicacionService.deleteAll();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar las ubicaciones de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "Todas las ubicaciones han sido eliminadas con éxito");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
} 