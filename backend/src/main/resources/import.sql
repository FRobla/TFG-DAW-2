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

-- Insertar Ubicaciones - Ciudades principales de España
INSERT INTO ubicaciones (id, nombre, provincia, comunidad_autonoma, codigo_postal, activo) VALUES 
-- Andalucía
(1, 'Sevilla', 'Sevilla', 'Andalucía', '41001', true),
(2, 'Málaga', 'Málaga', 'Andalucía', '29001', true),
(3, 'Córdoba', 'Córdoba', 'Andalucía', '14001', true),
(4, 'Granada', 'Granada', 'Andalucía', '18001', true),
(5, 'Cádiz', 'Cádiz', 'Andalucía', '11001', true),
(6, 'Almería', 'Almería', 'Andalucía', '04001', true),
(7, 'Huelva', 'Huelva', 'Andalucía', '21001', true),
(8, 'Jaén', 'Jaén', 'Andalucía', '23001', true),

-- Aragón
(9, 'Zaragoza', 'Zaragoza', 'Aragón', '50001', true),
(10, 'Huesca', 'Huesca', 'Aragón', '22001', true),
(11, 'Teruel', 'Teruel', 'Aragón', '44001', true),

-- Asturias
(12, 'Oviedo', 'Asturias', 'Principado de Asturias', '33001', true),
(13, 'Gijón', 'Asturias', 'Principado de Asturias', '33201', true),

-- Baleares
(14, 'Palma', 'Baleares', 'Islas Baleares', '07001', true),

-- Canarias
(15, 'Las Palmas de Gran Canaria', 'Las Palmas', 'Canarias', '35001', true),
(16, 'Santa Cruz de Tenerife', 'Santa Cruz de Tenerife', 'Canarias', '38001', true),

-- Cantabria
(17, 'Santander', 'Cantabria', 'Cantabria', '39001', true),

-- Castilla-La Mancha
(18, 'Toledo', 'Toledo', 'Castilla-La Mancha', '45001', true),
(19, 'Ciudad Real', 'Ciudad Real', 'Castilla-La Mancha', '13001', true),
(20, 'Albacete', 'Albacete', 'Castilla-La Mancha', '02001', true),
(21, 'Cuenca', 'Cuenca', 'Castilla-La Mancha', '16001', true),
(22, 'Guadalajara', 'Guadalajara', 'Castilla-La Mancha', '19001', true),

-- Castilla y León
(23, 'Valladolid', 'Valladolid', 'Castilla y León', '47001', true),
(24, 'León', 'León', 'Castilla y León', '24001', true),
(25, 'Burgos', 'Burgos', 'Castilla y León', '09001', true),
(26, 'Salamanca', 'Salamanca', 'Castilla y León', '37001', true),
(27, 'Palencia', 'Palencia', 'Castilla y León', '34001', true),
(28, 'Zamora', 'Zamora', 'Castilla y León', '49001', true),
(29, 'Ávila', 'Ávila', 'Castilla y León', '05001', true),
(30, 'Segovia', 'Segovia', 'Castilla y León', '40001', true),
(31, 'Soria', 'Soria', 'Castilla y León', '42001', true),

-- Cataluña
(32, 'Barcelona', 'Barcelona', 'Cataluña', '08001', true),
(33, 'Lleida', 'Lleida', 'Cataluña', '25001', true),
(34, 'Girona', 'Girona', 'Cataluña', '17001', true),
(35, 'Tarragona', 'Tarragona', 'Cataluña', '43001', true),

-- Comunidad Valenciana
(36, 'Valencia', 'Valencia', 'Comunidad Valenciana', '46001', true),
(37, 'Alicante', 'Alicante', 'Comunidad Valenciana', '03001', true),
(38, 'Castellón', 'Castellón', 'Comunidad Valenciana', '12001', true),

-- Extremadura
(39, 'Mérida', 'Badajoz', 'Extremadura', '06800', true),
(40, 'Badajoz', 'Badajoz', 'Extremadura', '06001', true),
(41, 'Cáceres', 'Cáceres', 'Extremadura', '10001', true),

-- Galicia
(42, 'Santiago de Compostela', 'A Coruña', 'Galicia', '15701', true),
(43, 'A Coruña', 'A Coruña', 'Galicia', '15001', true),
(44, 'Vigo', 'Pontevedra', 'Galicia', '36201', true),
(45, 'Pontevedra', 'Pontevedra', 'Galicia', '36001', true),
(46, 'Lugo', 'Lugo', 'Galicia', '27001', true),
(47, 'Ourense', 'Ourense', 'Galicia', '32001', true),

-- La Rioja
(48, 'Logroño', 'La Rioja', 'La Rioja', '26001', true),

