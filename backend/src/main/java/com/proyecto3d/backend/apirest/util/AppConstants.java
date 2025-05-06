package com.proyecto3d.backend.apirest.util;

/**
 * Clase que contiene constantes utilizadas en la aplicación
 */
public class AppConstants {
    
    // Constantes para estados de anuncios
    public static final String ESTADO_ANUNCIO_ACTIVO = "activo";
    public static final String ESTADO_ANUNCIO_RESERVADO = "reservado";
    public static final String ESTADO_ANUNCIO_VENDIDO = "vendido";
    
    // Constantes para roles de usuarios
    public static final String ROL_ADMIN = "ADMIN";
    public static final String ROL_USER = "USER";
    
    // Constantes para paginación
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "asc";
    
    // Configuración de la aplicación
    public static final String API_BASE_PATH = "/api";
    public static final String FRONTEND_URL = "http://localhost:4200";
    
    // Mensajes de error comunes
    public static final String ERROR_DB_CONSULTA = "Error al realizar la consulta en la base de datos";
    public static final String ERROR_DB_INSERTAR = "Error al insertar en la base de datos";
    public static final String ERROR_DB_ACTUALIZAR = "Error al actualizar en la base de datos";
    public static final String ERROR_DB_ELIMINAR = "Error al eliminar de la base de datos";
    
    // Mensajes de éxito comunes
    public static final String EXITO_CREAR = "El recurso ha sido creado con éxito";
    public static final String EXITO_ACTUALIZAR = "El recurso ha sido actualizado con éxito";
    public static final String EXITO_ELIMINAR = "El recurso ha sido eliminado con éxito";
    
    // Evitar instanciación
    private AppConstants() {
        throw new IllegalStateException("Clase de utilidad");
    }
} 