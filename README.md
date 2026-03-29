# 🛒 FoodStore Backend

Backend desarrollado en Java con Spring Boot para la gestión de una tienda online.

Permite administrar usuarios, categorías, productos y pedidos, incluyendo lógica de negocio real como cálculo de totales, validación de stock y gestión de estados.

---

## 🚀 Tecnologías utilizadas

* Java 17+
* Spring Boot
* Spring Data JPA
* Hibernate
* MySQL
* Maven

---
```
foodstore-backend
│
├── src/main/java/foodstore_backend
│   ├── controller      # Endpoints REST
│   ├── service         # Lógica de negocio
│   ├── repository      # Acceso a datos (JPA)
│   ├── model           # Entidades
│   ├── dto             # Objetos de transferencia
│   ├── exception       # Manejo global de errores
│   └── config          # Configuraciones
│
├── src/main/resources
│   ├── application.properties
│
└── pom.xml
```
---

## 📦 Funcionalidades

### 👤 Usuarios

* Registro de usuarios (`/api/auth/register`)
* Login (`/api/auth/login`)
* Validación de datos
* Email único
* Contraseña encriptada con SHA-256
* Ocultamiento de contraseña en respuestas

---

### 📂 Categorías

* CRUD completo (`/api/categories`)
* Validaciones de nombre, descripción e imagen
* Control de duplicados
* Soft delete (baja lógica)

---

### 🛍️ Productos

* CRUD completo (`/api/products`)
* Relación con categoría
* Validación de:

  * precio > 0
  * stock >= 0
  * imagen válida
* Campo `disponible` para control de venta
* Filtros:

  * productos disponibles
  * productos por categoría
* Soft delete

---

### 🧾 Pedidos

* Creación de pedidos (`/api/orders`)
* Validaciones:

  * usuario existente
  * al menos un producto
  * producto disponible
  * stock suficiente
* Cálculo automático de:

  * subtotales
  * total del pedido
* Descuento automático de stock
* Manejo transaccional (`@Transactional`)
* Estados del pedido:

  * PENDIENTE
  * CONFIRMADO
  * CANCELADO
  * ENTREGADO
* Actualización de estado:

  * `PATCH /api/orders/{id}/status`
* Listados:

  * todos los pedidos
  * por usuario
  * por estado

---

## 🛠️ Configuración del proyecto

### Requisitos

* Java 17 o superior
* MySQL
* Maven

### Base de datos

1. Crear una base de datos en MySQL:

```sql
CREATE DATABASE food_store;
```

2. Configurar el archivo `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/food_store
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## ▶️ Ejecución

Desde la raíz del proyecto:

```
mvn spring-boot:run
```

---

## 📄 Documentación API

Swagger disponible en:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 🔗 Endpoints principales

### Auth

* `POST /api/auth/register`
* `POST /api/auth/login`

### Categorías

* `GET /api/categories`
* `GET /api/categories/{id}`
* `POST /api/categories`
* `PUT /api/categories/{id}`
* `DELETE /api/categories/{id}`

### Productos

* `GET /api/products`
* `GET /api/products/available`
* `GET /api/products/{id}`
* `POST /api/products`
* `PUT /api/products/{id}`
* `DELETE /api/products/{id}`
* `GET /api/products/category/{id}`

### Pedidos

* `GET /api/orders`
* `GET /api/orders/{id}`
* `GET /api/orders/usuario/{id}`
* `GET /api/orders?estado=PENDIENTE`
* `POST /api/orders`
* `PUT /api/orders/{id}`
* `DELETE /api/orders/{id}`
* `PATCH /api/orders/{id}/status`

---

## ⚙️ Características técnicas

* Arquitectura en capas:

  * Controller
  * Service
  * Repository
* Uso de DTOs para entrada/salida
* Validaciones con Jakarta Validation (`@Valid`)
* Manejo global de excepciones (`@ControllerAdvice`)
* Soft delete en todas las entidades
* Transacciones en operaciones críticas
* API RESTful siguiendo buenas prácticas

---

## 🧪 Pruebas recomendadas

Se recomienda probar con Swagger o herramientas como Postman/Thunder Client:

* Registro y login de usuario
* CRUD de categorías y productos
* Creación de pedidos válidos
* Casos de error:

  * producto no disponible
  * stock insuficiente
  * datos inválidos

---

## 🎥 Video demostrativo

👉 [AGREGAR LINK DEL VIDEO]

---

## 📄 Documentación (PDF)

👉 [AGREGAR LINK O ARCHIVO PDF]

---

## 👨‍💻 Autor

Damián Nogueira

---

## 📌 Notas finales

Este proyecto fue desarrollado como trabajo práctico integrador de la materia Programación 3 de la carrera de Programación de la UTN, aplicando conceptos de:

* API REST
* Persistencia con JPA/Hibernate
* Validaciones de negocio
* Manejo de errores
* Arquitectura backend en Java

---
