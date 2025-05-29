SET SQL_SAFE_UPDATES = 0;

-- Ejemplo de datos para la tabla ubicaciones
INSERT INTO ubicaciones (id, nombre, provincia, comunidad_autonoma, codigo_postal, activo) VALUES
(1, 'Madrid', 'Madrid', 'Comunidad de Madrid', '28001', true),
(2, 'Barcelona', 'Barcelona', 'Cataluña', '08001', true),
(3, 'Valencia', 'Valencia', 'Comunidad Valenciana', '46001', true),
(4, 'Sevilla', 'Sevilla', 'Andalucía', '41001', true),
(5, 'Valladolid', 'Valladolid', 'Castilla y León', '47001', true),
(6, 'Bilbao', 'Vizcaya', 'País Vasco', '48001', true),
(7, 'Zaragoza', 'Zaragoza', 'Aragón', '50001', true),
(8, 'Málaga', 'Málaga', 'Andalucía', '29001', true),
(9, 'Murcia', 'Murcia', 'Región de Murcia', '30001', true),
(10, 'Las Palmas', 'Las Palmas', 'Canarias', '35001', true);

-- Ejemplo de datos para la tabla usuarios (actualizado con ubicaciones)
INSERT INTO usuarios (id, nombre, apellido, direccion, email, password, rol, foto, fecha_registro, ubicacion_id) VALUES
(1, 'Admin', 'Admin', 'Calle Admin', 'admin@mail.com', 'passadmin', 'ADMIN', NULL, '2025-04-01', 1),
(2, 'Juan', 'Pérez', 'Calle Uno', 'juan1@mail.com', 'pass1', 'USER', NULL, '2025-04-01', 1),
(3, 'Ana', 'López', 'Calle Dos', 'ana2@mail.com', 'pass2', 'USER', NULL, '2025-04-02', 2),
(4, 'Luis', 'Martínez', 'Calle Tres', 'luis3@mail.com', 'pass3', 'USER', NULL, '2025-04-03', 3),
(5, 'Marta', 'García', 'Calle Cuatro', 'marta4@mail.com', 'pass4', 'USER', NULL, '2025-04-04', 1),
(6, 'Pedro', 'Sánchez', 'Calle Cinco', 'pedro5@mail.com', 'pass5', 'USER', NULL, '2025-04-05', 2),
(7, 'Lucía', 'Ruiz', 'Calle Seis', 'lucia6@mail.com', 'pass6', 'USER', NULL, '2025-04-06', 4),
(8, 'Carlos', 'Fernández', 'Calle Siete', 'carlos7@mail.com', 'pass7', 'USER', NULL, '2025-04-07', 5),
(9, 'Sara', 'Díaz', 'Calle Ocho', 'sara8@mail.com', 'pass8', 'USER', NULL, '2025-04-08', 6),
(10, 'Elena', 'Moreno', 'Calle Nueve', 'elena9@mail.com', 'pass9', 'USER', NULL, '2025-04-09', 3);

-- Ejemplo de datos para la tabla categorias
INSERT INTO categorias (id, nombre, descripcion) VALUES
(1, 'Impresión FDM', 'Impresión 3D por extrusión de filamento termoplástico capa por capa'),
(2, 'Modelado 3D', 'Creación digital de modelos tridimensionales mediante software CAD'),
(3, 'Escaneado 3D', 'Captura tridimensional de objetos físicos para su reproducción o análisis'),
(4, 'Consultoría', 'Asesoría técnica y estratégica en proyectos de impresión 3D'),
(5, 'Producción en serie', 'Fabricación de múltiples copias de una pieza o producto en 3D'),
(6, 'Impresión FDM/Resina', 'Servicio combinado de impresión FDM y resina según requerimientos'),
(7, 'Prototipos médicos', 'Desarrollo de modelos médicos personalizados o funcionales'),
(8, 'Maquetas arquitectónicas', 'Construcción de modelos arquitectónicos detallados en 3D'),
(9, 'Personalización', 'Modificación o adaptación de objetos a medida del cliente'),
(10, 'Educación y talleres', 'Capacitación y formación práctica en tecnologías 3D');

