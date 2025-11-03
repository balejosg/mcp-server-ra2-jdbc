# MCP Server RA2 - Acceso a Datos mediante JDBC PURO

Servidor educacional MCP (Model Context Protocol) para enseñanza de JDBC vanilla en el módulo de Acceso a Datos de 2º DAM.

## 📋 Descripción

Proyecto educativo que proporciona un **esqueleto de aplicación JDBC PURO** donde los estudiantes implementan operaciones de base de datos usando JDBC vanilla (sin JPA/Hibernate/Spring DataSource). El proyecto expone 15 herramientas MCP que los estudiantes deben completar.

## ⚡ IMPORTANTE: JDBC Puro vs Spring DataSource

**Este proyecto usa JDBC VANILLA deliberadamente para máximo aprendizaje:**

✅ **SÍ usamos:**
- `DriverManager.getConnection()` - Conexiones directas
- `Class.forName()` - Carga explícita del driver
- Gestión manual de conexiones
- Try-with-resources obligatorio

❌ **NO usamos:**
- Spring `DataSource` (inyección de dependencias)
- Spring `JdbcTemplate`
- Connection pools automáticos de Spring
- Inicialización automática de BD por Spring

**¿Por qué?** Los estudiantes aprenden:
1. El ciclo completo de JDBC desde cero
2. Cómo funcionan las conexiones realmente
3. La importancia del cierre manual de recursos
4. Los fundamentos antes de usar abstracciones

**Estado actual:**
- ✅ **5 métodos EJEMPLOS implementados** (para aprender el patrón)
- ⚠️ **10 métodos TODO** (para que estudiantes implementen)

## 🎯 Resultado de Aprendizaje

**RA2**: Desarrolla aplicaciones que gestionan información almacenada mediante conectores

### Criterios de Evaluación

| CE | Descripción | Métodos |
|----|-------------|---------|
| **CE2.a** | Gestión de conexiones a bases de datos | `testConnection()`, `getConnectionInfo()` |
| **CE2.b** | Operaciones CRUD con JDBC | `createUser()`, `findUserById()`, `updateUser()`, `deleteUser()`, `findAll()` |
| **CE2.c** | Consultas avanzadas y paginación | `findUsersByDepartment()`, `searchUsers()`, `findUsersWithPagination()` |
| **CE2.d** | Gestión de transacciones | `transferData()`, `batchInsertUsers()` |
| **CE2.e** | Metadatos de bases de datos | `getDatabaseInfo()`, `getTableColumns()` |
| **CE2.f** | Stored Procedures (avanzado) | `executeCountByDepartment()` |

## 🏗️ Estructura del Proyecto

```
mcp-server-ra2-jdbc/
├── src/
│   ├── main/
│   │   ├── java/com/dam/accesodatos/
│   │   │   ├── McpAccesoDatosRa2Application.java  [COMPLETO - Main Spring Boot]
│   │   │   ├── config/                            [COMPLETO - Configuración]
│   │   │   ├── model/                             [COMPLETO - User, DTOs]
│   │   │   └── ra2/                               [IMPLEMENTACIÓN ESTUDIANTES]
│   │   │       ├── DatabaseUserService.java       [COMPLETO - Interface con @Tool]
│   │   │       ├── DatabaseUserServiceImpl.java   [5 EJEMPLOS + 10 TODOs]
│   │   │       └── package-info.java              [COMPLETO - Documentación RA2]
│   │   └── resources/
│   │       ├── application.yml                    [COMPLETO - Config H2 + MCP]
│   │       ├── schema.sql                         [COMPLETO - CREATE TABLE users]
│   │       └── data.sql                           [COMPLETO - Datos de prueba]
│   └── test/
│       ├── java/com/dam/accesodatos/ra2/
│       │   └── DatabaseUserServiceTest.java       [TODO - Tests TDD]
│       └── resources/
│           ├── test-schema.sql                    [COMPLETO - Schema de tests]
│           └── test-data.sql                      [COMPLETO - Datos de tests]
├── build.gradle                                   [COMPLETO - Spring Boot + JDBC + H2]
├── settings.gradle                                [COMPLETO]
└── README.md                                      [Este archivo]
```

## 🚀 Inicio Rápido

### Pre-requisitos

- **Java 17** o superior
- **Gradle** (incluido via wrapper)
- **IntelliJ IDEA** recomendado (o cualquier IDE con soporte Gradle)

### Compilar el Proyecto

```bash
# Desde línea de comandos
./gradlew clean compileJava

# Desde IntelliJ IDEA
Panel Gradle → Tasks → build → build
```

### Ejecutar la Aplicación

```bash
# Desde línea de comandos
./gradlew bootRun

# Desde IntelliJ IDEA
Run → McpAccesoDatosRa2Application
```

