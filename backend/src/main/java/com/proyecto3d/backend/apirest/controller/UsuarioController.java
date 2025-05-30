package com.proyecto3d.backend.apirest.controller;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto3d.backend.apirest.model.entity.Usuario;
import com.proyecto3d.backend.apirest.model.entity.Ubicacion;
import com.proyecto3d.backend.apirest.model.service.UsuarioService;
import com.proyecto3d.backend.apirest.model.service.UbicacionService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private UbicacionService ubicacionService;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Obtener todos los usuarios
    @GetMapping("/usuarios")
    public List<Usuario> index() {
        return usuarioService.findAll();
    }

    // Obtener usuario por ID
    @GetMapping("/usuario/{id}")
    public ResponseEntity<?> show(@PathVariable(name = "id") Long id) {

        Usuario usuario;
        Map<String, Object> response = new HashMap<>();

        try {
            usuario = usuarioService.findById(id);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (usuario == null) {
            response.put("mensaje", "El usuario con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    // Validar contraseña actual del usuario
    @PostMapping("/usuario/{id}/validar-password")
    public ResponseEntity<?> validarPassword(@PathVariable(name = "id") Long id, @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        String passwordActual = request.get("password");
        
        if (passwordActual == null || passwordActual.isEmpty()) {
            response.put("mensaje", "La contraseña es requerida");
            response.put("valida", false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            Usuario usuario = usuarioService.findById(id);
            
            if (usuario == null) {
                response.put("mensaje", "Usuario no encontrado");
                response.put("valida", false);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            // Verificar si la contraseña coincide usando BCryptPasswordEncoder
            boolean passwordValida = passwordEncoder.matches(passwordActual, usuario.getPassword());
            
            response.put("valida", passwordValida);
            response.put("mensaje", passwordValida ? "Contraseña válida" : "Contraseña incorrecta");
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al validar la contraseña");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("valida", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Cambiar contraseña del usuario
    @PutMapping("/usuario/{id}/cambiar-password")
    public ResponseEntity<?> cambiarPassword(@PathVariable(name = "id") Long id, @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        String nuevaPassword = request.get("password");
        
        if (nuevaPassword == null || nuevaPassword.isEmpty()) {
            response.put("mensaje", "La nueva contraseña es requerida");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (nuevaPassword.length() < 6) {
            response.put("mensaje", "La contraseña debe tener al menos 6 caracteres");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            Usuario usuario = usuarioService.findById(id);
            
            if (usuario == null) {
                response.put("mensaje", "Usuario no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            // Encriptar y actualizar la nueva contraseña
            usuario.setPassword(passwordEncoder.encode(nuevaPassword));
            usuarioService.save(usuario);
            
            response.put("mensaje", "Contraseña actualizada correctamente");
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al cambiar la contraseña");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar foto de perfil del usuario
    @PutMapping("/usuario/{id}/foto")
    public ResponseEntity<?> actualizarFotoPerfil(@PathVariable(name = "id") Long id, @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        String fotoBase64 = request.get("foto");
        
        if (fotoBase64 == null || fotoBase64.isEmpty()) {
            response.put("mensaje", "La foto es requerida");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            Usuario usuario = usuarioService.findById(id);
            
            if (usuario == null) {
                response.put("mensaje", "Usuario no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            // Actualizar la foto de perfil
            usuario.setFoto(fotoBase64);
            usuarioService.save(usuario);
            
            response.put("mensaje", "Foto de perfil actualizada correctamente");
            response.put("usuario", usuario);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar la foto de perfil");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Crear un nuevo usuario
    @PostMapping("/usuario")
    public ResponseEntity<?> create(@RequestBody Usuario usuario, BindingResult result) {

        Usuario nuevoUsuario;
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
            nuevoUsuario = usuarioService.save(usuario);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al insertar en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El usuario ha sido creado con éxito");
        response.put("usuario", nuevoUsuario);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Actualizar usuario
    @PutMapping("/usuario/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> update(@RequestBody Usuario usuario, BindingResult result, @PathVariable(name = "id") Long id) {

        Usuario currentUsuario = this.usuarioService.findById(id);
        Usuario nuevoUsuario;
        Map<String, Object> response = new HashMap<>();

        // Validamos campos
        if (result.hasErrors()) {

            List<String> errores = result.getFieldErrors().stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errores", errores);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (currentUsuario == null) {
            response.put("mensaje", "No se puede editar, el usuario con ID: "
                    .concat(id.toString().concat(" no existe en la base de datos")));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        try {
            currentUsuario.setApellido(usuario.getApellido());
            currentUsuario.setDireccion(usuario.getDireccion());
            currentUsuario.setNombre(usuario.getNombre());
            currentUsuario.setEmail(usuario.getEmail());
            // Solo actualizar la contraseña si se proporciona una nueva y encriptarla
            if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
                currentUsuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
            // Actualizar la foto de perfil si se proporciona una nueva
            if (usuario.getFoto() != null) {
                currentUsuario.setFoto(usuario.getFoto());
            }
            // Actualizar la ubicación si se proporciona
            if (usuario.getUbicacion() != null && usuario.getUbicacion().getId() != null) {
                Ubicacion ubicacion = ubicacionService.findById(usuario.getUbicacion().getId());
                currentUsuario.setUbicacion(ubicacion);
            } else if (usuario.getUbicacion() == null) {
                // Si se envía ubicacion como null, eliminar la ubicación del usuario
                currentUsuario.setUbicacion(null);
            }
            currentUsuario.setRol(usuario.getRol());

            nuevoUsuario = usuarioService.save(currentUsuario);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar el usuario en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El usuario ha sido actualizado con éxito");
        response.put("usuario", nuevoUsuario);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Eliminar usuario por ID
    @DeleteMapping("/usuario/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        Usuario currentUsuario = this.usuarioService.findById(id);
        Map<String, Object> response = new HashMap<>();

        // Validación de que exista el usuario
        if (currentUsuario == null) {
            response.put("mensaje", "El usuario con ID: " + id + " no existe en la base de datos");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        try {
            usuarioService.delete(currentUsuario);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al eliminar el usuario en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El usuario ha sido eliminado con éxito");
        response.put("usuario", currentUsuario);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Obtener usuario por email
    @GetMapping("/usuario/email/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable(name = "email") String email) {
        Usuario usuario;
        Map<String, Object> response = new HashMap<>();

        try {
            usuario = usuarioService.findByEmail(email);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (usuario == null) {
            response.put("mensaje", "El usuario con email: ".concat(email.concat(" no existe en la base de datos")));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

}
