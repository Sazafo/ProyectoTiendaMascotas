# PetShop Online

PetShop Online es una aplicación web desarrollada con **Java, Spring Boot, Thymeleaf y MySQL** para la gestión y compra de productos para mascotas.

##Ejecución

La aplicación se ejecuta en:

http://localhost:8080

Antes de iniciar el proyecto, debe estar creada la base de datos:

petshop_db

## Usuarios

### Administrador

Correo:
admin@petshop.com

Contraseña:
1234

El administrador puede:

- Gestionar productos.
- Crear, editar y eliminar productos.
- Gestionar categorías.
- Administrar el stock.
- Consultar pedidos.
- Gestionar usuarios.
- Acceder al panel administrativo.

### Usuario

Los usuarios pueden registrarse directamente desde la aplicación.

Un usuario puede:

- Iniciar sesión.
- Consultar productos.
- Filtrar productos por categoría y precio.
- Ver detalles de productos.
- Agregar productos al carrito.
- Realizar compras.
- Consultar sus pedidos.
- Editar su perfil.
- Publicar reseñas de productos.

## Base de datos

El sistema utiliza **MySQL** y la base de datos:

petshop_db

Tablas principales:

- usuario
- categoria
- producto
- pedido
- detalle_pedido
- pago
- movimiento_inventario
- resena

Los productos, usuarios, categorías, pedidos, pagos, movimientos de inventario y reseñas se almacenan de forma persistente en MySQL.

## Tecnologías

- Java 17
- Spring Boot
- Spring Data JPA
- Thymeleaf
- MySQL
- Bootstrap
- HTML / CSS
- Maven
