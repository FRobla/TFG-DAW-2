SET SQL_SAFE_UPDATES = 0;

-- Eliminar datos existentes para evitar duplicados
DELETE FROM valoraciones;
DELETE FROM favoritos;
DELETE FROM anuncio_material;
DELETE FROM anuncio_categoria;
DELETE FROM anuncios;
DELETE FROM usuarios;
DELETE FROM categorias;
DELETE FROM impresoras;
DELETE FROM materiales;
DELETE FROM ubicaciones;

-- Restablecer los auto_increment (comentado para mantener IDs específicos)
-- ALTER TABLE usuarios AUTO_INCREMENT = 1;
-- ALTER TABLE categorias AUTO_INCREMENT = 1;
-- ALTER TABLE impresoras AUTO_INCREMENT = 1;
-- ALTER TABLE anuncios AUTO_INCREMENT = 1;
-- ALTER TABLE materiales AUTO_INCREMENT = 1;
-- ALTER TABLE ubicaciones AUTO_INCREMENT = 1;

-- Insertar Ubicaciones
INSERT INTO ubicaciones (id, nombre, provincia, comunidad_autonoma, codigo_postal, activo) VALUES 
(1, 'Madrid', 'Madrid', 'Comunidad de Madrid', '28001', true),
(2, 'Barcelona', 'Barcelona', 'Cataluña', '08001', true),
(3, 'Valencia', 'Valencia', 'Comunidad Valenciana', '46001', true),
(4, 'Sevilla', 'Sevilla', 'Andalucía', '41001', true),
(5, 'Bilbao', 'Vizcaya', 'País Vasco', '48001', true),
(6, 'Zaragoza', 'Zaragoza', 'Aragón', '50001', true),
(7, 'Málaga', 'Málaga', 'Andalucía', '29001', true),
(8, 'Murcia', 'Murcia', 'Región de Murcia', '30001', true),
(9, 'Las Palmas', 'Las Palmas', 'Canarias', '35001', true),
(10, 'Valladolid', 'Valladolid', 'Castilla y León', '47001', true);

-- Insertar Materiales
INSERT INTO materiales (id, nombre, descripcion, propiedades, color_disponibles, precio_kg, activo) VALUES 
(1, 'PLA', 'Plástico biodegradable fácil de imprimir', 'Biodegradable, baja temperatura, fácil de usar', 'Blanco, Negro, Rojo, Azul, Verde, Amarillo, Transparente', 20.50, true),
(2, 'ABS', 'Plástico resistente para piezas funcionales', 'Resistente al calor, duradero, flexible', 'Negro, Blanco, Gris, Rojo, Azul', 22.00, true),
(3, 'PETG', 'Plástico transparente con buena resistencia química', 'Transparente, resistente químicamente, fácil impresión', 'Transparente, Blanco, Negro, Azul', 24.00, true),
(4, 'TPU', 'Material flexible para piezas elásticas', 'Flexible, elástico, resistente abrasión', 'Negro, Blanco, Rojo, Azul, Transparente', 45.00, true),
(5, 'Wood PLA', 'PLA con fibras de madera real', 'Biodegradable, olor a madera, post-procesable', 'Madera natural, Nogal, Bambú', 35.00, true),
(6, 'Carbon Fiber', 'Material reforzado con fibra de carbono', 'Muy resistente, ligero, conductivo', 'Negro', 80.00, true),
(7, 'Resina Standard', 'Resina fotopolímera estándar', 'Alta precisión, superficie lisa, detalle fino', 'Gris, Blanco, Negro, Transparente', 50.00, true),
(8, 'Resina Flexible', 'Resina flexible para piezas elásticas', 'Flexible, resistente, alta precisión', 'Transparente, Negro, Blanco', 65.00, true),
(9, 'Nylon', 'Material industrial de alta resistencia', 'Muy resistente, duradero, alta temperatura', 'Natural, Negro, Blanco', 60.00, true),
(10, 'Metal Fill', 'PLA con partículas metálicas', 'Pesado, post-procesable, aspecto metálico', 'Cobre, Bronce, Acero', 75.00, true);

