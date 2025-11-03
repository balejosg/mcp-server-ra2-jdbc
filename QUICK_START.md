# 🚀 QUICK START - MCP Server RA2 JDBC

Guía rápida para arrancar y probar el proyecto.

## ⚠️ IMPORTANTE: Este Proyecto Usa JDBC Puro

**NO usamos Spring DataSource** - Este proyecto enseña JDBC vanilla usando `DriverManager`:

✅ **SÍ usamos:**
- `DatabaseConfig.getConnection()` → `DriverManager.getConnection()`
- `Class.forName()` para cargar el driver H2
- Gestión manual de conexiones con try-with-resources

❌ **NO usamos:**
- Spring `DataSource` ni `@Autowired DataSource`
- `JdbcTemplate` de Spring
- Inicialización automática de Spring Boot

**Razón pedagógica:** Los estudiantes aprenden JDBC desde cero antes de usar abstracciones.

---

## ⚡ Inicio Rápido (5 minutos)

### 1. Compilar
```bash
cd /datos_replicados/Bruno/mcp-server-ra2-jdbc
./gradlew clean compileJava
```
**Esperado**: `BUILD SUCCESSFUL` ✅

### 2. Ejecutar
```bash
./gradlew bootRun
```
**Esperado**:
```
Started McpAccesoDatosRa2Application in X seconds
Server running on port 8082
```

### 3. Acceder a H2 Console
1. Abrir navegador: http://localhost:8082/h2-console
2. JDBC URL: `jdbc:h2:mem:ra2db`
3. User: `sa`
4. Password: (vacío)
5. Click "Connect"

### 4. Verificar Datos
```sql
-- Ver todos los usuarios
SELECT * FROM users;

-- Debería mostrar 8 usuarios

-- Ver usuarios por departamento
SELECT department, COUNT(*) as total
FROM users
GROUP BY department;

-- Resultado esperado:
-- IT: 3 usuarios
-- HR: 2 usuarios
-- Finance, Marketing, Sales: 1 cada uno
```

---

## 🧪 Probar Métodos Implementados

### Desde Java (crear clase de prueba)

Crear `src/test/java/QuickTest.java`:

```java
import com.dam.accesodatos.McpAccesoDatosRa2Application;
import com.dam.accesodatos.model.User;
import com.dam.accesodatos.model.UserCreateDto;
import com.dam.accesodatos.ra2.DatabaseUserService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

public class QuickTest {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(McpAccesoDatosRa2Application.class, args);
        DatabaseUserService service = ctx.getBean(DatabaseUserService.class);

        // Test 1: testConnection()
        System.out.println("=== TEST 1: testConnection ===");
        String result = service.testConnection();
        System.out.println(result);

        // Test 2: createUser()
        System.out.println("\n=== TEST 2: createUser ===");
        UserCreateDto dto = new UserCreateDto("Test User", "test@example.com", "IT", "Developer");
        User created = service.createUser(dto);
        System.out.println("Created user with ID: " + created.getId());

        // Test 3: findUserById()
        System.out.println("\n=== TEST 3: findUserById ===");
        User found = service.findUserById(created.getId());
        System.out.println("Found: " + found.getName() + " (" + found.getEmail() + ")");

        // Test 4: updateUser()
        System.out.println("\n=== TEST 4: updateUser ===");
        UserUpdateDto updateDto = new UserUpdateDto();
        updateDto.setRole("Senior Developer");
        User updated = service.updateUser(created.getId(), updateDto);
        System.out.println("Updated role to: " + updated.getRole());

        System.out.println("\n✅ All implemented methods work!");
    }
}
```

### Ejecutar:
```bash
./gradlew compileJava
java -cp "build/classes/java/main:$(./gradlew -q printClasspath)" QuickTest
```

---

## 📊 Verificar Estructura

### Ver archivos creados:
```bash
find . -type f \( -name "*.java" -o -name "*.sql" -o -name "*.yml" -o -name "*.md" \) | sort
```

### Contar líneas de código:
```bash
# Java
find src/main/java -name "*.java" -exec wc -l {} + | tail -1

# SQL
find src -name "*.sql" -exec wc -l {} + | tail -1

# Total
find src -name "*.java" -o -name "*.sql" | xargs wc -l | tail -1
```

---

## 🔍 Verificar Métodos TODO

```bash
# Ver todos los TODOs
grep -n "TODO CE2" src/main/java/com/dam/accesodatos/ra2/DatabaseUserServiceImpl.java

# Resultado esperado: 10 líneas con TODOs
```

---

## 🧩 Próximos Pasos para Estudiantes

### Método Más Fácil: deleteUser()

