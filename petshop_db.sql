-- =============================================================
--  PetShop Online — Script de base de datos
--  Compatible con MySQL 8 / MySQL Workbench
--  Orden de creación respeta las FK (sin depender de SET FOREIGN_KEY_CHECKS)
-- =============================================================

CREATE DATABASE IF NOT EXISTS petshop_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE petshop_db;

-- -------------------------------------------------------------
-- 1. categoria
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS categoria (
    id_categoria  INT          NOT NULL AUTO_INCREMENT,
    nombre        VARCHAR(100) NOT NULL,
    descripcion   VARCHAR(255),
    PRIMARY KEY (id_categoria)
) ENGINE=InnoDB;

-- -------------------------------------------------------------
-- 2. usuario
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS usuario (
    id_usuario  INT          NOT NULL AUTO_INCREMENT,
    nombre      VARCHAR(150) NOT NULL,
    correo      VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    rol         ENUM('ADMIN','USER') NOT NULL DEFAULT 'USER',
    PRIMARY KEY (id_usuario)
) ENGINE=InnoDB;

-- -------------------------------------------------------------
-- 3. producto
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS producto (
    id_producto   INT            NOT NULL AUTO_INCREMENT,
    nombre        VARCHAR(150)   NOT NULL,
    descripcion   TEXT,
    precio        DECIMAL(10,2)  NOT NULL,
    imagen        VARCHAR(500),
    id_categoria  INT,
    PRIMARY KEY (id_producto),
    CONSTRAINT fk_producto_categoria
        FOREIGN KEY (id_categoria) REFERENCES categoria (id_categoria)
        ON UPDATE CASCADE
        ON DELETE SET NULL
) ENGINE=InnoDB;

-- -------------------------------------------------------------
-- 4. pedido
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS pedido (
    id_pedido       INT            NOT NULL AUTO_INCREMENT,
    id_usuario      INT            NOT NULL,
    total           DECIMAL(10,2)  NOT NULL,
    direccion_envio VARCHAR(300)   NOT NULL,
    fecha           DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_pedido),
    CONSTRAINT fk_pedido_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

-- -------------------------------------------------------------
-- 5. detalle_pedido  (equivalente a List<ItemCarrito> en Pedido)
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS detalle_pedido (
    id_detalle      INT           NOT NULL AUTO_INCREMENT,
    id_pedido       INT           NOT NULL,
    id_producto     INT           NOT NULL,
    cantidad        INT           NOT NULL CHECK (cantidad > 0),
    precio_unitario DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id_detalle),
    CONSTRAINT fk_detalle_pedido
        FOREIGN KEY (id_pedido)   REFERENCES pedido   (id_pedido)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_detalle_producto
        FOREIGN KEY (id_producto) REFERENCES producto (id_producto)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- =============================================================
--  Datos iniciales
-- =============================================================

INSERT INTO categoria (id_categoria, nombre, descripcion) VALUES
    (1, 'Alimentos',   'Alimentos y nutrición para mascotas'),
    (2, 'Accesorios',  'Correas, collares y accesorios varios'),
    (3, 'Higiene',     'Productos de higiene y cuidado personal');

INSERT INTO producto (id_producto, nombre, descripcion, precio, imagen, id_categoria) VALUES
    (1, 'Alimento Premium',   'Alimento balanceado para perros adultos.',   25.00,
        'https://images.unsplash.com/photo-1589924691995-400dc9ecc119', 1),
    (2, 'Juguete para Perro', 'Juguete resistente para mascotas activas.',   8.50,
        'https://images.unsplash.com/photo-1601758124510-52d02ddb7cbd', 2),
    (3, 'Arena para Gato',    'Arena absorbente con control de olores.',    12.00,
        'https://images.unsplash.com/photo-1574158622682-e40e69881006', 3),
    (4, 'Correa Ajustable',   'Correa cómoda y resistente.',                10.00,
        'https://images.unsplash.com/photo-1548199973-03cce0bbc87b', 2);

INSERT INTO usuario (id_usuario, nombre, correo, password, rol) VALUES
    (1, 'Administrador', 'admin@petshop.com', '1234', 'ADMIN');