-- Insertar Usuarios (ADMIN y USER) con ubicaciones
INSERT INTO usuarios (id, nombre, apellido, email, password, direccion, foto, rol, fecha_registro, ubicacion_id) VALUES 
(1, 'Admin', 'Principal', 'admin@admin.com', '$2a$10$sYgf2E/5PdNVlySBkSqDj.DgPCFeSbfVaECrTJY7eglsFQgZGzWcG', 'Calle Admin 123', NULL, 'ADMIN', '2025-05-01', 1);
INSERT INTO usuarios (id, nombre, apellido, email, password, direccion, foto, rol, fecha_registro, ubicacion_id) VALUES 
(2, 'TechPrint', 'Madrid', 'techprint@madrid.com', '$2a$10$sYgf2E/5PdNVlySBkSqDj.DgPCFeSbfVaECrTJY7eglsFQgZGzWcG', 'Calle Tecnología 15, Madrid', NULL, 'USER', '2025-05-02', 1);
INSERT INTO usuarios (id, nombre, apellido, email, password, direccion, foto, rol, fecha_registro, ubicacion_id) VALUES 
(3, '3DExpress', 'Barcelona', '3dexpress@barcelona.com', '$2a$10$sYgf2E/5PdNVlySBkSqDj.DgPCFeSbfVaECrTJY7eglsFQgZGzWcG', 'Avenida Rápida 24, Barcelona', NULL, 'USER', '2025-05-03', 2);
INSERT INTO usuarios (id, nombre, apellido, email, password, direccion, foto, rol, fecha_registro, ubicacion_id) VALUES 
(4, 'EcoMakers', 'Valencia', 'ecomakers@valencia.com', '$2a$10$rJSdH5Qg/4S6L/J4uP/qUO.eKGtLBnROGMXNCCxnKLcX1vBcGEIxC', 'Calle Verde 18, Valencia', NULL, 'USER', '2025-05-04', 3);
INSERT INTO usuarios (id, nombre, apellido, email, password, direccion, foto, rol, fecha_registro, ubicacion_id) VALUES 
(5, 'ColorPrint', 'Sevilla', 'colorprint@sevilla.com', '$2a$10$rJSdH5Qg/4S6L/J4uP/qUO.eKGtLBnROGMXNCCxnKLcX1vBcGEIxC', 'Plaza Color 8, Sevilla', NULL, 'USER', '2025-05-05', 4);
INSERT INTO usuarios (id, nombre, apellido, email, password, direccion, foto, rol, fecha_registro, ubicacion_id) VALUES 
(6, 'ProMakers', 'Bilbao', 'promakers@bilbao.com', '$2a$10$rJSdH5Qg/4S6L/J4uP/qUO.eKGtLBnROGMXNCCxnKLcX1vBcGEIxC', 'Calle Industrial 45, Bilbao', NULL, 'USER', '2025-05-06', 5);
INSERT INTO usuarios (id, nombre, apellido, email, password, direccion, foto, rol, fecha_registro, ubicacion_id) VALUES 
(7, 'TechSolutions', 'Valladolid', 'techsolutions@zaragoza.com', '$2a$10$rJSdH5Qg/4S6L/J4uP/qUO.eKGtLBnROGMXNCCxnKLcX1vBcGEIxC', 'Avenida Soluciones 67, Valladolid', NULL, 'USER', '2025-05-07', 10);
INSERT INTO usuarios (id, nombre, apellido, email, password, direccion, foto, rol, fecha_registro, ubicacion_id) VALUES 
(8, 'PrintStarters', 'Málaga', 'printstarters@malaga.com', '$2a$10$rJSdH5Qg/4S6L/J4uP/qUO.eKGtLBnROGMXNCCxnKLcX1vBcGEIxC', 'Calle Inicio 12, Málaga', NULL, 'USER', '2025-05-08', 7);
INSERT INTO usuarios (id, nombre, apellido, email, password, direccion, foto, rol, fecha_registro, ubicacion_id) VALUES 
(9, 'IndustrialPrint', 'Murcia', 'industrialprint@murcia.com', '$2a$10$rJSdH5Qg/4S6L/J4uP/qUO.eKGtLBnROGMXNCCxnKLcX1vBcGEIxC', 'Polígono Industrial 89, Murcia', NULL, 'USER', '2025-05-09', 8);
INSERT INTO usuarios (id, nombre, apellido, email, password, direccion, foto, rol, fecha_registro, ubicacion_id) VALUES 
(10, 'NylonTech', 'Las Palmas', 'nylontech@laspalmas.com', '$2a$10$rJSdH5Qg/4S6L/J4uP/qUO.eKGtLBnROGMXNCCxnKLcX1vBcGEIxC', 'Calle Tech 34, Las Palmas', NULL, 'USER', '2025-05-10', 9);

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

