package com.proyecto3d.backend.apirest.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto3d.backend.apirest.model.entity.Usuario;
import com.proyecto3d.backend.apirest.model.service.UsuarioService;

/**
 * Controlador REST para el registro público de usuarios
 */
@CrossOrigin(origins = { "http://localhost:4200" }, methods = { RequestMethod.POST }, allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class RegistroController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Endpoint para registro público de usuarios
     * @param datos - Datos del usuario a registrar (nombre, apellido, email, password)
     * @param result - Resultado de validación
     * @return ResponseEntity con el usuario creado o mensaje de error
     */
    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody Map<String, String> datos, BindingResult result) {
        
        Usuario nuevoUsuario;
        Map<String, Object> response = new HashMap<>();

        // Validar campos obligatorios
        if (!datos.containsKey("firstName") || datos.get("firstName").trim().isEmpty()) {
            response.put("mensaje", "El nombre es obligatorio");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        
        if (!datos.containsKey("lastName") || datos.get("lastName").trim().isEmpty()) {
            response.put("mensaje", "El apellido es obligatorio");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        
        if (!datos.containsKey("email") || datos.get("email").trim().isEmpty()) {
            response.put("mensaje", "El email es obligatorio");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        
        if (!datos.containsKey("password") || datos.get("password").trim().isEmpty()) {
            response.put("mensaje", "La contraseña es obligatoria");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Validar formato de email
        String email = datos.get("email").trim();
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            response.put("mensaje", "El formato del email no es válido");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Validar longitud de contraseña
        String password = datos.get("password");
        if (password.length() < 6) {
            response.put("mensaje", "La contraseña debe tener al menos 6 caracteres");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            // Verificar si el email ya existe
            Usuario usuarioExistente = usuarioService.findByEmail(email);
            if (usuarioExistente != null) {
                response.put("mensaje", "Ya existe un usuario registrado con ese email");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }

            // Crear nuevo usuario
            nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(datos.get("firstName").trim());
            nuevoUsuario.setApellido(datos.get("lastName").trim());
            nuevoUsuario.setEmail(email);
            // Encriptar contraseña antes de guardarla
            nuevoUsuario.setPassword(passwordEncoder.encode(password));
            nuevoUsuario.setRol("USER"); // Rol por defecto
            nuevoUsuario.setFecha_registro(new Date());

            // Guardar en la base de datos
            nuevoUsuario = usuarioService.save(nuevoUsuario);

            // No devolver la contraseña en la respuesta
            nuevoUsuario.setPassword(null);

        } catch (DataAccessException e) {
            response.put("mensaje", "Error al registrar el usuario en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "Usuario registrado exitosamente");
        response.put("usuario", nuevoUsuario);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
} 