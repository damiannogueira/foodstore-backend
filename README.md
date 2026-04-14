🛒 FoodStore Backend

Backend desarrollado en Java con Spring Boot para la gestión de una tienda online.

Permite administrar usuarios, categorías, productos y pedidos, incluyendo lógica de negocio real como cálculo de totales, validación de stock y gestión de estados.

🚀 Tecnologías utilizadas
Java 17+
Spring Boot
Spring Data JPA
Hibernate
MySQL
Maven

---

📁 Estructura del proyecto
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
│   └── config          # Configuraciones (UserLoad, etc.)
│
├── src/main/resources
│   ├── application-example.properties
│
├── .mvn/               # Maven Wrapper
├── mvnw
├── mvnw.cmd
│
└── pom.xml
```
---

📦 Funcionalidades

👤 Usuarios
Registro (/api/auth/register)
Login (/api/auth/login)
Validación de datos
Email único
Contraseña encriptada con BCrypt
Soft delete (baja lógica)
Ocultamiento de contraseña en respuestas

📂 Categorías
CRUD completo (/api/categories)
Validaciones de datos
Control de duplicados
Soft delete (baja lógica)

🛍️ Productos
CRUD completo (/api/products)
Relación con categoría
Validaciones:
precio > 0
stock >= 0
imagen válida
Campo disponible
Filtros:
productos disponibles
productos por categoría
Soft delete

🧾 Pedidos
Creación (/api/orders)
Validaciones:
usuario existente
productos válidos
stock suficiente
Cálculo automático de totales
Descuento de stock
Manejo transaccional (@Transactional)
Estados del pedido:
PENDIENTE
CONFIRMADO
EN_PREPARACION
ENVIADO
ENTREGADO
TERMINADO
CANCELADO
Cambio de estado:
PATCH /api/orders/{id}/status
Listados:
todos
por usuario
por estado

---

⚙️ Configuración del proyecto
Requisitos
Java 17 o superior
MySQL
No es necesario tener Maven instalado (se usa Maven Wrapper)
🔧 Configuración de base de datos

Por seguridad, el archivo application.properties no se incluye en el repositorio.

Se provee un archivo de ejemplo:

application-example.properties

Pasos:

Copiar el archivo:
cp application-example.properties application.properties
Completar credenciales:
spring.datasource.url=jdbc:mysql://localhost:3306/food_store
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD
Crear base de datos:
CREATE DATABASE food_store;

---

👑 Usuario administrador por defecto

Se crea automáticamente al iniciar la aplicación si no existe:

Email: admin@admin.com
Password: 123456

---

▶️ Ejecución

Usando Maven Wrapper:

.\mvnw.cmd spring-boot:run

---

📄 Documentación API

Swagger:

http://localhost:8080/swagger-ui/index.html

---

🔗 Endpoints principales

Auth
POST /api/auth/register
POST /api/auth/login

Categorías
GET /api/categories
GET /api/categories/{id}
POST /api/categories
PUT /api/categories/{id}
DELETE /api/categories/{id}

Productos
GET /api/products
GET /api/products/available
GET /api/products/{id}
POST /api/products
PUT /api/products/{id}
DELETE /api/products/{id}
GET /api/products/categoria/{id}

Pedidos
GET /api/orders
GET /api/orders/{id}
GET /api/orders/usuario/{id}
GET /api/orders?estado=PENDIENTE
POST /api/orders
PUT /api/orders/{id}
DELETE /api/orders/{id}
PATCH /api/orders/{id}/status

---

⚙️ Características técnicas
Arquitectura en capas (Controller / Service / Repository)
Uso de DTOs con Java Records
Validaciones con Jakarta Validation
Manejo global de excepciones
Soft delete en todas las entidades
Transacciones en operaciones críticas
API RESTful (status codes, DTOs, validaciones)
Encriptación de contraseñas con BCrypt
Maven Wrapper (ejecución sin instalar Maven)

---

🧪 Pruebas

El proyecto incluye:

Tests unitarios (Service) con JUnit y Mockito
Tests de integración (Controller) con MockMvc
Aislamiento de datos en tests (limpieza de base de datos para evitar conflictos de FK)

Casos cubiertos:

Login correcto e incorrecto
Duplicados (usuarios, categorías, productos)
Soft delete
Validaciones de negocio
Flujo completo de pedidos

---

🎥 Video demostrativo

 [AGREGAR LINK DEL VIDEO]

---

📄 Documentación (PDF)

[AGREGAR LINK O ARCHIVO PDF]

---

👨‍💻 Autor

Damián Ignacio Nogueira
Estudiante TUPaD - UTN

---

📌 Notas finales

Este proyecto fue desarrollado como trabajo práctico integrador de Programación 3.

Incluye:

API REST completa
Persistencia con JPA/Hibernate
Validaciones de negocio reales
Control de stock y estados de pedidos
Seguridad básica (BCrypt)
Testing unitario e integración

El sistema simula el funcionamiento real de un backend de e-commerce, aplicando buenas prácticas de desarrollo backend.
