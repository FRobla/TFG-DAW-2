package com.evidenlibrary.backend.apirest.controller;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.persistence.EntityManager;

import com.evidenlibrary.backend.apirest.model.entity.Impresora;
import com.evidenlibrary.backend.apirest.model.entity.Categoria;
import com.evidenlibrary.backend.apirest.model.entity.Anuncio;
import com.evidenlibrary.backend.apirest.model.service.ImpresoraService;
import com.evidenlibrary.backend.apirest.model.service.CategoriaService;
import com.evidenlibrary.backend.apirest.model.service.AnuncioService;

@CrossOrigin(origins = { "http://localhost:4200" }, methods = { RequestMethod.GET, RequestMethod.POST,
		RequestMethod.PUT, RequestMethod.DELETE }, allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class AnuncioController {

	@Autowired
	private AnuncioService anuncioService;

	@Autowired
	private ImpresoraService impresoraService;

	@Autowired
	private CategoriaService categoriaService;

	@Autowired
	private EntityManager entityManager;

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

	// Obtener anuncios de una impresora específica (paginada con tamaño personalizado)
	@GetMapping("/anuncios/impresora/{impresoraId}/page/{page}/size/{size}")
	public Page<Anuncio> getAnunciosPorImpresora(
			@PathVariable(name = "impresoraId") Long impresoraId,
			@PathVariable(name = "page") Integer page,
			@PathVariable(name = "size") Integer size) {
		return anuncioService.findByImpresoraIdPaginado(impresoraId, PageRequest.of(page, size));
	}

	// Obtener anuncios de un categoria específico (paginado con tamaño personalizado)
	@GetMapping("/anuncios/categoria/{categoriaId}/page/{page}/size/{size}")
	public Page<Anuncio> getAnunciosPorCategoria(
			@PathVariable(name = "categoriaId") Long categoriaId,
			@PathVariable(name = "page") Integer page,
			@PathVariable(name = "size") Integer size) {
		return anuncioService.findByCategoriaIdPaginado(categoriaId, PageRequest.of(page, size));
	}

	// Obtener anuncios de un categoria y impresora específica (paginado con tamaño
	// personalizado)
	@GetMapping("/anuncios/impresora/{impresoraId}/categoria/{categoriaId}/page/{page}/size/{size}")
	public ResponseEntity<?> getAnunciosPorCategoriaYImpresora(
			@PathVariable(name = "impresoraId") Long impresoraId,
			@PathVariable(name = "categoriaId") Long categoriaId,
			@PathVariable(name = "page") Integer page,
			@PathVariable(name = "size") Integer size) {
		// Removed call to findByCategoriaIdAndImpresoraIdPaginado, which does not exist in service anymore
        // You can implement a filter logic here if needed, or return a bad request/empty result
        return ResponseEntity.badRequest().body(java.util.Collections.emptyList());
	}

	// Obtener mejor valorados
	@GetMapping("/anuncios/mejor-valorados")
	public List<Anuncio> getMejorValorados() {
		return anuncioService.getMejorValorados();
	}

	// Obtener anuncios por ID
	@GetMapping("/anuncio/{id}")
	public ResponseEntity<?> show(@PathVariable(name = "id") Long id) {

		Anuncio anuncio;
		Map<String, Object> response = new HashMap<>();

		try {
			// calcula la media cada vez que carga
			anuncioService.obtenerAnuncioConValoracionMedia(id);
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

		// Procesamos impresoras y categorias
		if (anuncio.getImpresora() != null && !anuncio.getImpresora().isEmpty()) {
			for (Impresora impresora : anuncio.getImpresora()) {
				if (impresora.getId() != null) {
					Impresora impresoraExistente = impresoraService.findById(impresora.getId());
					if (impresoraExistente != null) {
						anuncio.getImpresora().remove(impresora);
						anuncio.getImpresora().add(impresoraExistente);
					}
				}
			}
		}

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

			// Procesamos impresoras
			if (anuncio.getImpresora() != null && !anuncio.getImpresora().isEmpty()) {
				// Limpiamos las impresoras actuales
				currentAnuncio.getImpresora().clear();

				// Añadimos las nuevos impresoras
				for (Impresora impresora : anuncio.getImpresora()) {
					if (impresora.getId() != null) {
						Impresora impresoraExistente = impresoraService.findById(impresora.getId());
						if (impresoraExistente != null) {
							currentAnuncio.getImpresora().add(impresoraExistente);
						}
					}
				}
			}

			// Procesamos géneros
			if (anuncio.getCategorias() != null && !anuncio.getCategorias().isEmpty()) {
				// Limpiamos los géneros actuales
				currentAnuncio.getCategorias().clear();

				// Añadimos los nuevos géneros
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
			// Verificar si el anuncio tiene pedidos asociados
			Long count = (Long) entityManager.createQuery(
					"SELECT COUNT(dp) FROM DetallePedido dp WHERE dp.anuncio.id = :anuncioId")
					.setParameter("anuncioId", id)
					.getSingleResult();

			if (count > 0) {
				response.put("mensaje", "No se puede eliminar el anuncio porque está asociado a uno o más pedidos");
				response.put("error",
						"El anuncio tiene pedidos asociados. Elimine primero los pedidos o implemente una eliminación lógica.");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			}

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