-- Insertar Anuncios con valoración media calculada
INSERT INTO anuncios (id, titulo, descripcion, estado, imagen, precio_base, tiempo_estimado, fecha_publicacion, vistas, valoracion_media, usuario_id, impresora_id) 
VALUES (1, 'Impresión PLA de alta resolución', 'Impresión de prototipos con alta precisión. Material PLA ecológico disponible en diferentes colores.', 'activo', NULL, 15.00, '2-3 días', '2025-05-01', 44, 4.5, 2, 2);

INSERT INTO anuncios (id, titulo, descripcion, estado, imagen, precio_base, tiempo_estimado, fecha_publicacion, vistas, valoracion_media, usuario_id, impresora_id) 
VALUES (2, 'Impresión FDM con acabado premium', 'Servicio rápido de impresión FDM con acabado profesional y tratamiento post-impresión.', 'activo', NULL, 20.00, '24 horas', '2025-05-02', 86, 4.2, 3, 1);

INSERT INTO anuncios (id, titulo, descripcion, estado, imagen, precio_base, tiempo_estimado, fecha_publicacion, vistas, valoracion_media, usuario_id, impresora_id) 
VALUES (3, 'Impresión PLA biodegradable', 'Impresión con materiales 100% biodegradables y ecológicos. Ideal para proyectos sostenibles.', 'activo', NULL, 18.00, '3-4 días', '2025-05-03', 22, 4.8, 4, 2);

INSERT INTO anuncios (id, titulo, descripcion, estado, imagen, precio_base, tiempo_estimado, fecha_publicacion, vistas, valoracion_media, usuario_id, impresora_id) 
VALUES (4, 'Impresión FDM multicolor', 'Impresión con cambios de filamento para crear piezas multicolor en una sola impresión.', 'activo', NULL, 25.00, '4-5 días', '2025-05-04', 49, 4.0, 5, 1);

INSERT INTO anuncios (id, titulo, descripcion, estado, imagen, precio_base, tiempo_estimado, fecha_publicacion, vistas, valoracion_media, usuario_id, impresora_id) 
VALUES (5, 'Impresión FDM industrial', 'Impresión con equipos industriales para piezas de gran formato y alta resistencia.', 'activo', NULL, 40.00, '5-7 días', '2025-05-05', 36, 4.7, 6, 5);

INSERT INTO anuncios (id, titulo, descripcion, estado, imagen, precio_base, tiempo_estimado, fecha_publicacion, vistas, valoracion_media, usuario_id, impresora_id) 
VALUES (6, 'Impresión FDM con PETG', 'Impresión con PETG para piezas resistentes a impactos y temperaturas. Ideal para uso funcional.', 'activo', NULL, 22.00, '3-4 días', '2025-05-06', 14, 4.3, 7, 2);

INSERT INTO anuncios (id, titulo, descripcion, estado, imagen, precio_base, tiempo_estimado, fecha_publicacion, vistas, valoracion_media, usuario_id, impresora_id) 
VALUES (7, 'Pack impresión FDM básica', 'Pack económico para impresiones sencillas. Ideal para prototipos rápidos y económicos.', 'activo', NULL, 12.00, '4-5 días', '2025-05-07', 25, 3.8, 8, 1);

INSERT INTO anuncios (id, titulo, descripcion, estado, imagen, precio_base, tiempo_estimado, fecha_publicacion, vistas, valoracion_media, usuario_id, impresora_id) 
VALUES (8, 'Impresión FDM con ABS', 'Impresión con ABS para piezas resistentes y duraderas. Ideal para componentes mecánicos.', 'activo', NULL, 24.00, '3-4 días', '2025-05-08', 36, 4.1, 9, 1);

INSERT INTO anuncios (id, titulo, descripcion, estado, imagen, precio_base, tiempo_estimado, fecha_publicacion, vistas, valoracion_media, usuario_id, impresora_id) 
VALUES (9, 'Impresión FDM con Nylon', 'Impresión con filamento de nylon para piezas con alta resistencia mecánica y flexibilidad.', 'activo', NULL, 28.00, '4-6 días', '2025-05-09', 32, 4.6, 10, 5);