-- Ejemplo de datos para la tabla materiales (actualizado con nueva estructura)
INSERT INTO materiales (id, nombre, descripcion, propiedades, color_disponibles, precio_kg, activo) VALUES
(1, 'PLA', 'Filamento biodegradable ideal para principiantes', 'Fácil de imprimir, biodegradable, baja temperatura', 'Rojo,Azul,Verde,Negro,Blanco,Amarillo', 20.0, true),
(2, 'ABS', 'Filamento resistente para aplicaciones técnicas', 'Alta resistencia, flexible, soporta altas temperaturas', 'Negro,Blanco,Gris,Azul', 22.0, true),
(3, 'PETG', 'Filamento transparente con buenas propiedades mecánicas', 'Transparente, químicamente resistente, fácil de imprimir', 'Transparente,Azul,Verde,Rojo', 25.0, true),
(4, 'TPU', 'Filamento flexible para aplicaciones elásticas', 'Flexible, resistente al desgaste, elástico', 'Negro,Rojo,Azul,Transparente', 35.0, true),
(5, 'WOOD', 'Filamento con fibra de madera', 'Aspecto y olor a madera, se puede lijar y pintar', 'Madera natural,Roble,Cerezo', 28.0, true),
(6, 'Metal Fill', 'Filamento con partículas metálicas', 'Aspecto metálico, se puede pulir, pesado', 'Cobre,Acero,Bronce', 45.0, true),
(7, 'Carbon Fiber', 'Filamento reforzado con fibra de carbono', 'Ultra resistente, ligero, alta rigidez', 'Negro,Gris', 60.0, true),
(8, 'Resina Standard', 'Resina estándar para impresión SLA/DLP', 'Alta precisión, acabado liso, curado UV', 'Gris,Blanco,Negro,Transparente', 35.0, true),
(9, 'Resina Flexible', 'Resina flexible para aplicaciones elásticas', 'Flexible, resistente a impactos, duradero', 'Negro,Transparente,Rojo', 45.0, true),
(10, 'Resina Dental', 'Resina biocompatible para aplicaciones médicas', 'Biocompatible, alta precisión, certificado médico', 'Blanco,Rosa', 80.0, true);

-- Ejemplo de datos para la tabla autores (impresoras)
INSERT INTO autores (id, nombre, marca, modelo, tipo, volumen_impresion_x, volumen_impresion_y, volumen_impresion_z) VALUES
(1, 'Impresora A', 'Prusa', 'MK3', 'filamento', 250, 210, 200),
(2, 'Impresora B', 'Creality', 'Ender 3', 'filamento', 220, 220, 250),
(3, 'Impresora C', 'Anycubic', 'Photon', 'resina', 115, 65, 155),
(4, 'Impresora D', 'Elegoo', 'Mars', 'resina', 120, 68, 155),
(5, 'Impresora E', 'Bambu', 'X1', 'filamento', 256, 256, 256),
(6, 'Impresora F', 'Sunlu', 'S8', 'filamento', 310, 310, 400),
(7, 'Impresora G', 'Prusa', 'Mini', 'filamento', 180, 180, 180),
(8, 'Impresora H', 'Anycubic', 'Mega S', 'filamento', 210, 210, 205),
(9, 'Impresora I', 'Elegoo', 'Saturn', 'resina', 192, 120, 200),
(10, 'Impresora J', 'Creality', 'CR-10', 'filamento', 300, 300, 400);

-- Ejemplo de datos para la tabla anuncios (actualizado con valoraciones)
DELETE FROM anuncios;

