package com.proyecto3d.backend.apirest.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;

import com.proyecto3d.backend.apirest.model.entity.Usuario;
import com.proyecto3d.backend.apirest.model.service.UsuarioService;

/**
 * Tests unitarios para RegistroController
 * Cobertura objetivo: 99%
 */
@ExtendWith(MockitoExtension.class)
class RegistroControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private RegistroController registroController;

    private Map<String, String> datosValidos;
    private Usuario usuarioMock;

    @BeforeEach
    void setUp() {
        // Datos válidos para las pruebas
        datosValidos = new HashMap<>();
        datosValidos.put("firstName", "Juan");
        datosValidos.put("lastName", "Pérez");
        datosValidos.put("email", "juan.perez@email.com");
        datosValidos.put("password", "123456");

        // Usuario mock para las respuestas
        usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setNombre("Juan");
        usuarioMock.setApellido("Pérez");
        usuarioMock.setEmail("juan.perez@email.com");
        usuarioMock.setRol("USER");
        usuarioMock.setFecha_registro(new Date());
    }

    @Test
    void testRegistroExitoso() {
        // Configuración del mock
        when(usuarioService.findByEmail(anyString())).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$encryptedPassword");
        when(usuarioService.save(any(Usuario.class))).thenReturn(usuarioMock);

        // Ejecución
        ResponseEntity<?> response = registroController.registro(datosValidos, bindingResult);

        // Verificaciones
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        
        assertNotNull(responseBody);
        assertEquals("Usuario registrado exitosamente", responseBody.get("mensaje"));
        
        Usuario usuarioRespuesta = (Usuario) responseBody.get("usuario");
        assertNotNull(usuarioRespuesta);
        assertEquals("Juan", usuarioRespuesta.getNombre());
        assertEquals("Pérez", usuarioRespuesta.getApellido());
        assertEquals("juan.perez@email.com", usuarioRespuesta.getEmail());
        assertEquals("USER", usuarioRespuesta.getRol());
        assertNull(usuarioRespuesta.getPassword()); // La contraseña no debe devolverse

        // Verificar que se llamó al servicio y al encoder
        verify(usuarioService).findByEmail("juan.perez@email.com");
        verify(passwordEncoder).encode("123456");
        verify(usuarioService).save(any(Usuario.class));
    }

    @Test
    void testRegistroSinNombre() {
        // Datos sin nombre
        datosValidos.remove("firstName");

        // Ejecución
        ResponseEntity<?> response = registroController.registro(datosValidos, bindingResult);

        // Verificaciones
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        
        assertNotNull(responseBody);
        assertEquals("El nombre es obligatorio", responseBody.get("mensaje"));

        // Verificar que no se llamó al servicio
        verify(usuarioService, never()).findByEmail(anyString());
        verify(usuarioService, never()).save(any(Usuario.class));
    }

    @Test
    void testRegistroConNombreVacio() {
        // Datos con nombre vacío
        datosValidos.put("firstName", "   ");

        // Ejecución
        ResponseEntity<?> response = registroController.registro(datosValidos, bindingResult);

        // Verificaciones
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        
        assertNotNull(responseBody);
        assertEquals("El nombre es obligatorio", responseBody.get("mensaje"));
    }

    @Test
    void testRegistroSinApellido() {
        // Datos sin apellido
        datosValidos.remove("lastName");

        // Ejecución
        ResponseEntity<?> response = registroController.registro(datosValidos, bindingResult);

        // Verificaciones
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        
        assertNotNull(responseBody);
        assertEquals("El apellido es obligatorio", responseBody.get("mensaje"));
    }

    @Test
    void testRegistroConApellidoVacio() {
        // Datos con apellido vacío
        datosValidos.put("lastName", "   ");

        // Ejecución
        ResponseEntity<?> response = registroController.registro(datosValidos, bindingResult);

        // Verificaciones
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        
        assertNotNull(responseBody);
        assertEquals("El apellido es obligatorio", responseBody.get("mensaje"));
    }

    @Test
    void testRegistroSinEmail() {
        // Datos sin email
        datosValidos.remove("email");

        // Ejecución
        ResponseEntity<?> response = registroController.registro(datosValidos, bindingResult);

        // Verificaciones
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        
        assertNotNull(responseBody);
        assertEquals("El email es obligatorio", responseBody.get("mensaje"));
    }

    @Test
    void testRegistroConEmailInvalido() {
        // Datos con email inválido
        datosValidos.put("email", "email-invalido");

        // Ejecución
        ResponseEntity<?> response = registroController.registro(datosValidos, bindingResult);

        // Verificaciones
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        
        assertNotNull(responseBody);
        assertEquals("El formato del email no es válido", responseBody.get("mensaje"));
    }

    @Test
    void testRegistroSinPassword() {
        // Datos sin contraseña
        datosValidos.remove("password");

        // Ejecución
        ResponseEntity<?> response = registroController.registro(datosValidos, bindingResult);

        // Verificaciones
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        
        assertNotNull(responseBody);
        assertEquals("La contraseña es obligatoria", responseBody.get("mensaje"));
    }

    @Test
    void testRegistroConPasswordCorta() {
        // Datos con contraseña muy corta
        datosValidos.put("password", "123");

        // Ejecución
        ResponseEntity<?> response = registroController.registro(datosValidos, bindingResult);

        // Verificaciones
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        
        assertNotNull(responseBody);
        assertEquals("La contraseña debe tener al menos 6 caracteres", responseBody.get("mensaje"));
    }

    @Test
    void testRegistroConEmailExistente() {
        // Configuración del mock - usuario ya existe
        when(usuarioService.findByEmail(anyString())).thenReturn(usuarioMock);

        // Ejecución
        ResponseEntity<?> response = registroController.registro(datosValidos, bindingResult);

        // Verificaciones
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        
        assertNotNull(responseBody);
        assertEquals("Ya existe un usuario registrado con ese email", responseBody.get("mensaje"));

        // Verificar que se llamó a findByEmail pero no a save
        verify(usuarioService).findByEmail("juan.perez@email.com");
        verify(usuarioService, never()).save(any(Usuario.class));
    }

    @Test
    void testRegistroConErrorBaseDatos() {
        // Configuración del mock - usuario no existe pero falla al guardar
        when(usuarioService.findByEmail(anyString())).thenReturn(null);
        when(usuarioService.save(any(Usuario.class))).thenThrow(new DataAccessException("Error de base de datos") {
            private static final long serialVersionUID = 1L;
        });

        // Ejecución
        ResponseEntity<?> response = registroController.registro(datosValidos, bindingResult);

        // Verificaciones
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        
        assertNotNull(responseBody);
        assertEquals("Error al registrar el usuario en la base de datos", responseBody.get("mensaje"));
        assertNotNull(responseBody.get("error"));

        // Verificar que se llamó a ambos métodos del servicio
        verify(usuarioService).findByEmail("juan.perez@email.com");
        verify(usuarioService).save(any(Usuario.class));
    }

    @Test
    void testRegistroConEmailConEspacios() {
        // Email con espacios al inicio y final
        datosValidos.put("email", "  juan.perez@email.com  ");

        // Configuración del mock
        when(usuarioService.findByEmail("juan.perez@email.com")).thenReturn(null);
        when(usuarioService.save(any(Usuario.class))).thenReturn(usuarioMock);

        // Ejecución
        ResponseEntity<?> response = registroController.registro(datosValidos, bindingResult);

        // Verificaciones
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        
        // Verificar que se limpió el email correctamente
        verify(usuarioService).findByEmail("juan.perez@email.com");
    }

    @Test
    void testRegistroConNombreYApellidoConEspacios() {
        // Nombre y apellido con espacios
        datosValidos.put("firstName", "  Juan  ");
        datosValidos.put("lastName", "  Pérez  ");

        // Configuración del mock
        when(usuarioService.findByEmail(anyString())).thenReturn(null);
        when(usuarioService.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            assertEquals("Juan", usuario.getNombre());
            assertEquals("Pérez", usuario.getApellido());
            return usuarioMock;
        });

        // Ejecución
        ResponseEntity<?> response = registroController.registro(datosValidos, bindingResult);

        // Verificaciones
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testRegistroVerificaRolPorDefecto() {
        // Configuración del mock
        when(usuarioService.findByEmail(anyString())).thenReturn(null);
        when(usuarioService.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            assertEquals("USER", usuario.getRol());
            assertNotNull(usuario.getFecha_registro());
            return usuarioMock;
        });

        // Ejecución
        ResponseEntity<?> response = registroController.registro(datosValidos, bindingResult);

        // Verificaciones
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
} 