package com.dam.accesodatos.ra2;

import com.dam.accesodatos.config.DatabaseConfig;
import com.dam.accesodatos.model.User;
import com.dam.accesodatos.model.UserCreateDto;
import com.dam.accesodatos.model.UserQueryDto;
import com.dam.accesodatos.model.UserUpdateDto;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementación del servicio JDBC para gestión de usuarios
 *
 * ESTRUCTURA DE IMPLEMENTACIÓN:
 * - ✅ 5 MÉTODOS IMPLEMENTADOS (ejemplos para estudiantes)
 * - ❌ 10 MÉTODOS TODO (estudiantes deben implementar)
 *
 * MÉTODOS IMPLEMENTADOS (Ejemplos):
 * 1. testConnection() - Ejemplo básico de conexión JDBC
 * 2. createUser() - INSERT con PreparedStatement y getGeneratedKeys
 * 3. findUserById() - SELECT y mapeo de ResultSet a objeto
 * 4. updateUser() - UPDATE statement con validación
 * 5. transferData() - Transacción manual con commit/rollback
 *
 * MÉTODOS TODO (Estudiantes implementan):
 * 1. getConnectionInfo()
 * 2. deleteUser()
 * 3. findAll()
 * 4. findUsersByDepartment()
 * 5. searchUsers()
 * 6. findUsersWithPagination()
 * 7. batchInsertUsers()
 * 8. getDatabaseInfo()
 * 9. getTableColumns()
 * 10. executeCountByDepartment()
 */
@Service
public class DatabaseUserServiceImpl implements DatabaseUserService {

    // JDBC PURO - SIN Spring DataSource
    // Los estudiantes usan DatabaseConfig.getConnection() directamente
    // para obtener conexiones usando DriverManager

    // ========== CE2.a: Connection Management ==========

