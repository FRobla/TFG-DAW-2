SET SQL_SAFE_UPDATES = 0;

-- Eliminar datos existentes para evitar duplicados
DELETE FROM anuncio_categoria;
DELETE FROM anuncios;
DELETE FROM usuarios;
DELETE FROM categorias;
DELETE FROM impresoras;

-- Restablecer los auto_increment
-- ALTER TABLE usuarios AUTO_INCREMENT = 1;
-- ALTER TABLE categorias AUTO_INCREMENT = 1;
-- ALTER TABLE impresoras AUTO_INCREMENT = 1;
-- ALTER TABLE anuncios AUTO_INCREMENT = 1;

-- Insertar Usuarios (ADMIN y USER) con IDs específicos
INSERT INTO usuarios (id, nombre, apellido, email, password, direccion, foto, rol, fecha_registro) VALUES 
(1, 'Admin', 'Principal', 'admin@proyecto3d.com', '$2a$10$rJSdH5Qg/4S6L/J4uP/qUO.eKGtLBnROGMXNCCxnKLcX1vBcGEIxC', 'Calle Admin 123', NULL, 'ADMIN', '2025-05-01');
INSERT INTO usuarios (id, nombre, apellido, email, password, direccion, foto, rol, fecha_registro) VALUES 
(2, 'María', 'López', 'maria@gmail.com', '$2a$10$rJSdH5Qg/4S6L/J4uP/qUO.eKGtLBnROGMXNCCxnKLcX1vBcGEIxC', 'Calle Principal 45', NULL, 'USER', '2025-05-02');
INSERT INTO usuarios (id, nombre, apellido, email, password, direccion, foto, rol, fecha_registro) VALUES 
(3, 'Juan', 'Pérez', 'juan@gmail.com', '$2a$10$rJSdH5Qg/4S6L/J4uP/qUO.eKGtLBnROGMXNCCxnKLcX1vBcGEIxC', 'Avenida Central 78', NULL, 'USER', '2025-05-03');
INSERT INTO usuarios (id, nombre, apellido, email, password, direccion, foto, rol, fecha_registro) VALUES 
(4, 'Laura', 'Martínez', 'laura@gmail.com', '$2a$10$rJSdH5Qg/4S6L/J4uP/qUO.eKGtLBnROGMXNCCxnKLcX1vBcGEIxC', 'Plaza Mayor 12', NULL, 'USER', '2025-05-04');

-- Insertar Categorías
INSERT INTO categorias (id, nombre, descripcion) VALUES (1, 'Impresión FDM', 'Impresión 3D por extrusión de filamento termoplástico capa por capa');
INSERT INTO categorias (id, nombre, descripcion) VALUES (2, 'Modelado 3D', 'Creación digital de modelos tridimensionales mediante software CAD');
INSERT INTO categorias (id, nombre, descripcion) VALUES (3, 'Escaneado 3D', 'Captura tridimensional de objetos físicos para su reproducción o análisis');
INSERT INTO categorias (id, nombre, descripcion) VALUES (4, 'Consultoría', 'Asesoría técnica y estratégica en proyectos de impresión 3D');
INSERT INTO categorias (id, nombre, descripcion) VALUES (5, 'Producción en serie', 'Fabricación de múltiples copias de una pieza o producto en 3D');
INSERT INTO categorias (id, nombre, descripcion) VALUES (6, 'Impresión FDM/Resina', 'Servicio combinado de impresión FDM y resina según requerimientos');
INSERT INTO categorias (id, nombre, descripcion) VALUES (7, 'Prototipos médicos', 'Desarrollo de modelos médicos personalizados o funcionales');
INSERT INTO categorias (id, nombre, descripcion) VALUES (8, 'Maquetas arquitectónicas', 'Construcción de modelos arquitectónicos detallados en 3D');
INSERT INTO categorias (id, nombre, descripcion) VALUES (9, 'Personalización', 'Modificación o adaptación de objetos a medida del cliente');
INSERT INTO categorias (id, nombre, descripcion) VALUES (10, 'Educación y talleres', 'Capacitación y formación práctica en tecnologías 3D');

-- Insertar Impresoras
INSERT INTO impresoras (id, modelo, marca, tecnologia, volumen_impresion_x, volumen_impresion_y, volumen_impresion_z, valor_precision, descripcion, imagen) 
VALUES (1, 'Ender 3 Pro', 'Creality', 'FDM', 220.0, 220.0, 250.0, 0.1, 'Impresora 3D FDM de alta calidad para principiantes y aficionados', NULL);
INSERT INTO impresoras (id, modelo, marca, tecnologia, volumen_impresion_x, volumen_impresion_y, volumen_impresion_z, valor_precision, descripcion, imagen) 
VALUES (2, 'Prusa i3 MK3S+', 'Prusa Research', 'FDM', 250.0, 210.0, 210.0, 0.05, 'Una de las mejores impresoras FDM del mercado con excelente calidad de impresión', NULL);
INSERT INTO impresoras (id, modelo, marca, tecnologia, volumen_impresion_x, volumen_impresion_y, volumen_impresion_z, valor_precision, descripcion, imagen) 
VALUES (3, 'Form 3', 'Formlabs', 'SLA', 145.0, 145.0, 185.0, 0.025, 'Impresora SLA de resina con alta precisión', NULL);
INSERT INTO impresoras (id, modelo, marca, tecnologia, volumen_impresion_x, volumen_impresion_y, volumen_impresion_z, valor_precision, descripcion, imagen) 
VALUES (4, 'Anycubic Photon Mono X', 'Anycubic', 'MSLA', 192.0, 120.0, 245.0, 0.01, 'Impresora de resina MSLA con gran volumen de impresión', NULL);
INSERT INTO impresoras (id, modelo, marca, tecnologia, volumen_impresion_x, volumen_impresion_y, volumen_impresion_z, valor_precision, descripcion, imagen) 
VALUES (5, 'Ultimaker S5', 'Ultimaker', 'FDM', 330.0, 240.0, 300.0, 0.02, 'Impresora profesional con doble extrusor y gran volumen', NULL);

