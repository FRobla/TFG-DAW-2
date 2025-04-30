SET SQL_SAFE_UPDATES = 0;

-- Ejemplo de datos para la tabla usuarios
INSERT INTO usuarios (id, nombre, apellido, direccion, email, password, rol, foto, fecha_registro) VALUES
(1, 'Juan', 'Pérez', 'Calle Uno', 'juan1@mail.com', 'pass1', 'USER', NULL, '2024-04-01'),
(2, 'Ana', 'López', 'Calle Dos', 'ana2@mail.com', 'pass2', 'USER', NULL, '2024-04-02'),
(3, 'Luis', 'Martínez', 'Calle Tres', 'luis3@mail.com', 'pass3', 'ADMIN', NULL, '2024-04-03'),
(4, 'Marta', 'García', 'Calle Cuatro', 'marta4@mail.com', 'pass4', 'USER', NULL, '2024-04-04'),
(5, 'Pedro', 'Sánchez', 'Calle Cinco', 'pedro5@mail.com', 'pass5', 'USER', NULL, '2024-04-05'),
(6, 'Lucía', 'Ruiz', 'Calle Seis', 'lucia6@mail.com', 'pass6', 'USER', NULL, '2024-04-06'),
(7, 'Carlos', 'Fernández', 'Calle Siete', 'carlos7@mail.com', 'pass7', 'USER', NULL, '2024-04-07'),
(8, 'Sara', 'Díaz', 'Calle Ocho', 'sara8@mail.com', 'pass8', 'USER', NULL, '2024-04-08'),
(9, 'Elena', 'Moreno', 'Calle Nueve', 'elena9@mail.com', 'pass9', 'USER', NULL, '2024-04-09'),
(10, 'David', 'Muñoz', 'Calle Diez', 'david10@mail.com', 'pass10', 'USER', NULL, '2024-04-10');

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


-- Ejemplo de datos para la tabla materiales
INSERT INTO materiales (id, nombre, fabricante, color, tipo, precio_kg, stock) VALUES
(1, 'PLA', 'Prusa', 'Rojo', 'Filamento', 20.0, 50),
(2, 'ABS', 'Creality', 'Negro', 'Filamento', 22.0, 40),
(3, 'PETG', 'Anycubic', 'Transparente', 'Filamento', 25.0, 30),
(4, 'Resina Standard', 'Elegoo', 'Gris', 'Resina', 35.0, 20),
(5, 'PLA', 'Bambu', 'Azul', 'Filamento', 21.0, 60),
(6, 'ABS', 'Sunlu', 'Blanco', 'Filamento', 23.0, 25),
(7, 'PETG', 'Prusa', 'Verde', 'Filamento', 26.0, 10),
(8, 'Resina Flexible', 'Anycubic', 'Negro', 'Resina', 45.0, 15),
(9, 'PLA', 'Sunlu', 'Amarillo', 'Filamento', 19.0, 35),
(10, 'ABS', 'Elegoo', 'Gris', 'Filamento', 24.0, 12);

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

-- Ejemplo de datos para la tabla anuncios
DELETE FROM anuncios;

INSERT INTO anuncios (id, titulo, descripcion, estado, imagen, precio_base, tiempo_estimado, fecha_publicacion, usuario_id, impresora_id, vistas) VALUES
(1, 'Juguete Dinosaurio', 'Figura 3D de dinosaurio', 'activo', NULL, 10.0, '2 días', '2024-04-01', 1, 1, 10),
(2, 'Lámpara Decorativa', 'Lámpara impresa en 3D', 'activo', NULL, 25.0, '3 días', '2024-04-02', 2, 2, 15),
(3, 'Soporte Móvil', 'Soporte ajustable para móvil', 'activo', NULL, 8.0, '1 día', '2024-04-03', 3, 3, 8),
(4, 'Caja Organizadora', 'Caja multiusos impresa', 'activo', NULL, 12.0, '2 días', '2024-04-04', 4, 4, 12),
(5, 'Figura de Ajedrez', 'Pieza de ajedrez personalizada', 'activo', NULL, 5.0, '1 día', '2024-04-05', 5, 5, 5),
(6, 'Pendientes Modernos', 'Pendientes impresos', 'activo', NULL, 7.0, '1 día', '2024-04-06', 6, 6, 7),
(7, 'Organizador Escritorio', 'Organizador multifunción', 'activo', NULL, 15.0, '2 días', '2024-04-07', 7, 7, 15),
(8, 'Escultura Abstracta', 'Escultura para decoración', 'activo', NULL, 30.0, '4 días', '2024-04-08', 8, 8, 30),
(9, 'Portabotellas Bicicleta', 'Accesorio para bicicleta', 'activo', NULL, 6.0, '1 día', '2024-04-09', 9, 9, 6),
(10, 'Soporte Portátil', 'Soporte para portátil', 'activo', NULL, 18.0, '2 días', '2024-04-10', 10, 10, 18);

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

-- Ejemplo de datos para la tabla favoritos
INSERT INTO favoritos (id, usuario_id, anuncio_id, fecha_agregado) VALUES
(1, 1, 1, '2024-04-10'),
(2, 2, 2, '2024-04-11'),
(3, 3, 3, '2024-04-12'),
(4, 4, 4, '2024-04-13'),
(5, 5, 5, '2024-04-14'),
(6, 6, 6, '2024-04-15'),
(7, 7, 7, '2024-04-16'),
(8, 8, 8, '2024-04-17'),
(9, 9, 9, '2024-04-18'),
(10, 10, 10, '2024-04-19');

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

-- Ejemplo de datos para la tabla valoraciones
INSERT INTO valoraciones (id, usuario_id, anuncio_id, puntuacion, comentario, fecha) VALUES
(1, 1, 1, 5, 'Excelente', '2024-04-10'),
(2, 2, 2, 4, 'Muy bueno', '2024-04-11'),
(3, 3, 3, 3, 'Bueno', '2024-04-12'),
(4, 4, 4, 2, 'Regular', '2024-04-13'),
(5, 5, 5, 1, 'Excelente', '2024-04-14'),
(6, 6, 6, 5, 'Excelente', '2024-04-15'),
(7, 7, 7, 4, 'Muy bueno', '2024-04-16'),
(8, 8, 8, 3, 'Bueno', '2024-04-17'),
(9, 9, 9, 2, 'Regular', '2024-04-18'),
(10, 10, 10, 1, 'Excelente', '2024-04-19');

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