INSERT INTO anuncios (id, titulo, descripcion, estado, imagen, precio_base, tiempo_estimado, fecha_publicacion, vistas, valoracion_media, usuario_id, impresora_id)
VALUES 
(10, 'Modelado 3D personalizado', 'Servicio de modelado 3D desde cero basado en tus ideas o bocetos.', 'activo', NULL, 30.00, '2-5 días', '2025-05-10', 51, 4.4, 4, 2),
(11, 'Escaneado 3D de objetos físicos', 'Captura de objetos reales con escáner 3D de alta precisión para posterior impresión.', 'activo', NULL, 35.00, '3 días', '2025-05-11', 28, 4.2, 3, 2),
(12, 'Prototipos médicos impresos en resina', 'Fabricación de prototipos médicos con precisión de 25 micras usando tecnología SLA.', 'activo', NULL, 60.00, '5-7 días', '2025-05-12', 32, 4.9, 2, 3),
(13, 'Maquetas arquitectónicas detalladas', 'Maquetas a escala para proyectos arquitectónicos. Detalles finos y acabados de calidad.', 'activo', NULL, 100.00, '7-10 días', '2025-05-13', 19, 4.7, 7, 5),
(14, 'Consultoría técnica en impresión 3D', 'Evaluación de viabilidad, materiales y tecnologías para tus proyectos de impresión 3D.', 'activo', NULL, 50.00, '1-2 días', '2025-05-14', 22, 4.5, 1, 1),
(15, 'Taller de impresión 3D para principiantes', 'Aprende a imprimir en 3D desde cero. Incluye teoría, práctica y certificados.', 'activo', NULL, 20.00, '1 día', '2025-05-15', 51, 4.3, 8, 1);

-- Relaciones entre Anuncios y Categorías
INSERT INTO anuncio_categoria (anuncio_id, categoria_id) VALUES (1, 1); -- Impresión PLA alta resolución - Impresión FDM
INSERT INTO anuncio_categoria (anuncio_id, categoria_id) VALUES (2, 1); -- Impresión FDM acabado premium - Impresión FDM
INSERT INTO anuncio_categoria (anuncio_id, categoria_id) VALUES (3, 1); -- Impresión PLA biodegradable - Impresión FDM
INSERT INTO anuncio_categoria (anuncio_id, categoria_id) VALUES (4, 1); -- Impresión FDM multicolor - Impresión FDM
INSERT INTO anuncio_categoria (anuncio_id, categoria_id) VALUES (4, 9); -- Impresión FDM multicolor - Personalización
INSERT INTO anuncio_categoria (anuncio_id, categoria_id) VALUES (5, 1); -- Impresión FDM industrial - Impresión FDM
INSERT INTO anuncio_categoria (anuncio_id, categoria_id) VALUES (5, 5); -- Impresión FDM industrial - Producción en serie
INSERT INTO anuncio_categoria (anuncio_id, categoria_id) VALUES (6, 1); -- Impresión FDM con PETG - Impresión FDM
INSERT INTO anuncio_categoria (anuncio_id, categoria_id) VALUES (7, 1); -- Pack impresión FDM básica - Impresión FDM
INSERT INTO anuncio_categoria (anuncio_id, categoria_id) VALUES (8, 1); -- Impresión FDM con ABS - Impresión FDM
INSERT INTO anuncio_categoria (anuncio_id, categoria_id) VALUES (9, 1); -- Impresión FDM con Nylon - Impresión FDM
INSERT INTO anuncio_categoria (anuncio_id, categoria_id) VALUES (10, 2); -- Modelado 3D personalizado - Modelado 3D
INSERT INTO anuncio_categoria (anuncio_id, categoria_id) VALUES (10, 9); -- Modelado 3D personalizado - Personalización
INSERT INTO anuncio_categoria (anuncio_id, categoria_id) VALUES (11, 3); -- Escaneado 3D - Escaneado 3D
INSERT INTO anuncio_categoria (anuncio_id, categoria_id) VALUES (12, 6); -- Prototipos médicos - Impresión FDM/Resina
INSERT INTO anuncio_categoria (anuncio_id, categoria_id) VALUES (12, 7); -- Prototipos médicos - Prototipos médicos
INSERT INTO anuncio_categoria (anuncio_id, categoria_id) VALUES (13, 8); -- Maquetas arquitectónicas - Maquetas arquitectónicas
INSERT INTO anuncio_categoria (anuncio_id, categoria_id) VALUES (13, 2); -- Maquetas arquitectónicas - Modelado 3D
INSERT INTO anuncio_categoria (anuncio_id, categoria_id) VALUES (14, 4); -- Consultoría técnica - Consultoría
INSERT INTO anuncio_categoria (anuncio_id, categoria_id) VALUES (15, 10); -- Taller de impresión 3D - Educación y talleres