El servidor arranca en **http://localhost:8082**

### Consola H2 Database

Para inspeccionar la base de datos:

1. Abrir: http://localhost:8082/h2-console
2. Configuración:
   - **JDBC URL**: `jdbc:h2:mem:ra2db`
   - **User Name**: `sa`
   - **Password**: (dejar vacío)
3. Conectar

## 📚 Implementación para Estudiantes

### Métodos Implementados (Ejemplos para Aprender)

#### 1. ✅ `testConnection()` - CE2.a
Ejemplo básico de conexión JDBC.

**Conceptos que muestra:**
- Try-with-resources
- Obtener Connection del DataSource
- Ejecutar query simple
- Procesar ResultSet
- Usar DatabaseMetaData

**Ubicación:** `DatabaseUserServiceImpl.java:55`

#### 2. ✅ `createUser()` - CE2.b
INSERT con PreparedStatement y getGeneratedKeys.

**Conceptos que muestra:**
- PreparedStatement para prevenir SQL injection
- Setear parámetros con tipos específicos
- `RETURN_GENERATED_KEYS`
- Manejar errores específicos (email duplicado)

**Ubicación:** `DatabaseUserServiceImpl.java:145`

#### 3. ✅ `findUserById()` - CE2.b
SELECT con navegación de ResultSet.

**Conceptos que muestra:**
- Query parametrizada con WHERE
- Navegar ResultSet con `rs.next()`
- Mapear columnas SQL a objeto Java
- Conversión de tipos (Long, String, Timestamp)

**Ubicación:** `DatabaseUserServiceImpl.java:203`

#### 4. ✅ `updateUser()` - CE2.b
UPDATE statement con validación.

**Conceptos que muestra:**
- Validar existencia antes de actualizar
- UPDATE con múltiples campos
- Actualizar timestamp automático
- Verificar filas afectadas

**Ubicación:** `DatabaseUserServiceImpl.java:242`

#### 5. ✅ `transferData()` - CE2.d
Transacción manual con commit/rollback.

**Conceptos que muestra:**
- Desactivar auto-commit: `conn.setAutoCommit(false)`
- Ejecutar múltiples operaciones
- COMMIT si todo OK
- ROLLBACK si hay error
- Restaurar auto-commit en finally

**Ubicación:** `DatabaseUserServiceImpl.java:453`

### Métodos TODO (Para Implementar)

| # | Método | CE | Dificultad | Prioridad |
|---|--------|----|-----------| ---------|
| 1 | `getConnectionInfo()` | CE2.a | Media | Alta |
| 2 | `deleteUser()` | CE2.b | Básica | Alta |
| 3 | `findAll()` | CE2.b | Básica | Alta |
| 4 | `findUsersByDepartment()` | CE2.c | Media | Alta |
| 5 | `searchUsers()` | CE2.c | Alta | Media |
| 6 | `findUsersWithPagination()` | CE2.c | Media | Alta |
| 7 | `batchInsertUsers()` | CE2.d | Media | Media |
| 8 | `getDatabaseInfo()` | CE2.e | Media | Media |
| 9 | `getTableColumns()` | CE2.e | Alta | Media |
| 10 | `executeCountByDepartment()` | CE2.f | Alta | Baja (Avanzado) |

**Cada método TODO incluye:**
- ✅ Descripción detallada de lo que debe hacer
- ✅ Pasos a seguir (algoritmo step-by-step)
- ✅ Clases JDBC requeridas
- ✅ Ejemplo de estructura de código
- ✅ Notas pedagógicas

## 🧪 Testing

### Ejecutar Tests

```bash
# Todos los tests (cuando se implementen)
./gradlew test

# Ver resultados detallados
./gradlew test --info

# Desde IntelliJ
Clic derecho en test/ → Run All Tests
```

### Estrategia TDD

1. **RED**: Ejecutar test → Falla (UnsupportedOperationException)
2. **GREEN**: Implementar método → Test pasa
3. **REFACTOR**: Mejorar código → Tests siguen pasando

## 📖 Clases JDBC Clave

### Connection Management con DatabaseConfig (JDBC Puro)
```java
// PATRÓN JDBC VANILLA - Sin Spring DataSource
try (Connection conn = DatabaseConfig.getConnection()) {
    // Trabajar con la conexión
    // DatabaseConfig usa DriverManager internamente
}
```

**Ventajas pedagógicas:**
- Los estudiantes ven `DriverManager.getConnection()` en acción
- No hay "magia" de inyección de dependencias
- Se aprende gestión manual de recursos