INSERT INTO anuncios (id, titulo, descripcion, estado, imagen, precio_base, tiempo_estimado, fecha_publicacion, usuario_id, impresora_id, vistas, valoracion_media) VALUES
(1, 'Juguete Dinosaurio', 'Figura 3D de dinosaurio personalizable', 'activo', NULL, 10.0, '2-3 días', '2024-04-01', 2, 1, 45, 4.5),
(2, 'Lámpara Decorativa', 'Lámpara moderna impresa en 3D con diseño único', 'activo', NULL, 25.0, '4-7 días', '2024-04-02', 3, 2, 32, 4.2),
(3, 'Soporte Móvil', 'Soporte ajustable para móvil con múltiples ángulos', 'activo', NULL, 8.0, '24 horas', '2024-04-03', 4, 3, 67, 4.8),
(4, 'Caja Organizadora', 'Caja multiusos impresa con compartimentos', 'activo', NULL, 12.0, '2-3 días', '2024-04-04', 5, 4, 23, 3.9),
(5, 'Figura de Ajedrez', 'Pieza de ajedrez personalizada de alta calidad', 'activo', NULL, 5.0, '24 horas', '2024-04-05', 6, 5, 18, 4.7),
(6, 'Pendientes Modernos', 'Pendientes impresos con diseño contemporáneo', 'activo', NULL, 7.0, '24 horas', '2024-04-06', 7, 6, 41, 4.3),
(7, 'Organizador Escritorio', 'Organizador multifunción para oficina', 'activo', NULL, 15.0, '2-3 días', '2024-04-07', 8, 7, 55, 4.6),
(8, 'Escultura Abstracta', 'Escultura decorativa con acabado profesional', 'activo', NULL, 30.0, '4-7 días', '2024-04-08', 9, 8, 29, 4.1),
(9, 'Portabotellas Bicicleta', 'Accesorio resistente para bicicleta', 'activo', NULL, 6.0, '24 horas', '2024-04-09', 10, 9, 73, 4.9),
(10, 'Soporte Portátil', 'Soporte ergonómico para portátil', 'activo', NULL, 18.0, '2-3 días', '2024-04-10', 2, 10, 38, 4.4);

-- Ejemplo de datos para la tabla carritos
INSERT INTO carritos (id, usuario_id, fecha_creacion, estado) VALUES
(1, 1, '2024-04-01', 'activo'),
(2, 2, '2024-04-02', 'activo'),
(3, 3, '2024-04-03', 'activo'),
(4, 4, '2024-04-04', 'activo'),
(5, 5, '2024-04-05', 'activo'),
(6, 6, '2024-04-06', 'activo'),
(7, 7, '2024-04-07', 'activo'),
(8, 8, '2024-04-08', 'activo'),
(9, 9, '2024-04-09', 'activo'),
(10, 10, '2024-04-10', 'activo');

-- Ejemplo de datos para la tabla favoritos (actualizado con nueva estructura)
INSERT INTO favoritos (id, anuncio_id, usuario_id, fecha_marcado) VALUES
(1, 1, 2, '2024-04-10 10:30:00'),
(2, 2, 3, '2024-04-11 14:15:00'),
(3, 3, 4, '2024-04-12 09:45:00'),
(4, 4, 5, '2024-04-13 16:20:00'),
(5, 5, 6, '2024-04-14 11:10:00'),
(6, 6, 7, '2024-04-15 13:25:00'),
(7, 7, 8, '2024-04-16 08:30:00'),
(8, 8, 9, '2024-04-17 15:40:00'),
(9, 9, 10, '2024-04-18 12:05:00'),
(10, 10, 2, '2024-04-19 17:15:00');

-- Ejemplo de datos para la tabla pedidos
INSERT INTO pedidos (id, usuario_id, fecha_pedido, total, direccion_envio, estado) VALUES
(1, 1, '2024-04-01', 50.0, 'Calle Uno', 'pendiente'),
(2, 2, '2024-04-02', 60.0, 'Calle Dos', 'pendiente'),
(3, 3, '2024-04-03', 70.0, 'Calle Tres', 'pendiente'),
(4, 4, '2024-04-04', 80.0, 'Calle Cuatro', 'pendiente'),
(5, 5, '2024-04-05', 90.0, 'Calle Cinco', 'pendiente'),
(6, 6, '2024-04-06', 100.0, 'Calle Seis', 'pendiente'),
(7, 7, '2024-04-07', 110.0, 'Calle Siete', 'pendiente'),
(8, 8, '2024-04-08', 120.0, 'Calle Ocho', 'pendiente'),
(9, 9, '2024-04-09', 130.0, 'Calle Nueve', 'pendiente'),
(10, 10, '2024-04-10', 140.0, 'Calle Diez', 'pendiente');