-- Relaciones entre Anuncios y Materiales
INSERT INTO anuncio_material (anuncio_id, material_id) VALUES (1, 1); -- Impresión PLA alta resolución - PLA
INSERT INTO anuncio_material (anuncio_id, material_id) VALUES (2, 1); -- Impresión FDM acabado premium - PLA
INSERT INTO anuncio_material (anuncio_id, material_id) VALUES (2, 2); -- Impresión FDM acabado premium - ABS
INSERT INTO anuncio_material (anuncio_id, material_id) VALUES (3, 1); -- Impresión PLA biodegradable - PLA
INSERT INTO anuncio_material (anuncio_id, material_id) VALUES (3, 5); -- Impresión PLA biodegradable - Wood PLA
INSERT INTO anuncio_material (anuncio_id, material_id) VALUES (4, 1); -- Impresión FDM multicolor - PLA
INSERT INTO anuncio_material (anuncio_id, material_id) VALUES (4, 5); -- Impresión FDM multicolor - Wood PLA
INSERT INTO anuncio_material (anuncio_id, material_id) VALUES (5, 2); -- Impresión FDM industrial - ABS
INSERT INTO anuncio_material (anuncio_id, material_id) VALUES (5, 9); -- Impresión FDM industrial - Nylon
INSERT INTO anuncio_material (anuncio_id, material_id) VALUES (5, 6); -- Impresión FDM industrial - Carbon Fiber
INSERT INTO anuncio_material (anuncio_id, material_id) VALUES (6, 3); -- Impresión FDM con PETG - PETG
INSERT INTO anuncio_material (anuncio_id, material_id) VALUES (7, 1); -- Pack impresión FDM básica - PLA
INSERT INTO anuncio_material (anuncio_id, material_id) VALUES (8, 2); -- Impresión FDM con ABS - ABS
INSERT INTO anuncio_material (anuncio_id, material_id) VALUES (9, 9); -- Impresión FDM con Nylon - Nylon
INSERT INTO anuncio_material (anuncio_id, material_id) VALUES (10, 1); -- Modelado 3D personalizado - PLA
INSERT INTO anuncio_material (anuncio_id, material_id) VALUES (10, 2); -- Modelado 3D personalizado - ABS
INSERT INTO anuncio_material (anuncio_id, material_id) VALUES (11, 1); -- Escaneado 3D - PLA
INSERT INTO anuncio_material (anuncio_id, material_id) VALUES (12, 7); -- Prototipos médicos - Resina Standard
INSERT INTO anuncio_material (anuncio_id, material_id) VALUES (12, 8); -- Prototipos médicos - Resina Flexible
INSERT INTO anuncio_material (anuncio_id, material_id) VALUES (13, 1); -- Maquetas arquitectónicas - PLA
INSERT INTO anuncio_material (anuncio_id, material_id) VALUES (13, 7); -- Maquetas arquitectónicas - Resina Standard
INSERT INTO anuncio_material (anuncio_id, material_id) VALUES (14, 1); -- Consultoría técnica - PLA (para ejemplos)
INSERT INTO anuncio_material (anuncio_id, material_id) VALUES (15, 1); -- Taller de impresión 3D - PLA