### PreparedStatement (Previene SQL Injection)
```java
String sql = "SELECT * FROM users WHERE id = ?";
try (Connection conn = DatabaseConfig.getConnection();
     PreparedStatement pstmt = conn.prepareStatement(sql)) {

    pstmt.setLong(1, userId);

    try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
            Long id = rs.getLong("id");
            String name = rs.getString("name");
            // ...
        }
    }
}
```

### Transacciones (Control Manual)
```java
// JDBC PURO - Sin transacciones de Spring
Connection conn = DatabaseConfig.getConnection();
try {
    conn.setAutoCommit(false);  // Inicio transacción MANUAL

    // Operación 1
    pstmt1.executeUpdate();

    // Operación 2
    pstmt2.executeUpdate();

    conn.commit();  // Confirmar si todo OK

} catch (SQLException e) {
    conn.rollback();  // Deshacer si error
    throw new RuntimeException(e);
} finally {
    conn.setAutoCommit(true);
    conn.close();
}
```

**Nota educativa**: Los estudiantes gestionan transacciones manualmente,
sin usar `@Transactional` de Spring.

### Batch Operations
```java
try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
    for (User user : users) {
        pstmt.setString(1, user.getName());
        pstmt.setString(2, user.getEmail());
        pstmt.addBatch();  // No ejecutar aún
    }

    int[] results = pstmt.executeBatch();  // Ejecutar todos
}
```

## 🔍 Debugging

### Ver Queries SQL Ejecutadas

En `application.yml`, logging está configurado en DEBUG:

```yaml
logging:
  level:
    org.springframework.jdbc: DEBUG
```

Verás en consola:
```
Executing SQL statement [INSERT INTO users ...]
```

### Verificar Datos en H2 Console

1. Abrir http://localhost:8082/h2-console
2. Ejecutar queries directas:
```sql
SELECT * FROM users;
SELECT * FROM users WHERE department = 'IT';
SELECT COUNT(*) FROM users GROUP BY department;
```

### Common Issues

**Error: "Table not found"**
- Verificar que `schema.sql` se ejecutó
- Revisar logs de inicio de aplicación

**Error: "Unique index violation"**
- Email duplicado (campo UNIQUE)
- Verificar constraint en `schema.sql`

**Error: "Parameter index out of range"**
- Índices de `pstmt.setXXX()` empiezan en 1, no en 0
- Contar placeholders `?` en SQL

## 📁 Base de Datos

### Schema

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    department VARCHAR(50) NOT NULL,
    role VARCHAR(50) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Datos Iniciales

El archivo `data.sql` inserta 8 usuarios de prueba en diferentes departamentos:
- IT: 3 usuarios
- HR: 2 usuarios
- Finance, Marketing, Sales: 1 usuario cada uno

## 🎓 Recursos Adicionales

### Documentación Java JDBC
- [JDBC Tutorial (Oracle)](https://docs.oracle.com/javase/tutorial/jdbc/)
- [PreparedStatement API](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/PreparedStatement.html)
- [ResultSet API](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/ResultSet.html)

### H2 Database
- [H2 Documentation](http://www.h2database.com/html/main.html)
- [H2 SQL Grammar](http://www.h2database.com/html/grammar.html)

### Spring Boot
- [Spring Boot JDBC](https://docs.spring.io/spring-boot/docs/current/reference/html/data.html#data.sql)

## 💡 Tips para Estudiantes

1. **Usa try-with-resources SIEMPRE** - Evita leaks de conexiones
2. **PreparedStatement > Statement** - Previene SQL injection
3. **Verifica filas afectadas** - `executeUpdate()` retorna int
4. **Mapea tipos correctamente** - SQL BIGINT → Java Long, SQL VARCHAR → Java String
5. **Maneja excepciones descriptivas** - `throw new RuntimeException("Error al buscar usuario: " + e.getMessage(), e)`
6. **Prueba en H2 Console primero** - Valida tus queries antes de implementar
7. **Lee los ejemplos implementados** - Sigue los patrones mostrados
8. **Commit frecuente** - Cada método que pase sus tests
9. **No uses JPA/Hibernate** - Este proyecto es sobre JDBC puro


### Entrega

- **Archivo**: `DatabaseUserServiceImpl.java` con todos los TODOs implementados
- **Tests**: Todos los tests en GREEN
- **Demo**: Mostrar funcionamiento vía H2 Console o tests

## 🤝 Soporte

- **Consultar ejemplos**: Revisar los 5 métodos implementados
- **Leer TODOs**: Cada método tiene instrucciones paso a paso
- **Debugging**: Usar H2 Console para validar queries
- **Profesor**: Consultar en clase sobre conceptos JDBC

---

**Proyecto educativo - Acceso a Datos 2º DAM**
**Versión**: 1.0.0
**Basado en**: Spring Boot 3.3.0 + H2 Database + JDBC
