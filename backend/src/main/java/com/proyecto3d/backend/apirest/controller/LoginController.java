package com.proyecto3d.backend.apirest.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto3d.backend.apirest.model.entity.Usuario;
import com.proyecto3d.backend.apirest.model.service.UsuarioService;

/**
 * Controlador REST para autenticación de usuarios
 */
@CrossOrigin(origins = { "http://localhost:4200" }, methods = { RequestMethod.POST }, allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Endpoint para autenticación de usuarios
     * @param credenciales - Email y contraseña del usuario
     * @return ResponseEntity con información del usuario autenticado o error
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        
        Map<String, Object> response = new HashMap<>();

        // Validar que se envíen las credenciales
        if (!credenciales.containsKey("email") || credenciales.get("email").trim().isEmpty()) {
            response.put("mensaje", "El email es obligatorio");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        
        if (!credenciales.containsKey("password") || credenciales.get("password").isEmpty()) {
            response.put("mensaje", "La contraseña es obligatoria");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        String email = credenciales.get("email").trim();
        String password = credenciales.get("password");

        try {
            // Buscar usuario por email
            Usuario usuario = usuarioService.findByEmail(email);
            
            if (usuario == null) {
                response.put("mensaje", "Credenciales inválidas");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

            // Verificar contraseña
            if (!passwordEncoder.matches(password, usuario.getPassword())) {
                response.put("mensaje", "Credenciales inválidas");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

            // Autenticación exitosa
            // No devolver la contraseña
            usuario.setPassword(null);
            
            // Crear respuesta de autenticación
            response.put("mensaje", "Autenticación exitosa");
            response.put("usuario", usuario);
            response.put("id", usuario.getId());
            response.put("username", usuario.getNombre() + " " + usuario.getApellido());
            response.put("email", usuario.getEmail());
            response.put("rol", usuario.getRol());
            
            // Generar un token simple (en un entorno real usarías JWT)
            String simpleToken = "local_" + usuario.getId() + "_" + System.currentTimeMillis();
            response.put("access_token", simpleToken);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("mensaje", "Error interno del servidor");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}