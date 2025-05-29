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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.proyecto3d.backend.apirest.model.entity.Usuario;
import com.proyecto3d.backend.apirest.model.service.UsuarioService;

/**
 * Tests unitarios para LoginController
 * Cobertura objetivo: 99%
 */
@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginController loginController;

    private Map<String, String> credencialesValidas;
    private Usuario usuarioMock;

    @BeforeEach
    void setUp() {
        // Credenciales válidas para las pruebas
        credencialesValidas = new HashMap<>();
        credencialesValidas.put("email", "juan.perez@email.com");
        credencialesValidas.put("password", "123456");

        // Usuario mock para las respuestas
        usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setNombre("Juan");
        usuarioMock.setApellido("Pérez");
        usuarioMock.setEmail("juan.perez@email.com");
        usuarioMock.setPassword("$2a$10$encryptedPassword");
        usuarioMock.setRol("USER");
        usuarioMock.setFecha_registro(new Date());
    }

    @Test
    void testLoginExitoso() {
        // Configuración del mock
        when(usuarioService.findByEmail("juan.perez@email.com")).thenReturn(usuarioMock);
        when(passwordEncoder.matches("123456", "$2a$10$encryptedPassword")).thenReturn(true);

        // Ejecución
        ResponseEntity<?> response = loginController.login(credencialesValidas);

        // Verificaciones
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        
        assertNotNull(responseBody);
        assertEquals("Autenticación exitosa", responseBody.get("mensaje"));
        assertEquals(1L, responseBody.get("id"));
        assertEquals("Juan Pérez", responseBody.get("username"));
        assertEquals("juan.perez@email.com", responseBody.get("email"));
        assertEquals("USER", responseBody.get("rol"));
        assertNotNull(responseBody.get("access_token"));
        assertTrue(responseBody.get("access_token").toString().startsWith("local_1_"));

        // Verificar que se llamó a los servicios
        verify(usuarioService).findByEmail("juan.perez@email.com");
        verify(passwordEncoder).matches("123456", "$2a$10$encryptedPassword");
    }

    @Test
    void testLoginSinEmail() {
        // Credenciales sin email
        credencialesValidas.remove("email");

        // Ejecución
        ResponseEntity<?> response = loginController.login(credencialesValidas);

        // Verificaciones
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        
        assertNotNull(responseBody);
        assertEquals("El email es obligatorio", responseBody.get("mensaje"));

        // Verificar que no se llamó a los servicios
        verify(usuarioService, never()).findByEmail(anyString());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void testLoginConEmailVacio() {
        // Email vacío
        credencialesValidas.put("email", "   ");

        // Ejecución
        ResponseEntity<?> response = loginController.login(credencialesValidas);

        // Verificaciones
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        
        assertNotNull(responseBody);
        assertEquals("El email es obligatorio", responseBody.get("mensaje"));
    }

    @Test
    void testLoginSinPassword() {
        // Credenciales sin contraseña
        credencialesValidas.remove("password");

        // Ejecución
        ResponseEntity<?> response = loginController.login(credencialesValidas);

        // Verificaciones
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        
        assertNotNull(responseBody);
        assertEquals("La contraseña es obligatoria", responseBody.get("mensaje"));
    }

    @Test
    void testLoginConPasswordVacia() {
        // Contraseña vacía
        credencialesValidas.put("password", "");

        // Ejecución
        ResponseEntity<?> response = loginController.login(credencialesValidas);

        // Verificaciones
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        
        assertNotNull(responseBody);
        assertEquals("La contraseña es obligatoria", responseBody.get("mensaje"));
    }

    @Test
    void testLoginUsuarioNoExiste() {
        // Configuración del mock - usuario no existe
        when(usuarioService.findByEmail("juan.perez@email.com")).thenReturn(null);

        // Ejecución
        ResponseEntity<?> response = loginController.login(credencialesValidas);

        // Verificaciones
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        
        assertNotNull(responseBody);
        assertEquals("Credenciales inválidas", responseBody.get("mensaje"));

        // Verificar que se llamó a findByEmail pero no a matches
        verify(usuarioService).findByEmail("juan.perez@email.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void testLoginPasswordIncorrecta() {
        // Configuración del mock - contraseña incorrecta
        when(usuarioService.findByEmail("juan.perez@email.com")).thenReturn(usuarioMock);
        when(passwordEncoder.matches("123456", "$2a$10$encryptedPassword")).thenReturn(false);

        // Ejecución
        ResponseEntity<?> response = loginController.login(credencialesValidas);

        // Verificaciones
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        
        assertNotNull(responseBody);
        assertEquals("Credenciales inválidas", responseBody.get("mensaje"));

        // Verificar que se llamó a ambos servicios
        verify(usuarioService).findByEmail("juan.perez@email.com");
        verify(passwordEncoder).matches("123456", "$2a$10$encryptedPassword");
    }

    @Test
    void testLoginConEmailConEspacios() {
        // Email con espacios
        credencialesValidas.put("email", "  juan.perez@email.com  ");

        // Configuración del mock
        when(usuarioService.findByEmail("juan.perez@email.com")).thenReturn(usuarioMock);
        when(passwordEncoder.matches("123456", "$2a$10$encryptedPassword")).thenReturn(true);

        // Ejecución
        ResponseEntity<?> response = loginController.login(credencialesValidas);

        // Verificaciones
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        // Verificar que se limpió el email correctamente
        verify(usuarioService).findByEmail("juan.perez@email.com");
    }

    @Test
    void testLoginUsuarioAdmin() {
        // Usuario administrador
        usuarioMock.setRol("ADMIN");

        // Configuración del mock
        when(usuarioService.findByEmail("juan.perez@email.com")).thenReturn(usuarioMock);
        when(passwordEncoder.matches("123456", "$2a$10$encryptedPassword")).thenReturn(true);

        // Ejecución
        ResponseEntity<?> response = loginController.login(credencialesValidas);

        // Verificaciones
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        
        assertNotNull(responseBody);
        assertEquals("ADMIN", responseBody.get("rol"));
    }

    @Test
    void testLoginErrorInterno() {
        // Configuración del mock - excepción en el servicio
        when(usuarioService.findByEmail("juan.perez@email.com")).thenThrow(new RuntimeException("Error de base de datos"));

        // Ejecución
        ResponseEntity<?> response = loginController.login(credencialesValidas);

        // Verificaciones
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        
        assertNotNull(responseBody);
        assertEquals("Error interno del servidor", responseBody.get("mensaje"));
        assertNotNull(responseBody.get("error"));
    }
} 