package com.proyecto3d.backend.apirest.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import com.proyecto3d.backend.apirest.model.entity.Anuncio;
import com.proyecto3d.backend.apirest.model.entity.Usuario;
import com.proyecto3d.backend.apirest.model.entity.Valoracion;
import com.proyecto3d.backend.apirest.model.service.AnuncioService;
import com.proyecto3d.backend.apirest.model.service.UsuarioService;
import com.proyecto3d.backend.apirest.model.service.ValoracionService;

/**
 * Controlador REST para la gestión de valoraciones de anuncios
 */
@CrossOrigin(origins = { "http://localhost:4200" }, methods = { RequestMethod.GET, RequestMethod.POST,
		RequestMethod.PUT, RequestMethod.DELETE }, allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class ValoracionController {

	@Autowired
	private ValoracionService valoracionService;
	
	@Autowired
	private AnuncioService anuncioService;
	
	@Autowired
	private UsuarioService usuarioService;

	// Obtener todas las valoraciones
	@GetMapping("/valoraciones")
	public List<Valoracion> index() {
		return valoracionService.findAll();
	}

	// Obtener valoraciones por anuncio con paginación
	@GetMapping("/valoraciones/anuncio/{anuncioId}")
	public ResponseEntity<?> getValoracionesByAnuncio(
			@PathVariable Long anuncioId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			Pageable pageable = PageRequest.of(page, size);
			Page<Valoracion> valoraciones = valoracionService.findByAnuncioId(anuncioId, pageable);
			
			response.put("valoraciones", valoraciones.getContent());
			response.put("currentPage", valoraciones.getNumber());
			response.put("totalItems", valoraciones.getTotalElements());
			response.put("totalPages", valoraciones.getTotalPages());
			response.put("pageSize", valoraciones.getSize());
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al obtener valoraciones del anuncio");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Obtener valoraciones por anuncio sin paginación
	@GetMapping("/valoraciones/anuncio/{anuncioId}/simple")
	public ResponseEntity<?> getValoracionesByAnuncioSimple(@PathVariable Long anuncioId) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			List<Valoracion> valoraciones = valoracionService.findByAnuncioId(anuncioId);
			response.put("valoraciones", valoraciones);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al obtener valoraciones del anuncio");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Obtener valoraciones por usuario
	@GetMapping("/valoraciones/usuario/{usuarioId}")
	public ResponseEntity<?> getValoracionesByUsuario(@PathVariable Long usuarioId) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			List<Valoracion> valoraciones = valoracionService.findByUsuarioId(usuarioId);
			response.put("valoraciones", valoraciones);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al obtener valoraciones del usuario");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Verificar si un usuario ya valoró un anuncio
	@GetMapping("/valoraciones/check/{anuncioId}/{usuarioId}")
	public ResponseEntity<?> checkValoracion(@PathVariable Long anuncioId, @PathVariable Long usuarioId) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			boolean yaValoro = valoracionService.existeValoracionUsuarioAnuncio(anuncioId, usuarioId);
			response.put("yaValoro", yaValoro);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al verificar valoración");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Obtener valoración por ID
	@GetMapping("/valoracion/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Valoracion valoracion;
		Map<String, Object> response = new HashMap<>();

		try {
			valoracion = valoracionService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (valoracion == null) {
			response.put("mensaje", "La valoración con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(valoracion, HttpStatus.OK);
	}

	// Obtener valoración media de un anuncio
	@GetMapping("/valoraciones/media/anuncio/{anuncioId}")
	public ResponseEntity<?> getValoracionMedia(@PathVariable Long anuncioId) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			Double media = valoracionService.getValoracionMediaByAnuncioId(anuncioId);
			Long cantidad = valoracionService.getNumeroValoracionesByAnuncioId(anuncioId);
			
			response.put("media", media);
			response.put("cantidad", cantidad);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al obtener valoración media");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Obtener distribución de valoraciones
	@GetMapping("/valoraciones/distribucion")
	public ResponseEntity<?> getDistribucionValoraciones() {
		Map<String, Object> response = new HashMap<>();
		
		try {
			List<Map<String, Object>> distribucion = valoracionService.getDistribucionValoraciones();
			response.put("distribucion", distribucion);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al obtener distribución de valoraciones");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Crear valoración
	@PostMapping("/valoracion")
	public ResponseEntity<?> create(@RequestBody Valoracion valoracion, BindingResult result) {
		Valoracion nuevaValoracion;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errores = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errores", errores);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			// Verificar si ya existe una valoración del usuario para este anuncio
			boolean yaValoro = valoracionService.existeValoracionUsuarioAnuncio(
					valoracion.getAnuncio().getId(), 
					valoracion.getUsuario().getId());
			
			if (yaValoro) {
				response.put("mensaje", "El usuario ya ha valorado este anuncio");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			}

			// Validar puntuación
			if (valoracion.getPuntuacion() < 1 || valoracion.getPuntuacion() > 5) {
				response.put("mensaje", "La puntuación debe estar entre 1 y 5");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			valoracion.setFechaValoracion(new Date());
			valoracion.setVerificado(false); // Por defecto no verificado
			
			nuevaValoracion = valoracionService.save(valoracion);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La valoración ha sido creada con éxito");
		response.put("valoracion", nuevaValoracion);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// Crear valoración directa con parámetros
	@PostMapping("/valoracion/{anuncioId}/{usuarioId}")
	public ResponseEntity<?> crearValoracion(
			@PathVariable Long anuncioId, 
			@PathVariable Long usuarioId,
			@RequestParam Integer puntuacion,
			@RequestParam(required = false) String comentario) {
		
		Map<String, Object> response = new HashMap<>();

		try {
			// Verificar si ya existe
			boolean yaValoro = valoracionService.existeValoracionUsuarioAnuncio(anuncioId, usuarioId);
			if (yaValoro) {
				response.put("mensaje", "El usuario ya ha valorado este anuncio");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			}

			// Validar puntuación
			if (puntuacion < 1 || puntuacion > 5) {
				response.put("mensaje", "La puntuación debe estar entre 1 y 5");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			// Obtener anuncio y usuario
			Anuncio anuncio = anuncioService.findById(anuncioId);
			Usuario usuario = usuarioService.findById(usuarioId);

			if (anuncio == null) {
				response.put("mensaje", "El anuncio con ID: " + anuncioId + " no existe");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

			if (usuario == null) {
				response.put("mensaje", "El usuario con ID: " + usuarioId + " no existe");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

			// Crear valoración
			Valoracion valoracion = new Valoracion();
			valoracion.setAnuncio(anuncio);
			valoracion.setUsuario(usuario);
			valoracion.setPuntuacion(puntuacion);
			valoracion.setComentario(comentario);
			valoracion.setFechaValoracion(new Date());
			valoracion.setVerificado(false);

			Valoracion nuevaValoracion = valoracionService.save(valoracion);
			
			response.put("mensaje", "Valoración creada con éxito");
			response.put("valoracion", nuevaValoracion);
			return new ResponseEntity<>(response, HttpStatus.CREATED);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al crear la valoración");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Actualizar valoración
	@PutMapping("/valoracion/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> update(@RequestBody Valoracion valoracion, BindingResult result,
			@PathVariable Long id) {

		Valoracion currentValoracion = valoracionService.findById(id);
		Valoracion nuevaValoracion;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errores = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errores", errores);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		if (currentValoracion == null) {
			response.put("mensaje", "No se pudo editar, la valoración con ID: "
					.concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		try {
			// Validar puntuación
			if (valoracion.getPuntuacion() < 1 || valoracion.getPuntuacion() > 5) {
				response.put("mensaje", "La puntuación debe estar entre 1 y 5");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			currentValoracion.setPuntuacion(valoracion.getPuntuacion());
			currentValoracion.setComentario(valoracion.getComentario());
			currentValoracion.setRespuestaProveedor(valoracion.getRespuestaProveedor());
			if (valoracion.getRespuestaProveedor() != null && !valoracion.getRespuestaProveedor().isEmpty()) {
				currentValoracion.setFechaRespuesta(new Date());
			}
			currentValoracion.setVerificado(valoracion.getVerificado());

			nuevaValoracion = valoracionService.save(currentValoracion);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La valoración ha sido actualizada con éxito");
		response.put("valoracion", nuevaValoracion);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// Eliminar valoración
	@DeleteMapping("/valoracion/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		try {
			Valoracion valoracion = valoracionService.findById(id);
			if (valoracion != null) {
				valoracionService.delete(valoracion);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar la valoración de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La valoración ha sido eliminada con éxito");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Eliminar todas las valoraciones
	@DeleteMapping("/valoraciones")
	public ResponseEntity<?> deleteAll() {
		Map<String, Object> response = new HashMap<>();

		try {
			valoracionService.deleteAll();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar las valoraciones de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "Todas las valoraciones han sido eliminadas con éxito");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
} 