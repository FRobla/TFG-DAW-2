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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto3d.backend.apirest.model.entity.Anuncio;
import com.proyecto3d.backend.apirest.model.entity.Categoria;
import com.proyecto3d.backend.apirest.model.service.AnuncioService;
import com.proyecto3d.backend.apirest.model.service.CategoriaService;

@CrossOrigin(origins = { "http://localhost:4200" }, methods = { RequestMethod.GET, RequestMethod.POST,
		RequestMethod.PUT, RequestMethod.DELETE }, allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class AnuncioController {

	@Autowired
	private AnuncioService anuncioService;

	@Autowired
	private CategoriaService categoriaService;

	// Obtener anuncios
	@GetMapping("/anuncios")
	public List<Anuncio> index() {
		return anuncioService.findAll();
	}

	// Obtener anuncios (paginado)
	@GetMapping("/anuncios/page/{page}")
	public Page<Anuncio> index(@PathVariable(name = "page") Integer page) {
		return anuncioService.findAllPaginado(PageRequest.of(page, 6));
	}

	// Obtener anuncios (paginado con tamaño personalizado)
	@GetMapping("/anuncios/page/{page}/size/{size}")
	public Page<Anuncio> index(@PathVariable(name = "page") Integer page, @PathVariable(name = "size") Integer size) {
		return anuncioService.findAllPaginado(PageRequest.of(page, size));
	}

	// Obtener anuncios de un categoria específico (paginado con tamaño personalizado)
	@GetMapping("/anuncios/categoria/{categoriaId}/page/{page}/size/{size}")
	public Page<Anuncio> getAnunciosPorCategoria(
			@PathVariable(name = "categoriaId") Long categoriaId,
			@PathVariable(name = "page") Integer page,
			@PathVariable(name = "size") Integer size) {
		return anuncioService.findByCategoriaIdPaginado(categoriaId, PageRequest.of(page, size));
	}

	/*
	// Obtener mejor valorados
	@GetMapping("/anuncios/mejor-valorados")
	public List<Anuncio> getMejorValorados() {
		return anuncioService.getMejorValorados();
	} */

	// Obtener anuncios por ID
	@GetMapping("/anuncio/{id}")
	public ResponseEntity<?> show(@PathVariable(name = "id") Long id) {

		Anuncio anuncio;
		Map<String, Object> response = new HashMap<>();

		try {
			anuncio = anuncioService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (anuncio == null) {
			response.put("mensaje", "El anuncio con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(anuncio, HttpStatus.OK);
	}

	// Crear anuncio
	@PostMapping("/anuncio")
	public ResponseEntity<?> create(@RequestBody Anuncio anuncio, BindingResult result) {

		Anuncio nuevoAnuncio;
		Map<String, Object> response = new HashMap<>();

		// Validamos campos
		if (result.hasErrors()) {
			List<String> errores = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errores", errores);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		// Procesamos categorias
		if (anuncio.getCategorias() != null && !anuncio.getCategorias().isEmpty()) {
			for (Categoria categoria : anuncio.getCategorias()) {
				if (categoria.getId() != null) {
					Categoria categoriaExistente = categoriaService.findById(categoria.getId());
					if (categoriaExistente != null) {
						anuncio.getCategorias().remove(categoria);
						anuncio.getCategorias().add(categoriaExistente);
					}
				}
			}
		}

		// Manejamos errores
		try {
			nuevoAnuncio = anuncioService.save(anuncio);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El anuncio ha sido creado con éxito");
		response.put("anuncio", nuevoAnuncio);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// Actualizar anuncio
	@PutMapping("/anuncio/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> update(@RequestBody Anuncio anuncio, BindingResult result,
			@PathVariable(name = "id") Long id) {

		Anuncio currentAnuncio = this.anuncioService.findById(id);
		Anuncio nuevoAnuncio;
		Map<String, Object> response = new HashMap<>();

		// Validamos campos
		if (result.hasErrors()) {
			List<String> errores = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errores", errores);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		if (currentAnuncio == null) {
			response.put("mensaje", "No se pudo editar, el anuncio con ID: "
					.concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		try {
			// Verificamos si el campo es nulo antes de actualizarlo
			if (anuncio.getPrecioBase() != null) {
				currentAnuncio.setPrecioBase(anuncio.getPrecioBase());
			}
			if (anuncio.getTitulo() != null && !anuncio.getTitulo().isEmpty()) {
				currentAnuncio.setTitulo(anuncio.getTitulo());
			}
			if (anuncio.getDescripcion() != null && !anuncio.getDescripcion().isEmpty()) {
				currentAnuncio.setDescripcion(anuncio.getDescripcion());
			}
			if (anuncio.getImagen() != null) {
				currentAnuncio.setImagen(anuncio.getImagen());
			}

			// Procesamos categorias
			if (anuncio.getCategorias() != null && !anuncio.getCategorias().isEmpty()) {
				// Limpiamos los categorias actuales
				currentAnuncio.getCategorias().clear();

				// Añadimos los nuevos categorias
				for (Categoria categoria : anuncio.getCategorias()) {
					if (categoria.getId() != null) {
						Categoria categoriaExistente = categoriaService.findById(categoria.getId());
						if (categoriaExistente != null) {
							currentAnuncio.getCategorias().add(categoriaExistente);
						}
					}
				}
			}

			// Guardamos el anuncio actualizado
			nuevoAnuncio = anuncioService.save(currentAnuncio);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el anuncio en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El anuncio ha sido actualizado con éxito");
		response.put("anuncio", nuevoAnuncio);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// Eliminar anuncio por ID
	@DeleteMapping("/anuncio/{id}")
	public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
		Anuncio currentAnuncio = this.anuncioService.findById(id);
		Map<String, Object> response = new HashMap<>();

		// Validación de que exista el anuncio
		if (currentAnuncio == null) {
			response.put("mensaje", "El anuncio con ID: " + id + " no existe en la base de datos");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		try {
			// Verificar si el anuncio tiene categorias asociadas
			if (!currentAnuncio.getCategorias().isEmpty()) {
				response.put("mensaje", "No se puede eliminar el anuncio porque está asociado a una o más categorias");
				response.put("error",
						"El anuncio tiene categorias asociadas. Elimine primero las categorias o implemente una eliminación lógica.");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			}
			
			// Eliminar el anuncio
			anuncioService.delete(currentAnuncio);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el anuncio en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El anuncio ha sido eliminado con éxito");
		response.put("anuncio", currentAnuncio);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Eliminar todos los anuncios
	@DeleteMapping("/anuncios")
	public ResponseEntity<?> deleteAll() {
		Map<String, Object> response = new HashMap<>();

		try {
			// Verificar si hay anuncios
			if (anuncioService.findAll().isEmpty()) {
				response.put("mensaje", "No hay anuncios en la base de datos");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

			// Eliminar categorias asociadas a los anuncios
			anuncioService.findAll().forEach(anuncio -> anuncio.getCategorias().clear());

			// Eliminar todos los anuncios
			anuncioService.deleteAll();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar los anuncios en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "Todos los anuncios han sido eliminados con éxito");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