-- Madrid
(49, 'Madrid', 'Madrid', 'Comunidad de Madrid', '28001', true),

-- Murcia
(50, 'Murcia', 'Murcia', 'Región de Murcia', '30001', true),

-- Navarra
(51, 'Pamplona', 'Navarra', 'Comunidad Foral de Navarra', '31001', true),

-- País Vasco
(52, 'Vitoria-Gasteiz', 'Álava', 'País Vasco', '01001', true),
(53, 'Bilbao', 'Vizcaya', 'País Vasco', '48001', true),
(54, 'San Sebastián', 'Guipúzcoa', 'País Vasco', '20001', true),

-- Ceuta y Melilla
(55, 'Ceuta', 'Ceuta', 'Ciudad Autónoma de Ceuta', '51001', true),
(56, 'Melilla', 'Melilla', 'Ciudad Autónoma de Melilla', '52001', true);

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

-- Insertar Usuarios (ADMIN y USER) con ubicaciones españolas distribuidas
INSERT INTO usuarios (id, nombre, apellido, email, password, direccion, foto, rol, fecha_registro, ubicacion_id) VALUES 
(1, 'Admin', 'Principal', 'admin@admin.com', '$2a$10$sYgf2E/5PdNVlySBkSqDj.DgPCFeSbfVaECrTJY7eglsFQgZGzWcG', 'Calle Mayor 123, Madrid', NULL, 'ADMIN', '2025-05-01', 49),
(2, 'TechPrint', 'Barcelona', 'techprint@barcelona.com', '$2a$10$sYgf2E/5PdNVlySBkSqDj.DgPCFeSbfVaECrTJY7eglsFQgZGzWcG', 'Passeig de Gràcia 85, Barcelona', NULL, 'USER', '2025-05-02', 32),
(3, '3DExpress', 'Valencia', '3dexpress@valencia.com', '$2a$10$sYgf2E/5PdNVlySBkSqDj.DgPCFeSbfVaECrTJY7eglsFQgZGzWcG', 'Calle Colón 24, Valencia', NULL, 'USER', '2025-05-03', 36),
(4, 'EcoMakers', 'Sevilla', 'ecomakers@sevilla.com', '$2a$10$rJSdH5Qg/4S6L/J4uP/qUO.eKGtLBnROGMXNCCxnKLcX1vBcGEIxC', 'Avenida de la Constitución 18, Sevilla', NULL, 'USER', '2025-05-04', 1),
(5, 'ColorPrint', 'Bilbao', 'colorprint@bilbao.com', '$2a$10$rJSdH5Qg/4S6L/J4uP/qUO.eKGtLBnROGMXNCCxnKLcX1vBcGEIxC', 'Gran Vía 8, Bilbao', NULL, 'USER', '2025-05-05', 53),
(6, 'ProMakers', 'Zaragoza', 'promakers@zaragoza.com', '$2a$10$rJSdH5Qg/4S6L/J4uP/qUO.eKGtLBnROGMXNCCxnKLcX1vBcGEIxC', 'Calle Alfonso I, 45, Zaragoza', NULL, 'USER', '2025-05-06', 9),
(7, 'TechSolutions', 'Valladolid', 'techsolutions@valladolid.com', '$2a$10$rJSdH5Qg/4S6L/J4uP/qUO.eKGtLBnROGMXNCCxnKLcX1vBcGEIxC', 'Plaza Mayor 67, Valladolid', NULL, 'USER', '2025-05-07', 23),
(8, 'PrintStarters', 'Málaga', 'printstarters@malaga.com', '$2a$10$rJSdH5Qg/4S6L/J4uP/qUO.eKGtLBnROGMXNCCxnKLcX1vBcGEIxC', 'Calle Larios 12, Málaga', NULL, 'USER', '2025-05-08', 2),
(9, 'IndustrialPrint', 'Murcia', 'industrialprint@murcia.com', '$2a$10$rJSdH5Qg/4S6L/J4uP/qUO.eKGtLBnROGMXNCCxnKLcX1vBcGEIxC', 'Gran Vía Alfonso X El Sabio 89, Murcia', NULL, 'USER', '2025-05-09', 50),
(10, 'NylonTech', 'Las Palmas', 'nylontech@laspalmas.com', '$2a$10$rJSdH5Qg/4S6L/J4uP/qUO.eKGtLBnROGMXNCCxnKLcX1vBcGEIxC', 'Calle Triana 34, Las Palmas de Gran Canaria', NULL, 'USER', '2025-05-10', 15);

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
INSERT INTO anuncio_material (anuncio_id, material_id) VALUES (5, 2); -- Impresión FDM industrial