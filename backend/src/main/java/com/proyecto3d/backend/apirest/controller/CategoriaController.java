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

import com.proyecto3d.backend.apirest.model.entity.Categoria;
import com.proyecto3d.backend.apirest.model.service.CategoriaService;



@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class CategoriaController {

	@Autowired
	private CategoriaService categoriaService;

	// Obtener categorias
	@GetMapping("/categorias")
	public List<Categoria> index() {
		return categoriaService.findAll();
	}

	// Obtener categorias por ID
	@GetMapping("/categoria/{id}")
	public ResponseEntity<?> show(@PathVariable(name = "id") Long id) {

		Categoria categoria;
		Map<String, Object> response = new HashMap<>();

		try {
			categoria = categoriaService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (categoria == null) {
			response.put("mensaje", "El categoria con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(categoria, HttpStatus.OK);
	}

	// Crear categoria
	@PostMapping("/categoria")
	public ResponseEntity<?> create(@RequestBody Categoria categoria, BindingResult result) {

		Categoria nuevoCategoria;
		Map<String, Object> response = new HashMap<>();

		// Validamos campos
		if (result.hasErrors()) {
			List<String> errores = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errores", errores);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		// Manejamos errores
		try {
			nuevoCategoria = categoriaService.save(categoria);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El categoria ha sido creado con éxito");
		response.put("autor", nuevoCategoria);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// Actualizar categoria
	@PutMapping("/categoria/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> update(@RequestBody Categoria categoria, BindingResult result, @PathVariable(name = "id") Long id) {

		Categoria currentCategoria = this.categoriaService.findById(id);
		Categoria nuevoCategoria;
		Map<String, Object> response = new HashMap<>();

		// Validamos campos
		if (result.hasErrors()) {

			List<String> errores = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errores", errores);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		if (currentCategoria == null) {
			response.put("mensaje", "No se puedo editar, el categoria con ID: "
					.concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		try {
			currentCategoria.setDescripcion(categoria.getDescripcion());
			currentCategoria.setNombre(categoria.getNombre());

			nuevoCategoria = categoriaService.save(currentCategoria);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el categoria en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El categoria ha sido actualizado con éxito");
		response.put("cliente", nuevoCategoria);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// Eliminar categoria por ID
	@DeleteMapping("/categorias/{id}")
	public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
		Categoria currentCategoria = this.categoriaService.findById(id);
		Map<String, Object> response = new HashMap<>();

		// Validación de que exista el categoria
		if (currentCategoria == null) {
			response.put("mensaje", "El categoria con ID: " + id + " no existe en la base de datos");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		try {
			categoriaService.delete(currentCategoria);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el categoria en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El categoria ha sido eliminado con éxito");
		response.put("cliente", currentCategoria);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Obtener todas las categorías con conteo de anuncios
	 */
	@GetMapping("/categorias/con-conteo")
	public ResponseEntity<List<Map<String, Object>>> getCategoriasConConteo() {
		try {
			List<Map<String, Object>> categoriasConConteo = categoriaService.getCategoriasConConteoAnuncios();
			return ResponseEntity.ok(categoriasConConteo);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