-- Ejemplo de datos para la tabla detalles_carrito
INSERT INTO detalles_carrito (id, carrito_id, anuncio_id, cantidad, precio_unitario) VALUES
(1, 1, 1, 2, 10.0),
(2, 2, 2, 1, 25.0),
(3, 3, 3, 3, 8.0),
(4, 4, 4, 1, 5.0),
(5, 5, 5, 2, 12.0),
(6, 6, 6, 1, 7.0),
(7, 7, 7, 2, 15.0),
(8, 8, 8, 1, 30.0),
(9, 9, 9, 1, 6.0),
(10, 10, 10, 1, 18.0);

-- Ejemplo de datos para la tabla detalles_pedido
INSERT INTO detalles_pedido (id, pedido_id, anuncio_id, instrucciones, inicio_produccion, fin_produccion, cantidad, precio_unitario) VALUES
(1, 1, 1, 'Ninguna', '2024-04-01', NULL, 2, 10.0),
(2, 2, 2, 'Ninguna', '2024-04-02', NULL, 1, 25.0),
(3, 3, 3, 'Ninguna', '2024-04-03', NULL, 3, 8.0),
(4, 4, 4, 'Ninguna', '2024-04-04', NULL, 1, 5.0),
(5, 5, 5, 'Ninguna', '2024-04-05', NULL, 2, 12.0),
(6, 6, 6, 'Ninguna', '2024-04-06', NULL, 1, 7.0),
(7, 7, 7, 'Ninguna', '2024-04-07', NULL, 2, 15.0),
(8, 8, 8, 'Ninguna', '2024-04-08', NULL, 1, 30.0),
(9, 9, 9, 'Ninguna', '2024-04-09', NULL, 1, 6.0),
(10, 10, 10, 'Ninguna', '2024-04-10', NULL, 1, 18.0);

-- Ejemplo de datos para la tabla valoraciones (actualizado con nueva estructura)
INSERT INTO valoraciones (id, anuncio_id, usuario_id, puntuacion, comentario, fecha_valoracion, verificado) VALUES
(1, 1, 3, 5, 'Excelente trabajo, muy detallado y entregado a tiempo', '2024-04-10 18:30:00', true),
(2, 1, 4, 4, 'Buen resultado aunque tardó un poco más de lo esperado', '2024-04-11 12:45:00', true),
(3, 2, 5, 4, 'Muy buena lámpara, el diseño es precioso', '2024-04-12 14:20:00', true),
(4, 2, 6, 5, 'Perfecta para mi sala de estar, recomendable', '2024-04-13 16:10:00', false),
(5, 3, 7, 5, 'Soporte muy útil y resistente', '2024-04-14 09:15:00', true),
(6, 3, 8, 4, 'Cumple su función perfectamente', '2024-04-15 11:30:00', true),
(7, 4, 9, 4, 'Buena calidad de impresión', '2024-04-16 13:45:00', false),
(8, 5, 10, 5, 'Pieza de ajedrez muy bien acabada', '2024-04-17 15:20:00', true),
(9, 6, 2, 4, 'Pendientes originales y ligeros', '2024-04-18 10:25:00', true),
(10, 7, 3, 5, 'Organizador perfecto para mi escritorio', '2024-04-19 17:40:00', true);

-- Ejemplo de datos para la tabla anuncio_categoria
INSERT INTO anuncio_categoria (anuncio_id, categoria_id) VALUES
(1, 1),
(1, 5),
(2, 2),
(2, 7),
(3, 3),
(3, 10),
(4, 4),
(5, 5),
(6, 6),
(7, 7),
(8, 8),
(9, 9),
(10, 10);

-- Ejemplo de datos para la tabla anuncio_material
INSERT INTO anuncio_material (anuncio_id, material_id) VALUES
(1, 1),
(1, 3),
(2, 2),
(3, 3),
(3, 4),
(4, 4),
(5, 5),
(6, 6),
(7, 7),
(8, 8),
(9, 9),
(10, 10);

SET SQL_SAFE_UPDATES = 1;