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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto3d.backend.apirest.model.entity.Impresora;
import com.proyecto3d.backend.apirest.model.service.ImpresoraService;


@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class ImpresoraController {

	@Autowired
	private ImpresoraService impresoraService;

	// Obtener impresoras
	@GetMapping("/impresoras")
	public List<Impresora> index() {
		return impresoraService.findAll();
	}
	
	// Obtener impresoras por ID
	@GetMapping("/impresora/{id}")
	public ResponseEntity<?> show(@PathVariable(name = "id") Long id) {
		
		Impresora impresora;
		Map<String, Object> response = new HashMap<>();
		
		try {
			impresora = impresoraService.findById(id);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(impresora == null) {
			response.put("mensaje", "La impresora con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(impresora, HttpStatus.OK); 
	}

	// Crear impresora
	@PostMapping("/impresora")
	public ResponseEntity<?> create(@RequestBody Impresora impresora, BindingResult result) {
		
		Impresora nuevaImpresora;
		Map<String, Object> response = new HashMap<>();
		
		// Validamos campos
		if(result.hasErrors()) {
			List<String> errores = result.getFieldErrors()
				.stream()
				.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
				.collect(Collectors.toList());
			
			response.put("errores", errores);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		// Manejamos errores
		try {
			nuevaImpresora = impresoraService.save(impresora);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al insertar en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La impresora ha sido creada con éxito");
		response.put("impresora", nuevaImpresora);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// Actualizar impresora
	@PutMapping("/impresora/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> update(@RequestBody Impresora impresora, BindingResult result ,@PathVariable(name = "id") Long id) {
		
		Impresora currentImpresora = this.impresoraService.findById(id);
		Impresora nuevaImpresora;
		Map<String, Object> response = new HashMap<>();
		
		// Validamos campos
		if(result.hasErrors()) {
			
			List<String> errores = result.getFieldErrors()
				.stream()
				.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
				.collect(Collectors.toList());
			
			response.put("errores", errores);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(currentImpresora == null) {
			response.put("mensaje", "No se puedo editar, la impresora con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			currentImpresora.setNombre(impresora.getNombre());
			currentImpresora.setModelo(impresora.getModelo());
			
			nuevaImpresora = impresoraService.save(currentImpresora);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al actualizar la impresora en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La impresora ha sido actualizada con éxito");
		response.put("impresora", nuevaImpresora);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// Eliminar impresora por ID
	@DeleteMapping("/impresora/{id}")
	public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
		Impresora currentImpresora = this.impresoraService.findById(id);
		Map<String, Object> response = new HashMap<>();

		// Validación de que exista la impresora
		if (currentImpresora == null) {
			response.put("mensaje", "La impresora con ID: " + id + " no existe en la base de datos");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		try {
			impresoraService.delete(currentImpresora);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar la impresora en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La impresora ha sido eliminada con éxito");
		response.put("impresora", currentImpresora);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
		
	// Eliminar todos los impresoras
	@DeleteMapping("/impresoras")
	public ResponseEntity<?> deleteAll() {
		Map<String, Object> response = new HashMap<>();

		try {
		    impresoraService.deleteAll();
		} catch(DataAccessException e) {
		    response.put("mensaje", "Error al eliminar las impresoras en la base de datos");
		    response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
		    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "Todas las impresoras han sido eliminadas con éxito");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
