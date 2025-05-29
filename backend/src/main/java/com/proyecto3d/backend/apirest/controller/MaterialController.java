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

import com.proyecto3d.backend.apirest.model.entity.Material;
import com.proyecto3d.backend.apirest.model.service.MaterialService;

/**
 * Controlador REST para la gestión de materiales de impresión 3D
 */
@CrossOrigin(origins = { "http://localhost:4200" }, methods = { RequestMethod.GET, RequestMethod.POST,
		RequestMethod.PUT, RequestMethod.DELETE }, allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class MaterialController {

	@Autowired
	private MaterialService materialService;

	// Obtener todos los materiales
	@GetMapping("/materiales")
	public List<Material> index() {
		return materialService.findAll();
	}

	// Obtener todos los materiales activos
	@GetMapping("/materiales/activos")
	public List<Material> getActiveMaterials() {
		return materialService.findByActivoTrue();
	}

	// Buscar materiales por término
	@GetMapping("/materiales/buscar")
	public List<Material> searchMaterials(@RequestParam String term) {
		return materialService.findByTerm(term);
	}

	// Obtener material por ID
	@GetMapping("/material/{id}")
	public ResponseEntity<?> show(@PathVariable(name = "id") Long id) {
		Material material;
		Map<String, Object> response = new HashMap<>();

		try {
			material = materialService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (material == null) {
			response.put("mensaje", "El material con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(material, HttpStatus.OK);
	}

	// Obtener conteos de todos los materiales con anuncios
	@GetMapping("/materiales/conteos")
	public ResponseEntity<?> getMaterialesConConteos() {
		Map<String, Object> response = new HashMap<>();
		
		try {
			List<Map<String, Object>> materialesConConteos = materialService.getMaterialesConConteoAnuncios();
			response.put("materiales", materialesConConteos);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al obtener conteos de materiales");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Crear material
	@PostMapping("/material")
	public ResponseEntity<?> create(@RequestBody Material material, BindingResult result) {
		Material nuevoMaterial;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errores = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errores", errores);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			nuevoMaterial = materialService.save(material);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El material ha sido creado con éxito");
		response.put("material", nuevoMaterial);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// Actualizar material
	@PutMapping("/material/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> update(@RequestBody Material material, BindingResult result,
			@PathVariable(name = "id") Long id) {

		Material currentMaterial = materialService.findById(id);
		Material nuevoMaterial;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errores = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errores", errores);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		if (currentMaterial == null) {
			response.put("mensaje", "No se pudo editar, el material con ID: "
					.concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		try {
			currentMaterial.setNombre(material.getNombre());
			currentMaterial.setDescripcion(material.getDescripcion());
			currentMaterial.setPropiedades(material.getPropiedades());
			currentMaterial.setColor_disponibles(material.getColor_disponibles());
			currentMaterial.setPrecio_kg(material.getPrecio_kg());
			currentMaterial.setActivo(material.getActivo());

			nuevoMaterial = materialService.save(currentMaterial);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El material ha sido actualizado con éxito");
		response.put("material", nuevoMaterial);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// Eliminar material
	@DeleteMapping("/material/{id}")
	public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
		Map<String, Object> response = new HashMap<>();

		try {
			Material material = materialService.findById(id);
			if (material != null) {
				materialService.delete(material);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el material de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El material ha sido eliminado con éxito");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Eliminar todos los materiales
	@DeleteMapping("/materiales")
	public ResponseEntity<?> deleteAll() {
		Map<String, Object> response = new HashMap<>();

		try {
			materialService.deleteAll();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar los materiales de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "Todos los materiales han sido eliminados con éxito");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
} 