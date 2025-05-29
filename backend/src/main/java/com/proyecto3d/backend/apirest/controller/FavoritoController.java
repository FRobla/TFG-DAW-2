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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto3d.backend.apirest.model.entity.Anuncio;
import com.proyecto3d.backend.apirest.model.entity.Favorito;
import com.proyecto3d.backend.apirest.model.entity.Usuario;
import com.proyecto3d.backend.apirest.model.service.AnuncioService;
import com.proyecto3d.backend.apirest.model.service.FavoritoService;
import com.proyecto3d.backend.apirest.model.service.UsuarioService;

/**
 * Controlador REST para la gestión de favoritos de usuarios
 */
@CrossOrigin(origins = { "http://localhost:4200" }, methods = { RequestMethod.GET, RequestMethod.POST,
		RequestMethod.PUT, RequestMethod.DELETE }, allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class FavoritoController {

	@Autowired
	private FavoritoService favoritoService;
	
	@Autowired
	private AnuncioService anuncioService;
	
	@Autowired
	private UsuarioService usuarioService;

	// Obtener todos los favoritos
	@GetMapping("/favoritos")
	public List<Favorito> index() {
		return favoritoService.findAll();
	}

	// Obtener favoritos por usuario con paginación
	@GetMapping("/favoritos/usuario/{usuarioId}")
	public ResponseEntity<?> getFavoritosByUsuario(
			@PathVariable Long usuarioId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			Pageable pageable = PageRequest.of(page, size);
			Page<Favorito> favoritos = favoritoService.findByUsuarioId(usuarioId, pageable);
			
			response.put("favoritos", favoritos.getContent());
			response.put("currentPage", favoritos.getNumber());
			response.put("totalItems", favoritos.getTotalElements());
			response.put("totalPages", favoritos.getTotalPages());
			response.put("pageSize", favoritos.getSize());
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al obtener favoritos del usuario");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Obtener favoritos por usuario sin paginación
	@GetMapping("/favoritos/usuario/{usuarioId}/simple")
	public ResponseEntity<?> getFavoritosByUsuarioSimple(@PathVariable Long usuarioId) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			List<Favorito> favoritos = favoritoService.findByUsuarioId(usuarioId);
			response.put("favoritos", favoritos);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al obtener favoritos del usuario");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Verificar si un anuncio es favorito de un usuario
	@GetMapping("/favoritos/check/{anuncioId}/{usuarioId}")
	public ResponseEntity<?> checkFavorito(@PathVariable Long anuncioId, @PathVariable Long usuarioId) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			boolean esFavorito = favoritoService.existeFavoritoUsuarioAnuncio(anuncioId, usuarioId);
			response.put("esFavorito", esFavorito);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al verificar favorito");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Obtener favorito por ID
	@GetMapping("/favorito/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Favorito favorito;
		Map<String, Object> response = new HashMap<>();

		try {
			favorito = favoritoService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (favorito == null) {
			response.put("mensaje", "El favorito con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(favorito, HttpStatus.OK);
	}

	// Contar favoritos por anuncio
	@GetMapping("/favoritos/contar/anuncio/{anuncioId}")
	public ResponseEntity<?> countFavoritosByAnuncio(@PathVariable Long anuncioId) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			Long count = favoritoService.getNumeroFavoritosByAnuncioId(anuncioId);
			response.put("count", count);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al contar favoritos del anuncio");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Contar favoritos por usuario
	@GetMapping("/favoritos/contar/usuario/{usuarioId}")
	public ResponseEntity<?> countFavoritosByUsuario(@PathVariable Long usuarioId) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			Long count = favoritoService.getNumeroFavoritosByUsuarioId(usuarioId);
			response.put("count", count);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al contar favoritos del usuario");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Marcar anuncio como favorito
	@PostMapping("/favorito/{anuncioId}/{usuarioId}")
	public ResponseEntity<?> marcarFavorito(@PathVariable Long anuncioId, @PathVariable Long usuarioId) {
		Map<String, Object> response = new HashMap<>();

		try {
			// Verificar si ya existe
			boolean yaExiste = favoritoService.existeFavoritoUsuarioAnuncio(anuncioId, usuarioId);
			if (yaExiste) {
				response.put("mensaje", "El anuncio ya está marcado como favorito");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
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

			// Crear favorito
			Favorito favorito = new Favorito();
			favorito.setAnuncio(anuncio);
			favorito.setUsuario(usuario);
			favorito.setFechaMarcado(new Date());

			Favorito nuevoFavorito = favoritoService.save(favorito);
			
			response.put("mensaje", "Anuncio marcado como favorito con éxito");
			response.put("favorito", nuevoFavorito);
			return new ResponseEntity<>(response, HttpStatus.CREATED);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al marcar como favorito");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Crear favorito (método alternativo)
	@PostMapping("/favorito")
	public ResponseEntity<?> create(@RequestBody Favorito favorito, BindingResult result) {
		Favorito nuevoFavorito;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errores = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errores", errores);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			// Verificar si ya existe
			boolean yaExiste = favoritoService.existeFavoritoUsuarioAnuncio(
					favorito.getAnuncio().getId(), 
					favorito.getUsuario().getId());
			
			if (yaExiste) {
				response.put("mensaje", "El anuncio ya está marcado como favorito");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			}

			favorito.setFechaMarcado(new Date());
			nuevoFavorito = favoritoService.save(favorito);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El favorito ha sido creado con éxito");
		response.put("favorito", nuevoFavorito);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// Eliminar favorito por ID
	@DeleteMapping("/favorito/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		try {
			Favorito favorito = favoritoService.findById(id);
			if (favorito != null) {
				favoritoService.delete(favorito);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el favorito de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El favorito ha sido eliminado con éxito");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Eliminar favorito por anuncio y usuario
	@DeleteMapping("/favorito/{anuncioId}/{usuarioId}")
	public ResponseEntity<?> deleteFavorito(@PathVariable Long anuncioId, @PathVariable Long usuarioId) {
		Map<String, Object> response = new HashMap<>();

		try {
			boolean existia = favoritoService.existeFavoritoUsuarioAnuncio(anuncioId, usuarioId);
			if (!existia) {
				response.put("mensaje", "El favorito no existe");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

			favoritoService.toggleFavorito(anuncioId, usuarioId);
			response.put("mensaje", "Favorito eliminado con éxito");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el favorito");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Obtener anuncios más favoritos
	@GetMapping("/favoritos/mas-favoritos")
	public ResponseEntity<?> getAnunciosMasFavoritos() {
		Map<String, Object> response = new HashMap<>();
		
		try {
			List<Map<String, Object>> anunciosMasFavoritos = favoritoService.getAnunciosMasFavoritos();
			response.put("anunciosMasFavoritos", anunciosMasFavoritos);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al obtener anuncios más favoritos");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
} 