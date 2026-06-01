# ConneXtion HelpDesk — Documentación de API

> **Base URL:** `http://localhost:8080/api`  
> **Base de datos:** SQL Server · `ConneXtionHelpDesk`  
> **Autenticación:** Sin JWT. El frontend gestiona la sesión localmente con los datos devueltos en `/login`.  
> **CORS:** Habilitado para todos los orígenes (`*`).

---

## Tabla de contenidos

- [Usuarios](#usuarios)
  - [POST /users/register](#post-usersregister)
  - [POST /login](#post-login)
  - [POST /logout](#post-logout)
  - [POST /support-users](#post-support-users)
  - [GET /users](#get-users)
- [Servicios](#servicios)
  - [GET /services](#get-services)
- [Tickets](#tickets)
  - [GET /tickets](#get-tickets)
  - [POST /tickets](#post-tickets)
  - [PUT /tickets/{ticketId}/assign/{technicianId}](#put-ticketsticketidassigntechnicianid)
  - [PATCH /tickets/{ticketId}/status](#patch-ticketsticketidstatus)
- [Modelos de datos](#modelos-de-datos)
- [Roles y valores permitidos](#roles-y-valores-permitidos)

---

## Usuarios

### POST /users/register

Registra un nuevo usuario con rol **CLIENT**.

**Request body**
```json
{
  "name":       "string (requerido)",
  "email":      "string (requerido, único)",
  "password":   "string (requerido)",
  "address":    "string (opcional)",
  "phone":      "string (opcional)",
  "serviceIds": [1, 2]
}
```

> `serviceIds` debe contener al menos un ID válido de la tabla `services`.

**Respuestas**

| Código | Descripción |
|--------|-------------|
| `201 Created` | Usuario registrado exitosamente |
| `400 Bad Request` | Correo ya registrado o `serviceIds` vacío/nulo |

**Body 201**
```json
{
  "message": "Usuario registrado exitosamente.",
  "userId": 5
}
```

**Body 400**
```json
{ "message": "El correo electrónico ya está registrado." }
// o
{ "message": "Debe seleccionar al menos un servicio." }
```

---

### POST /login

Autentica cualquier tipo de usuario (CLIENT, SUPPORTER, SUPERVISOR).

**Request body**
```json
{
  "email":    "string",
  "password": "string"
}
```

**Respuestas**

| Código | Descripción |
|--------|-------------|
| `200 OK` | Credenciales válidas |
| `401 Unauthorized` | Correo o contraseña incorrectos |
| `403 Forbidden` | Usuario de soporte sin servicios asignados |

**Body 200**
```json
{
  "id":                 1,
  "name":               "Juan Pérez",
  "email":              "juan@example.com",
  "role":               "CLIENT",
  "isSupervisor":       false,
  "subscribedServices": [
    { "id": 1, "name": "Internet" }
  ]
}
```

---

### POST /logout

Cierra la sesión. Como no hay JWT stateful, el servidor solo confirma; el frontend debe limpiar su sesión local.

**Request body:** ninguno

**Respuesta 200**
```json
{ "message": "Sesión cerrada correctamente." }
```

---

### POST /support-users

Registra un nuevo usuario de soporte (**SUPPORTER** o **SUPERVISOR**). Solo debe ser llamado por un administrador.

**Request body**
```json
{
  "name":         "string (requerido)",
  "email":        "string (requerido, único)",
  "password":     "string (requerido)",
  "isSupervisor": false,
  "serviceIds":   [1, 3]
}
```

> Si `isSupervisor` es `true` el rol queda como `SUPERVISOR`; si es `false`, como `SUPPORTER`.

**Respuestas**

| Código | Descripción |
|--------|-------------|
| `201 Created` | Usuario de soporte creado |
| `400 Bad Request` | Correo duplicado o `serviceIds` vacío |

**Body 201**
```json
{
  "message":      "Usuario de soporte registrado exitosamente.",
  "userId":       8,
  "role":         "SUPPORTER",
  "isSupervisor": false
}
```

---

### GET /users

Devuelve la lista completa de usuarios registrados en el sistema.

**Respuesta 200** — array de objetos `User`
```json
[
  {
    "id":                 1,
    "name":               "Juan Pérez",
    "email":              "juan@example.com",
    "role":               "CLIENT",
    "address":            "San José, Costa Rica",
    "phone":              "88001234",
    "isSupervisor":       false,
    "subscribedServices": [ { "id": 2, "name": "Cable" } ],
    "createdTickets":     [],
    "assignedTickets":    []
  }
]
```

---

## Servicios

### GET /services

Devuelve todos los servicios disponibles. Úsalo para poblar selectores en el registro de usuarios.

**Servicios pre-cargados al iniciar la aplicación:**

| id | name |
|----|------|
| 1 | Telefonía móvil |
| 2 | Cable |
| 3 | Internet |
| 4 | Telefonía fija |

**Respuesta 200**
```json
[
  { "id": 1, "name": "Telefonía móvil" },
  { "id": 2, "name": "Cable" },
  { "id": 3, "name": "Internet" },
  { "id": 4, "name": "Telefonía fija" }
]
```

---

## Tickets

### GET /tickets

Devuelve todos los tickets existentes.

**Respuesta 200** — array de objetos `Ticket`
```json
[
  {
    "id":          1,
    "title":       "Internet sin conexión",
    "description": "Sin señal desde las 8am",
    "status":      "OPEN",
    "priority":    "HIGH",
    "createdAt":   "2026-05-31T10:00:00",
    "client": {
      "id":    3,
      "name":  "María López",
      "email": "maria@example.com",
      "role":  "CLIENT"
    },
    "technician": null,
    "category": {
      "id":          2,
      "name":        "Conectividad",
      "description": "Problemas de red"
    }
  }
]
```

---

### POST /tickets

Crea un nuevo ticket. El status se fuerza automáticamente a `"OPEN"`.

**Request body**
```json
{
  "title":       "string (requerido, máx 150 chars)",
  "description": "string (requerido)",
  "priority":    "LOW | MEDIUM | HIGH",
  "client":      { "id": 3 },
  "category":    { "id": 2 }
}
```

> `technician` se omite al crear; se asigna posteriormente con el endpoint de asignación.

**Respuestas**

| Código | Descripción |
|--------|-------------|
| `200 OK` | Ticket creado (incluye objeto completo) |
| `400 Bad Request` | Datos inválidos o entidades relacionadas inexistentes |

**Body 200** — objeto `Ticket` completo (ver estructura arriba).

---

### PUT /tickets/{ticketId}/assign/{technicianId}

Asigna un técnico a un ticket y cambia el status a `"IN_PROGRESS"`.

> El usuario asignado debe tener rol `TECHNICIAN` o `ADMIN`; de lo contrario retorna error 400.

**Path params**

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `ticketId` | Long | ID del ticket |
| `technicianId` | Long | ID del usuario técnico |

**Respuestas**

| Código | Descripción |
|--------|-------------|
| `200 OK` | Técnico asignado, ticket actualizado |
| `400 Bad Request` | Ticket o técnico no encontrado / rol incorrecto |

**Body 200** — objeto `Ticket` actualizado con `status: "IN_PROGRESS"` y `technician` poblado.

---

### PATCH /tickets/{ticketId}/status

Actualiza el status de un ticket de forma libre.

**Path param**

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `ticketId` | Long | ID del ticket |

**Query param**

| Parámetro | Tipo | Valores válidos |
|-----------|------|-----------------|
| `status` | String | `OPEN`, `IN_PROGRESS`, `RESOLVED`, `CLOSED` |

**Ejemplo de llamada**
```
PATCH /api/tickets/7/status?status=RESOLVED
```

**Respuestas**

| Código | Descripción |
|--------|-------------|
| `200 OK` | Status actualizado |
| `400 Bad Request` | Ticket no encontrado |

**Body 200** — objeto `Ticket` con el nuevo status.

---

## Modelos de datos

### User
| Campo | Tipo | Notas |
|-------|------|-------|
| `id` | Long | Auto-generado |
| `name` | String | Nombre completo (usado para login/display) |
| `email` | String | Único en la tabla |
| `password` | String | Plano (sin hash en versión actual) |
| `role` | String | Ver tabla de roles |
| `address` | String | Opcional, solo clientes |
| `phone` | String | Opcional, solo clientes |
| `isSupervisor` | boolean | `true` cuando el rol es SUPERVISOR |
| `subscribedServices` | Service[] | Servicios suscritos/asignados |

### Ticket
| Campo | Tipo | Notas |
|-------|------|-------|
| `id` | Long | Auto-generado |
| `title` | String | Máx 150 chars |
| `description` | String | Texto libre |
| `status` | String | Ver valores permitidos |
| `priority` | String | `LOW`, `MEDIUM`, `HIGH` |
| `createdAt` | LocalDateTime | Se asigna automáticamente al crear |
| `client` | User | Usuario con rol CLIENT |
| `technician` | User | Puede ser null hasta asignación |
| `category` | Category | Categoría del ticket |

### Category
| Campo | Tipo |
|-------|------|
| `id` | Long |
| `name` | String |
| `description` | String |

### Service
| Campo | Tipo |
|-------|------|
| `id` | Long |
| `name` | String |

---

## Roles y valores permitidos

### Roles de usuario (`role`)
| Valor | Descripción |
|-------|-------------|
| `CLIENT` | Cliente registrado |
| `SUPPORTER` | Técnico de soporte |
| `SUPERVISOR` | Supervisor / administrador |
| `TECHNICIAN` | Rol reconocido por el servicio de asignación de tickets |
| `ADMIN` | Rol reconocido por el servicio de asignación de tickets |

### Status de ticket (`status`)
| Valor | Descripción |
|-------|-------------|
| `OPEN` | Recién creado, sin asignar |
| `IN_PROGRESS` | Técnico asignado |
| `RESOLVED` | Resuelto por el técnico |
| `CLOSED` | Cerrado definitivamente |

### Prioridad de ticket (`priority`)
| Valor |
|-------|
| `LOW` |
| `MEDIUM` |
| `HIGH` |

---

## Notas de integración

- Todos los endpoints aceptan y devuelven `Content-Type: application/json`.
- No existe paginación en esta versión; todos los `GET` devuelven la colección completa.
- No hay autenticación por token. El frontend guarda el objeto retornado por `/login` y lo usa para enviar los IDs correspondientes en cada petición.
- Para crear un ticket se envían solo los IDs de `client` y `category` dentro del objeto (`{ "id": X }`); Hibernate resuelve las relaciones.
- El servidor corre por defecto en `localhost:8080`. Si el equipo B levanta el backend en otra máquina, reemplazar `localhost` por la IP o dominio correspondiente.