    /**
     * ✅ EJEMPLO IMPLEMENTADO 1/5: Prueba de conexión básica
     *
     * Este método muestra el patrón fundamental de JDBC PURO:
     * 1. Obtener conexión usando DriverManager (vía DatabaseConfig)
     * 2. Ejecutar una query simple
     * 3. Procesar resultados
     * 4. Cerrar recursos con try-with-resources
     */
    @Override
    public String testConnection() {
        // Patrón try-with-resources: cierra automáticamente Connection, Statement, ResultSet
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT 1 as test, DATABASE() as db_name")) {

            // Validar que la conexión está abierta
            if (conn.isClosed()) {
                throw new RuntimeException("La conexión está cerrada");
            }

            // Navegar al primer (y único) resultado
            if (rs.next()) {
                int testValue = rs.getInt("test");
                String dbName = rs.getString("db_name");

                // Obtener información adicional de la conexión
                DatabaseMetaData metaData = conn.getMetaData();
                String dbProduct = metaData.getDatabaseProductName();
                String dbVersion = metaData.getDatabaseProductVersion();

                return String.format("✓ Conexión exitosa a %s %s | Base de datos: %s | Test: %d",
                        dbProduct, dbVersion, dbName, testValue);
            } else {
                throw new RuntimeException("No se obtuvieron resultados de la query de prueba");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al probar la conexión: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, String> getConnectionInfo() {
        /*
         * TODO CE2.a: Implementar obtención de información de conexión
         *
         * Pasos requeridos:
         * 1. Obtener Connection usando DatabaseConfig.getConnection() con try-with-resources
         * 2. Obtener DatabaseMetaData: conn.getMetaData()
         * 3. Extraer información:
         *    - metaData.getURL() - URL de conexión
         *    - metaData.getUserName() - Usuario conectado
         *    - metaData.getDatabaseProductName() - Nombre de la BD
         *    - metaData.getDatabaseProductVersion() - Versión de la BD
         *    - metaData.getDriverName() - Nombre del driver JDBC
         *    - metaData.getDriverVersion() - Versión del driver
         *    - metaData.getMaxConnections() - Máximo de conexiones
         *    - metaData.isReadOnly() - Si es solo lectura
         * 4. Crear Map<String, String> con estos valores
         * 5. Retornar el mapa
         *
         * Clases JDBC requeridas:
         * - java.sql.Connection
         * - java.sql.DatabaseMetaData
         *
         * Ejemplo de estructura:
         * Map<String, String> info = new HashMap<>();
         * info.put("url", metaData.getURL());
         * info.put("user", metaData.getUserName());
         * ...
         */

        throw new UnsupportedOperationException("TODO: Implementar getConnectionInfo() usando DatabaseMetaData");
    }

    // ========== CE2.b: CRUD Operations ==========

    /**
     * ✅ EJEMPLO IMPLEMENTADO 2/5: INSERT con PreparedStatement
     *
     * Este método muestra cómo:
     * - Usar PreparedStatement para prevenir SQL injection
     * - Setear parámetros con tipos específicos
     * - Obtener IDs autogenerados con getGeneratedKeys()
     * - Manejar excepciones SQL
     */
    @Override
    public User createUser(UserCreateDto dto) {
        String sql = "INSERT INTO users (name, email, department, role, active, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Setear parámetros del PreparedStatement
            // Índices empiezan en 1, no en 0
            pstmt.setString(1, dto.getName());
            pstmt.setString(2, dto.getEmail());
            pstmt.setString(3, dto.getDepartment());
            pstmt.setString(4, dto.getRole());
            pstmt.setBoolean(5, true); // active por defecto
            pstmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now())); // created_at
            pstmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now())); // updated_at

            // Ejecutar INSERT y obtener número de filas afectadas
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new RuntimeException("Error: INSERT no afectó ninguna fila");
            }

            // Obtener el ID autogenerado
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long generatedId = generatedKeys.getLong(1);

                    // Crear objeto User con el ID generado
                    User newUser = new User(generatedId, dto.getName(), dto.getEmail(),
                            dto.getDepartment(), dto.getRole());
                    newUser.setActive(true);
                    newUser.setCreatedAt(LocalDateTime.now());
                    newUser.setUpdatedAt(LocalDateTime.now());

                    return newUser;
                } else {
                    throw new RuntimeException("Error: INSERT exitoso pero no se generó ID");
                }
            }

        } catch (SQLException e) {
            // Manejar errores específicos como email duplicado
            if (e.getMessage().contains("Unique index or primary key violation")) {
                throw new RuntimeException("Error: El email '" + dto.getEmail() + "' ya está registrado", e);
            }
            throw new RuntimeException("Error al crear usuario: " + e.getMessage(), e);
        }
    }

    /**
     * ✅ EJEMPLO IMPLEMENTADO 3/5: SELECT y mapeo de ResultSet
     *
     * Este método muestra cómo:
     * - Usar PreparedStatement para queries parametrizadas
     * - Navegar ResultSet con rs.next()
     * - Mapear columnas SQL a campos Java
     * - Manejar tipos de datos (Long, String, Boolean, Timestamp)
     */
    @Override
    public User findUserById(Long id) {
        String sql = "SELECT id, name, email, department, role, active, created_at, updated_at " +
                     "FROM users WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Setear parámetro WHERE id = ?
            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                // next() retorna true si hay un resultado, false si no
                if (rs.next()) {
                    // Mapear ResultSet a objeto User
                    return mapResultSetToUser(rs);
                } else {
                    // No se encontró usuario con ese ID
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario con ID " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * ✅ EJEMPLO IMPLEMENTADO 4/5: UPDATE statement
     *
     * Este método muestra cómo:
     * - Validar que un registro existe antes de actualizar
     * - Construir UPDATE statement con campos opcionales
     * - Actualizar solo los campos proporcionados
     * - Verificar filas afectadas
     */
    @Override
    public User updateUser(Long id, UserUpdateDto dto) {
        // Primero verificar que el usuario existe
        User existing = findUserById(id);
        if (existing == null) {
            throw new RuntimeException("No se encontró usuario con ID " + id);
        }

        // Aplicar actualizaciones del DTO al usuario existente
        dto.applyTo(existing);

        // Construir UPDATE statement
        String sql = "UPDATE users SET name = ?, email = ?, department = ?, role = ?, " +
                     "active = ?, updated_at = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Setear todos los parámetros (incluso los no modificados)
            pstmt.setString(1, existing.getName());
            pstmt.setString(2, existing.getEmail());
            pstmt.setString(3, existing.getDepartment());
            pstmt.setString(4, existing.getRole());
            pstmt.setBoolean(5, existing.getActive());
            pstmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setLong(7, id);

            // Ejecutar UPDATE
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new RuntimeException("Error: UPDATE no afectó ninguna fila");
            }

            // Retornar usuario actualizado
            return findUserById(id);

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar usuario con ID " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteUser(Long id) {
        /*
         * TODO CE2.b: Implementar eliminación de usuario
         *
         * Pasos requeridos:
         * 1. Crear SQL DELETE: "DELETE FROM users WHERE id = ?"
         * 2. Obtener Connection con try-with-resources
         * 3. Crear PreparedStatement con el SQL DELETE
         * 4. Setear parámetro: pstmt.setLong(1, id)
         * 5. Ejecutar: int affectedRows = pstmt.executeUpdate()
         * 6. Retornar true si affectedRows > 0, false en caso contrario
         *
         * Clases JDBC requeridas:
         * - java.sql.Connection
         * - java.sql.PreparedStatement
         * - Método: executeUpdate()
         *
         * Notas:
         * - executeUpdate() retorna el número de filas eliminadas
         * - Si affectedRows == 0, significa que no existía usuario con ese ID
         * - Si affectedRows > 1, hay un error grave (IDs duplicados)
         */

        throw new UnsupportedOperationException("TODO: Implementar deleteUser() usando DELETE statement");
    }

    @Override
    public List<User> findAll() {
        /*
         * TODO CE2.b: Implementar consulta de todos los usuarios
         *
         * Pasos requeridos:
         * 1. Crear SQL SELECT: "SELECT * FROM users ORDER BY created_at DESC"
         * 2. Obtener Connection con try-with-resources
         * 3. Crear Statement o PreparedStatement (sin parámetros en este caso)
         * 4. Ejecutar query: ResultSet rs = stmt.executeQuery(sql)
         * 5. Crear List<User> users = new ArrayList<>()
         * 6. Iterar ResultSet: while (rs.next()) { ... }
         * 7. En cada iteración, mapear fila a User usando mapResultSetToUser(rs)
         * 8. Agregar cada User a la lista: users.add(user)
         * 9. Retornar la lista
         *
         * Clases JDBC requeridas:
         * - java.sql.Connection
         * - java.sql.Statement o PreparedStatement
         * - java.sql.ResultSet
         * - Métodos: next(), mapResultSetToUser()
         *
         * Ejemplo de estructura:
         * List<User> users = new ArrayList<>();
         * while (rs.next()) {
         *     users.add(mapResultSetToUser(rs));
         * }
         * return users;
         */

        throw new UnsupportedOperationException("TODO: Implementar findAll() usando SELECT");
    }

    // ========== CE2.c: Advanced Queries ==========

    @Override
    public List<User> findUsersByDepartment(String department) {
        /*
         * TODO CE2.c: Implementar búsqueda por departamento
         *
         * Pasos requeridos:
         * 1. Crear SQL SELECT con WHERE:
         *    "SELECT * FROM users WHERE department = ? AND active = ? ORDER BY name"
         * 2. Obtener Connection y PreparedStatement con try-with-resources
         * 3. Setear parámetros:
         *    - pstmt.setString(1, department)
         *    - pstmt.setBoolean(2, true)  // solo usuarios activos
         * 4. Ejecutar query: ResultSet rs = pstmt.executeQuery()
         * 5. Crear List<User> y usar while (rs.next()) para iterar
         * 6. Mapear cada fila a User y agregar a la lista
         * 7. Retornar lista
         *
         * Clases JDBC requeridas:
         * - java.sql.PreparedStatement
         * - java.sql.ResultSet
         *
         * Variación avanzada (opcional):
         * - Permitir búsqueda case-insensitive: WHERE LOWER(department) = LOWER(?)
         * - Permitir búsqueda parcial: WHERE department LIKE ?
         */

        throw new UnsupportedOperationException("TODO: Implementar findUsersByDepartment() con filtro WHERE");
    }

    @Override
    public List<User> searchUsers(UserQueryDto query) {
        /*
         * TODO CE2.c: Implementar búsqueda dinámica con múltiples filtros
         *
         * Pasos requeridos:
         * 1. Crear StringBuilder para construir SQL dinámico
         * 2. Comenzar con: "SELECT * FROM users WHERE 1=1"
         * 3. Para cada filtro presente en query, agregar cláusula AND:
         *    - Si query.getDepartment() != null: sql.append(" AND department = ?")
         *    - Si query.getRole() != null: sql.append(" AND role = ?")
         *    - Si query.getActive() != null: sql.append(" AND active = ?")
         * 4. Agregar ORDER BY y paginación:
         *    - sql.append(" ORDER BY created_at DESC LIMIT ? OFFSET ?")
         * 5. Crear PreparedStatement con el SQL construido
         * 6. Setear parámetros en orden (importante mantener el orden):
         *    - int paramIndex = 1
         *    - Si department != null: pstmt.setString(paramIndex++, query.getDepartment())
         *    - Si role != null: pstmt.setString(paramIndex++, query.getRole())
         *    - Si active != null: pstmt.setBoolean(paramIndex++, query.getActive())
         *    - pstmt.setInt(paramIndex++, query.getLimit())
         *    - pstmt.setInt(paramIndex++, query.getOffset())
         * 7. Ejecutar query y mapear resultados
         *
         * Clases JDBC requeridas:
         * - java.sql.PreparedStatement
         * - java.lang.StringBuilder (para SQL dinámico)
         *
         * Ejemplo de construcción dinámica:
         * StringBuilder sql = new StringBuilder("SELECT * FROM users WHERE 1=1");
         * if (query.getDepartment() != null) {
         *     sql.append(" AND department = ?");
         * }
         * ...
         */

        throw new UnsupportedOperationException("TODO: Implementar searchUsers() con filtros dinámicos");
    }

    @Override
    public List<User> findUsersWithPagination(int offset, int limit) {
        /*
         * TODO CE2.c: Implementar paginación simple
         *
         * Pasos requeridos:
         * 1. Crear SQL con LIMIT y OFFSET:
         *    "SELECT * FROM users ORDER BY created_at DESC LIMIT ? OFFSET ?"
         * 2. Obtener Connection y PreparedStatement
         * 3. Setear parámetros:
         *    - pstmt.setInt(1, limit)   // cuántos registros
         *    - pstmt.setInt(2, offset)  // cuántos saltar
         * 4. Ejecutar query y mapear resultados a lista
         *
         * Clases JDBC requeridas:
         * - java.sql.PreparedStatement
         * - Método: setInt() para LIMIT y OFFSET
         *
         * Ejemplos de uso:
         * - findUsersWithPagination(0, 10)  -> Primeros 10 usuarios
         * - findUsersWithPagination(10, 10) -> Usuarios 11-20
         * - findUsersWithPagination(20, 10) -> Usuarios 21-30
         */

        throw new UnsupportedOperationException("TODO: Implementar findUsersWithPagination() con LIMIT/OFFSET");
    }

    // ========== CE2.d: Transactions ==========

    /**
     * ✅ EJEMPLO IMPLEMENTADO 5/5: Transacción manual con commit/rollback
     *
     * Este método muestra cómo:
     * - Desactivar auto-commit para control manual de transacciones
     * - Realizar múltiples operaciones en una transacción
     * - Hacer commit si todo tiene éxito
     * - Hacer rollback si hay algún error
     * - Restaurar auto-commit al estado original
     */
    @Override
    public boolean transferData(List<User> users) {
        Connection conn = null;

        try {
            // Obtener conexión
            conn = DatabaseConfig.getConnection();

            // IMPORTANTE: Desactivar auto-commit para control manual
            conn.setAutoCommit(false);

            String sql = "INSERT INTO users (name, email, department, role, active, created_at, updated_at) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Insertar cada usuario en la transacción
                for (User user : users) {
                    pstmt.setString(1, user.getName());
                    pstmt.setString(2, user.getEmail());
                    pstmt.setString(3, user.getDepartment());
                    pstmt.setString(4, user.getRole());
                    pstmt.setBoolean(5, user.getActive() != null ? user.getActive() : true);
                    pstmt.setTimestamp(6, Timestamp.valueOf(
                            user.getCreatedAt() != null ? user.getCreatedAt() : LocalDateTime.now()));
                    pstmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));

                    pstmt.executeUpdate();
                }
            }

            // Si llegamos aquí, todas las inserciones fueron exitosas
            // COMMIT: hacer permanentes los cambios
            conn.commit();

            return true;

        } catch (SQLException e) {
            // Si hubo algún error, deshacer TODOS los cambios
            if (conn != null) {
                try {
                    // ROLLBACK: deshacer todos los cambios de la transacción
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    throw new RuntimeException("Error crítico en rollback: " + rollbackEx.getMessage(), rollbackEx);
                }
            }

            throw new RuntimeException("Error en transacción, se hizo rollback: " + e.getMessage(), e);

        } finally {
            // IMPORTANTE: Restaurar auto-commit y cerrar conexión
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restaurar estado original
                    conn.close();
                } catch (SQLException e) {
                    // Registrar error pero no lanzar excepción en finally
                    System.err.println("Error al cerrar conexión: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public int batchInsertUsers(List<User> users) {
        /*
         * TODO CE2.d: Implementar inserción por lotes (batch)
         *
         * Pasos requeridos:
         * 1. Crear SQL INSERT (igual que createUser)
         * 2. Obtener Connection y PreparedStatement
         * 3. Para cada usuario en la lista:
         *    - Setear parámetros del PreparedStatement
         *    - Llamar pstmt.addBatch() en lugar de executeUpdate()
         * 4. Después del bucle, ejecutar todos los batches:
         *    - int[] results = pstmt.executeBatch()
         * 5. Procesar resultados:
         *    - Cada elemento en results[] indica filas afectadas por ese INSERT
         *    - Contar cuántos fueron exitosos (> 0)
         * 6. Retornar el total de usuarios insertados
         *
         * Clases JDBC requeridas:
         * - java.sql.PreparedStatement
         * - Métodos: addBatch(), executeBatch()
         *
         * Ventajas del batch:
         * - Más eficiente que insertar uno por uno
         * - Reduce round-trips a la base de datos
         * - Puede procesar miles de registros rápidamente
         *
         * Ejemplo de estructura:
         * for (User user : users) {
         *     pstmt.setString(1, user.getName());
         *     // ... setear otros parámetros ...
         *     pstmt.addBatch();  // Agregar al batch, no ejecutar aún
         * }
         * int[] results = pstmt.executeBatch();  // Ejecutar todos de una vez
         *
         * // Contar exitosos
         * int count = 0;
         * for (int result : results) {
         *     if (result > 0) count++;
         * }
         * return count;
         */

        throw new UnsupportedOperationException("TODO: Implementar batchInsertUsers() usando addBatch/executeBatch");
    }

    // ========== CE2.e: Metadata ==========

    @Override
    public String getDatabaseInfo() {
        /*
         * TODO CE2.e: Implementar obtención de metadatos de la base de datos
         *
         * Pasos requeridos:
         * 1. Obtener Connection con try-with-resources
         * 2. Obtener DatabaseMetaData: DatabaseMetaData meta = conn.getMetaData()
         * 3. Extraer información interesante:
         *    - meta.getDatabaseProductName() - Nombre de la BD (ej: H2)
         *    - meta.getDatabaseProductVersion() - Versión
         *    - meta.getDriverName() - Nombre del driver JDBC
         *    - meta.getDriverVersion() - Versión del driver
         *    - meta.getURL() - URL de conexión
         *    - meta.getUserName() - Usuario conectado
         *    - meta.getMaxConnections() - Máximo de conexiones
         *    - meta.getMaxStatements() - Máximo de statements
         *    - meta.supportsBatchUpdates() - Si soporta batch
         *    - meta.supportsTransactions() - Si soporta transacciones
         * 4. Formatear en un String legible (puede ser multilinea o JSON)
         * 5. Retornar el String con toda la información
         *
         * Clases JDBC requeridas:
         * - java.sql.DatabaseMetaData
         * - Múltiples métodos get*() para obtener información
         *
         * Ejemplo de formato de salida:
         * return String.format(
         *     "Base de Datos: %s %s\n" +
         *     "Driver JDBC: %s %s\n" +
         *     "URL: %s\n" +
         *     "Usuario: %s\n" +
         *     "Máx. Conexiones: %d\n" +
         *     "Soporta Batch: %b\n" +
         *     "Soporta Transacciones: %b",
         *     meta.getDatabaseProductName(),
         *     meta.getDatabaseProductVersion(),
         *     ...
         * );
         */

        throw new UnsupportedOperationException("TODO: Implementar getDatabaseInfo() usando DatabaseMetaData");
    }

    @Override
    public List<Map<String, Object>> getTableColumns(String tableName) {
        /*
         * TODO CE2.e: Implementar obtención de metadatos de columnas
         *
         * Pasos requeridos:
         * 1. Obtener Connection y DatabaseMetaData
         * 2. Llamar meta.getColumns():
         *    ResultSet columns = meta.getColumns(null, null, tableName.toUpperCase(), null)
         *    - Primer parámetro: catalog (null para actual)
         *    - Segundo parámetro: schema (null para actual)
         *    - Tercer parámetro: nombre de tabla (en mayúsculas para H2)
         *    - Cuarto parámetro: patrón de columna (null para todas)
         * 3. Iterar ResultSet de columnas: while (columns.next())
         * 4. Para cada columna, extraer información:
         *    - columns.getString("COLUMN_NAME") - Nombre de columna
         *    - columns.getInt("DATA_TYPE") - Tipo SQL (código numérico)
         *    - columns.getString("TYPE_NAME") - Nombre del tipo (VARCHAR, BIGINT, etc.)
         *    - columns.getInt("COLUMN_SIZE") - Tamaño
         *    - columns.getString("IS_NULLABLE") - "YES" o "NO"
         *    - columns.getString("COLUMN_DEF") - Valor por defecto
         * 5. Crear Map<String, Object> para cada columna con esta info
         * 6. Agregar cada mapa a List<Map<String, Object>>
         * 7. Retornar la lista
         *
         * Clases JDBC requeridas:
         * - java.sql.DatabaseMetaData
         * - Método: getColumns(catalog, schema, table, columnPattern)
         *
         * Ejemplo de estructura:
         * List<Map<String, Object>> columnsList = new ArrayList<>();
         *
         * while (columns.next()) {
         *     Map<String, Object> columnInfo = new HashMap<>();
         *     columnInfo.put("name", columns.getString("COLUMN_NAME"));
         *     columnInfo.put("type", columns.getString("TYPE_NAME"));
         *     columnInfo.put("size", columns.getInt("COLUMN_SIZE"));
         *     columnInfo.put("nullable", columns.getString("IS_NULLABLE"));
         *     columnsList.add(columnInfo);
         * }
         *
         * return columnsList;
         */

        throw new UnsupportedOperationException("TODO: Implementar getTableColumns() usando DatabaseMetaData.getColumns()");
    }

    // ========== CE2.f: Stored Procedures ==========

    @Override
    public int executeCountByDepartment(String department) {
        /*
         * TODO CE2.f: Implementar llamada a función almacenada con CallableStatement
         *
         * Pasos requeridos (opción avanzada con función real):
         * 1. Crear función en H2 primero (ejecutar una vez):
         *    CREATE ALIAS IF NOT EXISTS COUNT_BY_DEPT AS $$
         *        int count(java.sql.Connection conn, String dept) throws java.sql.SQLException {
         *            java.sql.PreparedStatement ps = conn.prepareStatement(
         *                "SELECT COUNT(*) FROM users WHERE department = ? AND active = true"
         *            );
         *            ps.setString(1, dept);
         *            java.sql.ResultSet rs = ps.executeQuery();
         *            rs.next();
         *            return rs.getInt(1);
         *        }
         *    $$;
         *
         * 2. Llamar función con CallableStatement:
         *    - Sintaxis para función que retorna valor: {? = call FUNCTION_NAME(?)}
         *    - CallableStatement cstmt = conn.prepareCall("{? = call COUNT_BY_DEPT(?)}")
         * 3. Registrar parámetro de salida:
         *    - cstmt.registerOutParameter(1, Types.INTEGER)
         * 4. Setear parámetro de entrada:
         *    - cstmt.setString(2, department)
         * 5. Ejecutar:
         *    - cstmt.execute()
         * 6. Obtener resultado:
         *    - int count = cstmt.getInt(1)
         * 7. Retornar count
         *
         * Alternativa simple (para aprendizaje básico):
         * Si no se puede crear la función, implementar con query directa:
         *
         * String sql = "SELECT COUNT(*) FROM users WHERE department = ? AND active = true";
         * try (Connection conn = DatabaseConfig.getConnection();
         *      PreparedStatement pstmt = conn.prepareStatement(sql)) {
         *     pstmt.setString(1, department);
         *     try (ResultSet rs = pstmt.executeQuery()) {
         *         return rs.next() ? rs.getInt(1) : 0;
         *     }
         * }
         *
         * Clases JDBC requeridas:
         * - java.sql.CallableStatement (para stored procedures)
         * - java.sql.Types (para tipos de parámetros)
         * - Métodos: prepareCall(), registerOutParameter(), execute(), getInt()
         *
         * Nota pedagógica:
         * CallableStatement se usa principalmente con bases de datos como PostgreSQL,
         * Oracle, SQL Server que tienen procedimientos almacenados nativos.
         * En H2, se simula con ALIAS a funciones Java.
         */

        throw new UnsupportedOperationException("TODO: Implementar executeCountByDepartment() con CallableStatement o query directa");
    }

    // ========== HELPER METHODS ==========

    /**
     * Método auxiliar para mapear ResultSet a objeto User
     *
     * Este método se usa en múltiples lugares para evitar duplicación de código.
     * Extrae todas las columnas del ResultSet y crea un objeto User.
     *
     * @param rs ResultSet posicionado en una fila válida
     * @return User object con datos de la fila
     * @throws SQLException si hay error al leer el ResultSet
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();

        // Mapear tipos primitivos y objetos
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setDepartment(rs.getString("department"));
        user.setRole(rs.getString("role"));
        user.setActive(rs.getBoolean("active"));

        // Mapear Timestamps a LocalDateTime
        Timestamp createdAtTimestamp = rs.getTimestamp("created_at");
        if (createdAtTimestamp != null) {
            user.setCreatedAt(createdAtTimestamp.toLocalDateTime());
        }

        Timestamp updatedAtTimestamp = rs.getTimestamp("updated_at");
        if (updatedAtTimestamp != null) {
            user.setUpdatedAt(updatedAtTimestamp.toLocalDateTime());
        }

        return user;
    }
}