-- Insertar Valoraciones
INSERT INTO valoraciones (id, puntuacion, comentario, fecha_valoracion, respuesta_proveedor, fecha_respuesta, verificado, anuncio_id, usuario_id) VALUES
(1, 5, 'Excelente calidad de impresión, muy recomendable!', '2025-05-16', NULL, NULL, true, 1, 3),
(2, 4, 'Buen servicio, entrega puntual', '2025-05-17', NULL, NULL, true, 1, 4),
(3, 4, 'Muy profesional, acabado perfecto', '2025-05-18', 'Gracias por confiar en nosotros', '2025-05-19', true, 2, 5),
(4, 4, 'Rápido y eficiente', '2025-05-19', NULL, NULL, true, 2, 6),
(5, 5, 'Material ecológico de primera calidad', '2025-05-20', 'Nos alegra que valores nuestro compromiso ecológico', '2025-05-21', true, 3, 7),
(6, 5, 'Perfecta para proyectos sostenibles', '2025-05-21', NULL, NULL, true, 3, 8),
(7, 4, 'Impresión multicolor muy lograda', '2025-05-22', NULL, NULL, true, 4, 9),
(8, 4, 'Colores vibrantes y bien definidos', '2025-05-23', 'Trabajamos con filamentos de alta calidad', '2025-05-24', true, 4, 10),
(9, 5, 'Calidad industrial excepcional', '2025-05-24', 'Perfecto para aplicaciones industriales', '2025-05-25', true, 5, 2),
(10, 4, 'Resistencia comprobada', '2025-05-25', NULL, NULL, true, 5, 3),
(11, 4, 'PETG de alta calidad', '2025-05-26', NULL, NULL, true, 6, 4),
(12, 4, 'Resistente y transparente', '2025-05-27', 'PETG es ideal para estas aplicaciones', '2025-05-28', true, 6, 5),
(13, 4, 'Buena relación calidad-precio', '2025-05-28', NULL, NULL, false, 7, 6),
(14, 3, 'Básico pero cumple', '2025-05-29', 'Entendemos, es nuestro servicio más económico', '2025-05-30', false, 7, 7),
(15, 4, 'ABS de buena calidad', '2025-05-30', NULL, NULL, true, 8, 8),
(16, 4, 'Resistente para piezas funcionales', '2025-05-31', NULL, NULL, true, 8, 9),
(17, 5, 'Nylon excepcional para uso industrial', '2025-06-01', 'Material premium para aplicaciones exigentes', '2025-06-02', true, 9, 10),
(18, 4, 'Muy resistente y duradero', '2025-06-02', NULL, NULL, true, 9, 2),
(19, 4, 'Modelado profesional y detallado', '2025-06-03', NULL, NULL, true, 10, 3),
(20, 4, 'Diseño personalizado perfecto', '2025-06-04', 'Nos especializamos en diseños únicos', '2025-06-05', true, 10, 5),
(21, 4, 'Escaneado de alta precisión', '2025-06-05', NULL, NULL, true, 11, 6),
(22, 4, 'Excelente captura 3D', '2025-06-06', NULL, NULL, true, 11, 7),
(23, 5, 'Prototipos médicos perfectos', '2025-06-07', 'Cumplimos con los estándares médicos más exigentes', '2025-06-08', true, 12, 8),
(24, 5, 'Precisión médica excepcional', '2025-06-08', NULL, NULL, true, 12, 9),
(25, 5, 'Maqueta arquitectónica impresionante', '2025-06-09', 'Cada detalle cuenta en arquitectura', '2025-06-10', true, 13, 10),
(26, 4, 'Detalles arquitectónicos perfectos', '2025-06-10', NULL, NULL, true, 13, 2),
(27, 4, 'Consultoría muy útil', '2025-06-11', NULL, NULL, false, 14, 3),
(28, 5, 'Asesoramiento profesional', '2025-06-12', 'Siempre disponibles para ayudar', '2025-06-13', true, 14, 4),
(29, 4, 'Taller muy educativo', '2025-06-13', NULL, NULL, false, 15, 5),
(30, 4, 'Aprendizaje práctico excelente', '2025-06-14', 'Seguimos mejorando nuestros talleres', '2025-06-15', false, 15, 6);

-- Insertar Favoritos
INSERT INTO favoritos (id, fecha_marcado, anuncio_id, usuario_id) VALUES
(1, '2025-05-16', 1, 3),
(2, '2025-05-17', 1, 4),
(3, '2025-05-18', 2, 5),
(4, '2025-05-19', 3, 6),
(5, '2025-05-20', 3, 7),
(6, '2025-05-21', 5, 8),
(7, '2025-05-22', 5, 9),
(8, '2025-05-23', 12, 10),
(9, '2025-05-24', 12, 2),
(10, '2025-05-25', 13, 3),
(11, '2025-05-26', 9, 4),
(12, '2025-05-27', 10, 5),
(13, '2025-05-28', 14, 6),
(14, '2025-05-29', 15, 7),
(15, '2025-05-30', 6, 8);

SET SQL_SAFE_UPDATES = 1;