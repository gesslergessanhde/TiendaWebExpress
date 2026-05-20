    -- =====================================================================
-- SCRIPT DE CREACIÓN DE BASE DE DATOS Y CARGA DE DATOS INICIALES
-- PROYECTO: Tienda Web Express (Tecnología y Gaming)
-- =====================================================================

-- 1. Creación de la Base de Datos
CREATE DATABASE tienda_web_express;
GO

USE tienda_web_express;
GO

-- 2. Limpieza previa por seguridad en caso de re-ejecución
IF OBJECT_ID('detalle_pedido', 'U') IS NOT NULL DROP TABLE detalle_pedido;
IF OBJECT_ID('pedido', 'U') IS NOT NULL DROP TABLE pedido;
IF OBJECT_ID('productos', 'U') IS NOT NULL DROP TABLE productos;
GO

-- 3. Creación de la Tabla de Productos (Mapeada en Spring Boot)
CREATE TABLE productos (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    categoria VARCHAR(80) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    descripcion VARCHAR(1000),
    imagen VARCHAR(255),
    disponible BIT NOT NULL DEFAULT 1,
    destacado BIT NOT NULL DEFAULT 1
);
GO

-- 4. Creación de la Tabla de Pedidos 
CREATE TABLE pedido (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre_cliente VARCHAR(150) NOT NULL,
    correo VARCHAR(150) NOT NULL,
    producto_solicitado VARCHAR(150) NOT NULL,
    cantidad INT NOT NULL,
    comentario VARCHAR(500),
    fecha_pedido DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);
GO

-- 5. Inserción de Datos Iniciales (Catálogo Tecnológico con imágenes PNG)
INSERT INTO productos (nombre, categoria, precio, descripcion, imagen, disponible, destacado) VALUES
('Mouse inalámbrico', 'Tecnología', 12.99, 'Mouse ergonómico con conexión USB.', 'mouse.png', 1, 1),
('Teclado mecánico', 'Tecnología', 39.99, 'Teclado mecánico con iluminación LED.', 'teclado.png', 1, 1),
('Camiseta básica', 'Ropa', 9.50, 'Camiseta de algodón disponible en varias tallas.', 'camiseta.png', 1, 1),
('Taza personalizada', 'Hogar', 6.75, 'Taza de cerámica ideal para regalos.', 'taza.png', 1, 1),
('Mochila urbana', 'Accesorios', 24.99, 'Mochila resistente para uso diario.', 'mochila.png', 1, 1);
GO

-- 6. Verificación de la carga correcta
SELECT * FROM productos;
SELECT * FROM pedido;
GO
