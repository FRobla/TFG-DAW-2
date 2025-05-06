# Backend Proyecto 3D

Este repositorio contiene el backend para la aplicación Proyecto 3D, desarrollado con Spring Boot 3.2.12 y Java 17.

## Requisitos previos

- Java JDK 17 o superior
- Maven 3.8.x o superior
- MySQL 8.0 o superior

## Configuración

1. Clona este repositorio

2. Configura la base de datos MySQL:
   - Crea una base de datos llamada `proyecto3d`
   - Asegúrate de que el usuario y contraseña de MySQL coincidan con los configurados en `application.properties`

3. Configuración del correo electrónico:
   - Actualiza las propiedades de correo electrónico en `application.properties` con tus credenciales

## Ejecución

Para ejecutar la aplicación:

```bash
# Navegar al directorio del proyecto
cd backend

# Compilar el proyecto
mvn clean package

# Ejecutar la aplicación
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

## Endpoints API

La API REST expone los siguientes endpoints principales:

- `/api/anuncios` - Gestión de anuncios
- `/api/usuarios` - Gestión de usuarios
- `/api/categorias` - Gestión de categorías
- `/api/impresoras` - Gestión de impresoras

## Características

- API REST completa para la gestión de anuncios, usuarios, impresoras y categorías
- Integración con base de datos MySQL
- Validación de datos
- Configuración de seguridad (actualmente con acceso público a la API para facilitar el desarrollo)
- Configuración de CORS para permitir la comunicación con el frontend Angular

## Integración con el Frontend

Este backend está diseñado para funcionar con el frontend Angular que se encuentra en el directorio `../frontend`.
Para una correcta integración:

1. Ejecuta el backend en el puerto 8080
2. Ejecuta el frontend Angular en el puerto 4200
3. Asegúrate de que la configuración CORS permite la comunicación entre ambos

## Estructura del proyecto

- `config` - Configuración de seguridad y otras configuraciones
- `controller` - Controladores REST
- `model` - Entidades, DAOs y servicios
- `util` - Clases de utilidad 