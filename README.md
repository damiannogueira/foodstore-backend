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
* Swagger / OpenAPI
* BCrypt (Spring Security Crypto)

---

## 📦 Funcionalidades

### 👤 Usuarios

* Crear usuarios
* Login de usuarios
* Validación de datos
* Email único
* Contraseña encriptada con BCrypt
* Ocultamiento de contraseña en respuestas

### 📂 Categorías

* CRUD completo
* Validaciones
* Control de duplicados

### 🛍️ Productos

* CRUD completo
* Relación con categoría
* Validación de stock y precio
* Control de duplicados

### 📦 Pedidos

* Crear pedidos con múltiples productos
* Cálculo automático de:

  * subtotal por producto
  * total del pedido
* Validación de stock disponible
* Descuento automático de stock
* Estados del pedido:

  * PENDIENTE
  * CONFIRMADO
  * CANCELADO
  * ENTREGADO
* Actualización de estado y forma de pago
* Listado de pedidos
* Listado por usuario

---

## 🧠 Lógica de negocio implementada

* Validación de stock antes de confirmar pedido
* Manejo de errores personalizados:

  * Recurso no encontrado (404)
  * Duplicados (400)
  * Validaciones (400)
  * Stock insuficiente (400)
* Relaciones JPA:

  * Producto → Categoría (ManyToOne)
  * Pedido → Usuario (ManyToOne)
  * Pedido → DetallePedido (OneToMany)
  * DetallePedido → Producto (ManyToOne)

---

## 🗂️ Estructura del proyecto
```
src/
└── main/
    ├── java/foodstore_backend/
    │   ├── controller/        # Endpoints REST (Usuario, Producto, Categoria, Pedido)
    │   ├── service/           # Lógica de negocio
    │   ├── repository/        # Acceso a datos con Spring Data JPA
    │   ├── model/             # Entidades JPA
    │   │   └── enums/         # Enumeraciones (Rol, EstadoPedido)
    │   ├── config/            # Configuración (Swagger, PasswordEncoder)
    │   ├── dto/               # DTOs (Usuario, Producto, Categoria, Pedido, Auth)
    │   ├── exception/         # Manejo de excepciones personalizadas
    │   └── FoodstoreBackendApplication.java
    │
    └── resources/
        ├── application.properties
        └── application-example.properties

test/                          # Tests (estructura base)
target/                        # Archivos generados por Maven
pom.xml                        # Configuración del proyecto
```
---

## 📌 Ejemplo de request (crear pedido)

```json
{
  "usuarioId": 1,
  "formaPago": "EFECTIVO",
  "detalles": [
    {
      "productoId": 1,
      "cantidad": 2
    }
  ]
}
```

---

## ⚙️ Configuración

Crear archivo:

```
src/main/resources/application.properties
```

Basado en:

```
application-example.properties
```

Ejemplo:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/food_store
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
```

---

## ▶️ Cómo ejecutar

```bash
./mvnw spring-boot:run
```

La API corre en:

```
http://localhost:8080
```

---

## 📄 Documentación API (Swagger)

Disponible en:

http://localhost:8080/swagger-ui/index.html

---

## 🧪 Endpoints principales

### Usuarios

* GET /usuarios
* GET /usuarios/{id}
* POST /usuarios
* DELETE /usuarios/{id}

### Auth

* POST /auth/login

### Categorías

* GET /categorias
* GET /categorias/{id}
* POST /categorias
* PUT /categorias/{id}
* DELETE /categorias/{id}

### Productos

* GET /productos
* GET /productos/{id}
* GET /productos/categoria/{categoriaId}
* POST /productos
* PUT /productos/{id}
* DELETE /productos/{id}

### Pedidos

* GET /pedidos
* GET /pedidos/{id}
* GET /pedidos/usuario/{usuarioId}
* POST /pedidos
* PUT /pedidos/{id}
* PUT /pedidos/{id}/estado
* DELETE /pedidos/{id}

---

## 📈 Estado del proyecto

Backend académico funcional y completo para el alcance del TPI de la materia Programación 3 de la Tecnicatura en Programación
a Distancia de la UTN.

Incluye implementación completa de backend con buenas prácticas, validaciones y lógica de negocio.
El objetivo es aplicar conceptos de desarrollo backend utilizando Spring Boot, incluyendo modelado de entidades,
relaciones JPA, validaciones y lógica de negocio.

No está destinado a uso productivo.

---

## 👨‍💻 Autor

Damián Nogueira.

Técnico Superior en Programación.

Estudiante TUPaD - UTN

---