-- Insertar Anuncios
INSERT INTO anuncios (id, titulo, descripcion, estado, imagen, precio_base, tiempo_estimado, fecha_publicacion, vistas, usuario_id, impresora_id) 
VALUES (1, 'Figura de Yoda personalizada', 'Figura de Yoda de Star Wars personalizable en tamaño y color', 'activo', NULL, 25.99, '3-5 días', '2025-05-01', 42, 2, 1);
INSERT INTO anuncios (id, titulo, descripcion, estado, imagen, precio_base, tiempo_estimado, fecha_publicacion, vistas, usuario_id, impresora_id) 
VALUES (2, 'Soporte para móvil', 'Soporte estable para smartphone con diseño minimalista', 'activo', NULL, 12.50, '1-2 días', '2025-05-02', 27, 3, 2);
INSERT INTO anuncios (id, titulo, descripcion, estado, imagen, precio_base, tiempo_estimado, fecha_publicacion, vistas, usuario_id, impresora_id) 
VALUES (3, 'Maceta inteligente', 'Maceta con sistema de autorriego para plantas pequeñas', 'reservado', NULL, 18.75, '2-3 días', '2025-05-03', 35, 4, 5);
INSERT INTO anuncios (id, titulo, descripcion, estado, imagen, precio_base, tiempo_estimado, fecha_publicacion, vistas, usuario_id, impresora_id) 
VALUES (4, 'Engranajes personalizados', 'Juego de engranajes personalizados para proyectos mecánicos', 'activo', NULL, 30.00, '3-4 días', '2025-05-03', 19, 2, 3);
INSERT INTO anuncios (id, titulo, descripcion, estado, imagen, precio_base, tiempo_estimado, fecha_publicacion, vistas, usuario_id, impresora_id) 
VALUES (5, 'Organizador de escritorio', 'Organizador modular para mantener tu escritorio ordenado', 'vendido', NULL, 22.49, '2 días', '2025-05-04', 53, 3, 1);
INSERT INTO anuncios (id, titulo, descripcion, estado, imagen, precio_base, tiempo_estimado, fecha_publicacion, vistas, usuario_id, impresora_id) 
VALUES (6, 'Modelo anatómico', 'Modelo educativo del cuerpo humano para estudiantes', 'activo', NULL, 45.99, '5-7 días', '2025-05-04', 31, 4, 4);
INSERT INTO anuncios (id, titulo, descripcion, estado, imagen, precio_base, tiempo_estimado, fecha_publicacion, vistas, usuario_id, impresora_id) 
VALUES (7, 'Caja para herramientas', 'Caja personalizable para organizar herramientas pequeñas', 'activo', NULL, 15.50, '1-2 días', '2025-05-05', 12, 2, 2);
INSERT INTO anuncios (id, titulo, descripcion, estado, imagen, precio_base, tiempo_estimado, fecha_publicacion, vistas, usuario_id, impresora_id) 
VALUES (8, 'Maqueta arquitectónica', 'Maqueta personalizada de edificios y estructuras', 'activo', NULL, 80.00, '7-10 días', '2025-05-05', 8, 3, 5);

-- Relaciones entre Anuncios y Categorías
INSERT INTO anuncio_categoria (anuncio_id, categorias) VALUES (1, 9); -- Figura de Yoda - Personalización
INSERT INTO anuncio_categoria (anuncio_id, categorias) VALUES (1, 1); -- Figura de Yoda - Impresión FDM
INSERT INTO anuncio_categoria (anuncio_id, categorias) VALUES (2, 1); -- Soporte para móvil - Impresión FDM
INSERT INTO anuncio_categoria (anuncio_id, categorias) VALUES (2, 9); -- Soporte para móvil - Personalización
INSERT INTO anuncio_categoria (anuncio_id, categorias) VALUES (3, 1); -- Maceta inteligente - Impresión FDM
INSERT INTO anuncio_categoria (anuncio_id, categorias) VALUES (3, 9); -- Maceta inteligente - Personalización
INSERT INTO anuncio_categoria (anuncio_id, categorias) VALUES (4, 2); -- Engranajes personalizados - Modelado 3D
INSERT INTO anuncio_categoria (anuncio_id, categorias) VALUES (4, 5); -- Engranajes personalizados - Producción en serie
INSERT INTO anuncio_categoria (anuncio_id, categorias) VALUES (5, 1); -- Organizador de escritorio - Impresión FDM
INSERT INTO anuncio_categoria (anuncio_id, categorias) VALUES (6, 7); -- Modelo anatómico - Prototipos médicos
INSERT INTO anuncio_categoria (anuncio_id, categorias) VALUES (6, 10); -- Modelo anatómico - Educación y talleres
INSERT INTO anuncio_categoria (anuncio_id, categorias) VALUES (7, 1); -- Caja para herramientas - Impresión FDM
INSERT INTO anuncio_categoria (anuncio_id, categorias) VALUES (7, 9); -- Caja para herramientas - Personalización
INSERT INTO anuncio_categoria (anuncio_id, categorias) VALUES (8, 8); -- Maqueta arquitectónica - Maquetas arquitectónicas
INSERT INTO anuncio_categoria (anuncio_id, categorias) VALUES (8, 2); -- Maqueta arquitectónica - Modelado 3D

SET SQL_SAFE_UPDATES = 1;