1. Abrir `DatabaseUserServiceImpl.java`
2. Buscar método `deleteUser()` (línea ~284)
3. Leer las instrucciones del TODO
4. Implementar siguiendo el patrón de `findUserById()`
5. Compilar: `./gradlew compileJava`
6. Probar en H2 Console:
```sql
-- Insertar usuario de prueba
INSERT INTO users (name, email, department, role) VALUES ('Delete Me', 'delete@test.com', 'IT', 'Test');

-- Anotar el ID generado
SELECT id FROM users WHERE email = 'delete@test.com';

-- Desde Java, llamar: service.deleteUser(ID);

-- Verificar que fue eliminado
SELECT * FROM users WHERE email = 'delete@test.com';
-- Debería retornar 0 filas
```

### Implementación Sugerida:

```java
@Override
public boolean deleteUser(Long id) {
    String sql = "DELETE FROM users WHERE id = ?";

    // JDBC PURO - Usar DatabaseConfig.getConnection() (NO dataSource)
    try (Connection conn = DatabaseConfig.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setLong(1, id);
        int affectedRows = pstmt.executeUpdate();

        return affectedRows > 0;

    } catch (SQLException e) {
        throw new RuntimeException("Error al eliminar usuario con ID " + id + ": " + e.getMessage(), e);
    }
}
```

---

## 📋 Checklist de Verificación

### ✅ Compilación
- [ ] `./gradlew clean compileJava` → BUILD SUCCESSFUL
- [ ] No hay errores de sintaxis
- [ ] Todas las dependencias se resuelven

### ✅ Ejecución
- [ ] `./gradlew bootRun` arranca sin errores
- [ ] Logs muestran: "Started McpAccesoDatosRa2Application"
- [ ] Puerto 8082 está escuchando
- [ ] H2 Console accesible en http://localhost:8082/h2-console

### ✅ Base de Datos
- [ ] Tabla `users` existe
- [ ] 8 usuarios pre-cargados
- [ ] Queries SQL funcionan desde H2 Console

### ✅ Métodos Implementados
- [ ] `testConnection()` retorna mensaje de conexión exitosa
- [ ] `createUser()` inserta y retorna usuario con ID
- [ ] `findUserById()` encuentra usuarios existentes
- [ ] `updateUser()` modifica usuarios
- [ ] `transferData()` funciona con transacciones

### ✅ Métodos TODO
- [ ] 10 métodos tienen `throw new UnsupportedOperationException(...)`
- [ ] Cada TODO tiene comentarios con instrucciones
- [ ] Estructura de código sugerida presente

### ✅ Documentación
- [ ] README.md completo
- [ ] CLAUDE.md completo
- [ ] package-info.java documentado
- [ ] JavaDoc en todos los métodos

---

## 🐛 Troubleshooting

### Error: "Port 8082 already in use"

Otro proceso usa el puerto. Cambiar en `application.yml`:
```yaml
server:
  port: 8083  # O cualquier puerto libre
```

### Error: "Table 'USERS' not found"

La base de datos no se inicializó correctamente. Verificar logs:
```bash
./gradlew bootRun | grep "Inicializando base de datos"
```

Debería mostrar:
```
Inicializando base de datos con JDBC puro
(SIN Spring DataSource - Usando DriverManager)
✓ Driver JDBC H2 cargado correctamente
✓ Schema creado correctamente
✓ Datos de prueba insertados (8 usuarios)
```

### Error: "Cannot find symbol: class DatabaseConfig"

Compilar con Gradle, no directamente con javac:
```bash
./gradlew compileJava
```

El proyecto usa **JDBC puro** (NO Spring DataSource). Si ves errores relacionados con
DataSource, asegúrate de usar `DatabaseConfig.getConnection()` en su lugar.

### Métodos implementados no funcionan

Verificar que Spring detectó el @Service:
```bash
./gradlew bootRun | grep DatabaseUserServiceImpl
```

Debería mostrar: `Bean 'databaseUserServiceImpl' created`

---

## 💡 Tips Útiles

### Limpiar y Reconstruir
```bash
./gradlew clean build
```

### Ver dependencias
```bash
./gradlew dependencies
```

### Ejecutar en modo debug
```bash
./gradlew bootRun --debug-jvm
```

### Ver logs de SQL
Ya está activado en `application.yml`:
```yaml
logging:
  level:
    org.springframework.jdbc: DEBUG
```

Verás en consola:
```
Executing SQL statement [INSERT INTO users ...]
```

---

## 📚 Documentación Completa

- **README.md** - Guía completa para estudiantes
- **CLAUDE.md** - Instrucciones para AI assistants
- **RESUMEN_PROYECTO.md** - Resumen ejecutivo del proyecto
- **Este archivo (QUICK_START.md)** - Inicio rápido

---

**¡Listo para empezar! 🚀**

Cualquier duda, consultar README.md o CLAUDE.md.
