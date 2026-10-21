# Support Orders Fullstack By David Leonardo Barrera

Aplicación fullstack para registrar, consultar y gestionar órdenes de soporte originadas desde una aplicación móvil.

La solución fue desarrollada con un límite de tiempo de 4 horas, priorizando el flujo funcional, las reglas de negocio, las pruebas y una estructura simple de mantener.

## Tecnologías

**Backend**

- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Bean Validation
- H2
- JUnit / MockMvc

**Frontend**

- Angular 19
- TypeScript
- Reactive Forms
- HttpClient
- SCSS

## Estructura

```text
support-orders-from-mobile-application/
-->  Backend/
      --> support-orders-api/
--> Frontend/
     --> frontend/
        --> api.http
    README.md
  .gitignore
```

Backend:

```text
com.davidbarrera.supportorders
 controller
 dto
 entity
 enums
 exception
 factory
 repository
 service
```

## Ejecución

### Backend

```bash
cd Backend/support-orders-api
mvnw.cmd spring-boot:run
```

API:

```text
http://localhost:8080
```

### Frontend

```bash
cd Frontend/frontend
npm install
npm start
```

Aplicación:

```text
http://localhost:4200
```

## Base de datos

Se utiliza H2 para facilitar la ejecución local.

```text
JDBC URL: jdbc:h2:file:./data/supportorders
User: sa
Password: vacío
```

Consola:

```text
http://localhost:8080/h2-console
```

Los archivos locales de H2 están excluidos del repositorio.

## API

### Crear orden

```http
POST /api/support-orders
```

```json
{
  "externalReference": "MOB-1001",
  "customerId": "CUST-001",
  "category": "PAYMENT",
  "description": "El pago aparece como pendiente después de varios minutos.",
  "channel": "MOBILE_APP",
  "priority": "MEDIUM"
}
```

La orden se crea en estado `OPEN` y `createdAt` es generado por el backend.

### Listar

```http
GET /api/support-orders
GET /api/support-orders?status=OPEN
```

### Consultar detalle

```http
GET /api/support-orders/{id}
```

### Actualizar estado

```http
PATCH /api/support-orders/{id}/status
```

```json
{
  "status": "IN_PROGRESS"
}
```

Transiciones permitidas:

```text
OPEN -> IN_PROGRESS | CANCELLED
IN_PROGRESS -> RESOLVED | CANCELLED
RESOLVED -> final
CANCELLED -> final
```

Se considera activa una orden en estado `OPEN` o `IN_PROGRESS`.

También se incluye `api.http` con ejemplos de consumo.

## Pruebas

Backend:

```bash
cd Backend/support-orders-api
mvnw.cmd test
```

Se cubren los siguientes casos:

- creación válida;
- request inválido;
- transición de estado inválida;
- consulta de orden existente;
- consulta de orden inexistente;
- respuestas HTTP mediante MockMvc.

Resultado actual:

```text
Tests run: 5
Failures: 0
Errors: 0
```

Frontend:

```bash
cd Frontend/frontend
npm run build
```

## Decisiones técnicas

### 1. Arquitectura

La solución se separó por responsabilidades:

```text
Controller -> Service -> Repository -> Base de datos
```

Los DTO representan el contrato HTTP y están separados de las entidades JPA.

El Controller maneja HTTP, el Service contiene las reglas de negocio y el Repository concentra el acceso a datos. Para esta prueba consideré suficiente esta estructura y evité agregar capas que no eran necesarias para el alcance.

### 2. SOLID y patrón

Se aplicó principalmente **Single Responsibility Principle**: cada clase tiene una responsabilidad concreta.

Se utilizó **Factory** para centralizar la creación de `SupportOrder` a partir del DTO de entrada y evitar que el Controller conozca detalles de construcción de la entidad.

### 3. Restricciones por tiempo

Para cumplir el límite de 4 horas se priorizó el flujo principal. No se implementaron:

- autenticación/autorización;
- Docker;
- paginación;
- auditoría completa;
- pruebas automatizadas de frontend;
- despliegue.

La paginación podría agregarse con `Pageable` de Spring Data.

### 4. SQL vs NoSQL

SQL es apropiado porque las órdenes tienen una estructura definida y reglas de consistencia sobre estados y referencias externas.

Para la prueba se utilizó H2; en producción usaría PostgreSQL.

Actualmente se evita que una misma `externalReference` tenga más de una orden activa. En producción reforzaría esta regla también a nivel de base de datos para cubrir escenarios de concurrencia, por ejemplo mediante un índice único parcial en PostgreSQL.

Revisaría una opción NoSQL si el sistema necesitara manejar muchos eventos, historiales grandes o payloads con estructuras variables.

### 5. Producción

Antes de desplegar agregaría:

- OAuth2/OIDC y autorización por roles;
- PostgreSQL;
- logs estructurados y correlation ID;
- métricas, tracing y alertas;
- auditoría de cambios;
- idempotencia;
- manejo de concurrencia;
- health checks;
- CORS y URLs configurables por ambiente;
- CI/CD y contenedores.

La idempotencia sería especialmente útil para clientes móviles que pueden reenviar solicitudes por problemas de conectividad.

### 6. Trabajo en paralelo

Frontend y backend pueden desarrollarse en paralelo una vez definidos:

- endpoints;
- requests y responses;
- estados y prioridades;
- reglas de transición;
- códigos HTTP;
- formato de errores.

Con estos puntos claros, frontend puede trabajar con mocks mientras backend implementa la API. Las pruebas end-to-end requieren coordinación posterior.

### 7. Recursos adicionales

Con más recursos incorporaría:

- **Backend Developer:** concurrencia, seguridad, PostgreSQL, migraciones y observabilidad.
- **Frontend Developer:** componentes reutilizables, accesibilidad y pruebas.
- **QA / Automation:** pruebas E2E, regresión y escenarios negativos.
- **DevOps / SRE:** CI/CD, infraestructura, monitoreo y despliegue.

Backend, frontend y automatización pueden avanzar parcialmente en paralelo. Las decisiones sobre contratos, modelo de datos y reglas de negocio requieren coordinación previa.

## Supuestos

- `OPEN` e `IN_PROGRESS` son estados activos.
- `RESOLVED` y `CANCELLED` son estados finales.
- `createdAt` es generado por el backend.
- Una referencia externa puede reutilizarse cuando las órdenes anteriores estén en estado final.
- `category` y `channel` se manejan como texto porque no se definió un catálogo cerrado.
