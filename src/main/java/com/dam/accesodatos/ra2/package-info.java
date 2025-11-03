/**
 * RA2: Desarrolla aplicaciones que gestionan información almacenada mediante conectores
 *
 * <h2>Descripción del Resultado de Aprendizaje</h2>
 * <p>
 * Este paquete contiene las implementaciones para el RA2 del módulo de Acceso a Datos.
 * Los estudiantes aprenderán a usar JDBC (Java Database Connectivity) puro para interactuar
 * con bases de datos relacionales, sin usar frameworks ORM como Hibernate/JPA.
 * </p>
 *
 * <h2>Objetivo Pedagógico</h2>
 * <p>
 * Comprender los fundamentos del acceso a datos mediante JDBC:
 * <ul>
 *   <li>Gestión manual de conexiones con DataSource y Connection</li>
 *   <li>Uso de PreparedStatement para prevenir SQL injection</li>
 *   <li>Navegación y mapeo de ResultSet a objetos Java</li>
 *   <li>Control de transacciones con commit/rollback</li>
 *   <li>Manejo de metadatos de base de datos</li>
 *   <li>Uso de CallableStatement para stored procedures</li>
 * </ul>
 * </p>
 *
 * <h2>Criterios de Evaluación</h2>
 * <p>
 * <strong>CE2.a: Gestión de conexiones a bases de datos</strong>
 * <ul>
 *   <li>Establecimiento de conexiones usando {@link javax.sql.DataSource}</li>
 *   <li>Obtención de información de conexión con {@link java.sql.DatabaseMetaData}</li>
 *   <li>Cierre apropiado de recursos con try-with-resources</li>
 *   <li>Validación de conexiones con {@link java.sql.Connection#isClosed()}</li>
 * </ul>
 *
 * <strong>CE2.b: Operaciones CRUD con JDBC</strong>
 * <ul>
 *   <li><strong>CREATE:</strong> INSERT con {@link java.sql.PreparedStatement} y {@link java.sql.Statement#RETURN_GENERATED_KEYS}</li>
 *   <li><strong>READ:</strong> SELECT con navegación de {@link java.sql.ResultSet}</li>
 *   <li><strong>UPDATE:</strong> UPDATE statements con validación</li>
 *   <li><strong>DELETE:</strong> DELETE statements con confirmación</li>
 *   <li>Mapeo de tipos SQL a tipos Java (Long, String, Boolean, Timestamp)</li>
 * </ul>
 *
 * <strong>CE2.c: Consultas avanzadas y queries parametrizadas</strong>
 * <ul>
 *   <li>Cláusulas WHERE con múltiples condiciones</li>
 *   <li>Construcción dinámica de queries SQL</li>
 *   <li>Paginación con LIMIT y OFFSET</li>
 *   <li>Ordenamiento con ORDER BY</li>
 *   <li>Búsquedas con filtros opcionales</li>
 * </ul>
 *
 * <strong>CE2.d: Gestión de transacciones</strong>
 * <ul>
 *   <li>Control manual con {@link java.sql.Connection#setAutoCommit(boolean)}</li>
 *   <li>Commit exitoso con {@link java.sql.Connection#commit()}</li>
 *   <li>Rollback en caso de error con {@link java.sql.Connection#rollback()}</li>
 *   <li>Operaciones batch con {@link java.sql.PreparedStatement#addBatch()} y {@link java.sql.PreparedStatement#executeBatch()}</li>
 *   <li>Manejo de errores transaccionales</li>
 * </ul>
 *
 * <strong>CE2.e: Metadatos de bases de datos</strong>
 * <ul>
 *   <li>Información de BD con {@link java.sql.DatabaseMetaData}</li>
 *   <li>Estructura de tablas con {@link java.sql.DatabaseMetaData#getColumns(String, String, String, String)}</li>
 *   <li>Exploración de schemas y catálogos</li>
 *   <li>Información de índices y claves primarias</li>
 * </ul>
 *
 * <strong>CE2.f: Stored Procedures y Functions (Avanzado)</strong>
 * <ul>
 *   <li>Llamada a funciones con {@link java.sql.CallableStatement}</li>
 *   <li>Parámetros de entrada con {@link java.sql.CallableStatement#setString(int, String)}</li>
 *   <li>Parámetros de salida con {@link java.sql.CallableStatement#registerOutParameter(int, int)}</li>
 *   <li>Ejecución y obtención de resultados</li>
 * </ul>
 * </p>
 *
 * <h2>Tecnologías JDBC Requeridas</h2>
 *
 * <h3>CE2.a: Connection Management</h3>
 * <ul>
 *   <li>{@link javax.sql.DataSource} - Fuente de conexiones (inyectado por Spring)</li>
 *   <li>{@link java.sql.Connection} - Conexión activa a la base de datos</li>
 *   <li>{@link java.sql.DatabaseMetaData} - Metadatos de la base de datos</li>
 *   <li>{@link java.sql.DriverManager} - Gestión de drivers (opcional en Spring)</li>
 * </ul>
 *
 * <h3>CE2.b: CRUD Operations</h3>
 * <ul>
 *   <li>{@link java.sql.Statement} - Ejecución de SQL estático (básico)</li>
 *   <li>{@link java.sql.PreparedStatement} - SQL parametrizado (recomendado)</li>
 *   <li>{@link java.sql.ResultSet} - Resultados de consultas SELECT</li>
 *   <li>{@link java.sql.Statement#RETURN_GENERATED_KEYS} - Para obtener IDs autogenerados</li>
 *   <li>{@link java.sql.Timestamp} - Mapeo de fechas/horas</li>
 * </ul>
 *
 * <h3>CE2.c: Advanced Queries</h3>
 * <ul>
 *   <li>{@link java.sql.PreparedStatement} - Queries parametrizadas</li>
 *   <li>{@link java.lang.StringBuilder} - Construcción dinámica de SQL</li>
 *   <li>LIMIT/OFFSET - Paginación de resultados</li>
 *   <li>WHERE dinámico - Filtros opcionales</li>
 * </ul>
 *
 * <h3>CE2.d: Transactions</h3>
 * <ul>
 *   <li>{@link java.sql.Connection#setAutoCommit(boolean)} - Desactivar auto-commit</li>
 *   <li>{@link java.sql.Connection#commit()} - Confirmar cambios</li>
 *   <li>{@link java.sql.Connection#rollback()} - Deshacer cambios</li>
 *   <li>{@link java.sql.PreparedStatement#addBatch()} - Agregar al batch</li>
 *   <li>{@link java.sql.PreparedStatement#executeBatch()} - Ejecutar batch</li>
 * </ul>
 *
 * <h3>CE2.e: Metadata</h3>
 * <ul>
 *   <li>{@link java.sql.DatabaseMetaData} - Información de la BD</li>
 *   <li>{@link java.sql.ResultSetMetaData} - Información de columnas</li>
 *   <li>{@link java.sql.DatabaseMetaData#getTables(String, String, String, String[])} - Listar tablas</li>
 *   <li>{@link java.sql.DatabaseMetaData#getColumns(String, String, String, String)} - Información de columnas</li>
 * </ul>
 *
 * <h3>CE2.f: Stored Procedures</h3>
 * <ul>
 *   <li>{@link java.sql.CallableStatement} - Llamada a procedimientos/funciones</li>
 *   <li>{@link java.sql.Types} - Tipos de datos SQL</li>
 *   <li>{@link java.sql.CallableStatement#registerOutParameter(int, int)} - Parámetros OUT</li>
 * </ul>
 *
 * <h2>Patrones de Implementación Requeridos</h2>
 *
 * <h3>Try-with-resources (obligatorio)</h3>
 * <pre>{@code
 * try (Connection conn = dataSource.getConnection();
 *      PreparedStatement pstmt = conn.prepareStatement(sql);
 *      ResultSet rs = pstmt.executeQuery()) {
 *
 *     // Operaciones con la base de datos
 *
 * } catch (SQLException e) {
 *     throw new RuntimeException("Error: " + e.getMessage(), e);
 * }
 * }</pre>
 *
 * <h3>PreparedStatement con parámetros</h3>
 * <pre>{@code
 * String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
 *
 * try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
 *     pstmt.setString(1, "Juan Pérez");
 *     pstmt.setString(2, "juan@example.com");
 *     int rows = pstmt.executeUpdate();
 * }
 * }</pre>
 *
 * <h3>Navegación de ResultSet</h3>
 * <pre>{@code
 * try (ResultSet rs = pstmt.executeQuery()) {
 *     while (rs.next()) {
 *         Long id = rs.getLong("id");
 *         String name = rs.getString("name");
 *         // ... mapear a objeto
 *     }
 * }
 * }</pre>
 *
 * <h3>Transacción manual</h3>
 * <pre>{@code
 * Connection conn = dataSource.getConnection();
 * try {
 *     conn.setAutoCommit(false);
 *
 *     // Múltiples operaciones
 *     pstmt1.executeUpdate();
 *     pstmt2.executeUpdate();
 *
 *     conn.commit(); // Si todo OK
 *
 * } catch (SQLException e) {
 *     conn.rollback(); // Si error
 *     throw new RuntimeException(e);
 * } finally {
 *     conn.setAutoCommit(true);
 *     conn.close();
 * }
 * }</pre>
 *
 * <h2>Estructura del Paquete</h2>
 * <ul>
 *   <li>{@link com.dam.accesodatos.ra2.DatabaseUserService} - Interface con 15 métodos @Tool</li>
 *   <li>{@link com.dam.accesodatos.ra2.DatabaseUserServiceImpl} - Implementación (5 ejemplos + 10 TODOs)</li>
 * </ul>
 *
 * <h2>Métodos a Implementar por Estudiantes</h2>
 * <table border="1">
 *   <tr>
 *     <th>Método</th>
 *     <th>CE</th>
 *     <th>Prioridad</th>
 *     <th>Dificultad</th>
 *   </tr>
 *   <tr>
 *     <td>testConnection()</td>
 *     <td>CE2.a</td>
 *     <td>✅ EJEMPLO</td>
 *     <td>Básica</td>
 *   </tr>
 *   <tr>
 *     <td>getConnectionInfo()</td>
 *     <td>CE2.a</td>
 *     <td>⚠️ TODO</td>
 *     <td>Media</td>
 *   </tr>
 *   <tr>
 *     <td>createUser()</td>
 *     <td>CE2.b</td>
 *     <td>✅ EJEMPLO</td>
 *     <td>Media</td>
 *   </tr>
 *   <tr>
 *     <td>findUserById()</td>
 *     <td>CE2.b</td>
 *     <td>✅ EJEMPLO</td>
 *     <td>Media</td>
 *   </tr>
 *   <tr>
 *     <td>updateUser()</td>
 *     <td>CE2.b</td>
 *     <td>✅ EJEMPLO</td>
 *     <td>Media</td>
 *   </tr>
 *   <tr>
 *     <td>deleteUser()</td>
 *     <td>CE2.b</td>
 *     <td>⚠️ TODO</td>
 *     <td>Básica</td>
 *   </tr>
 *   <tr>
 *     <td>findAll()</td>
 *     <td>CE2.b</td>
 *     <td>⚠️ TODO</td>
 *     <td>Básica</td>
 *   </tr>
 *   <tr>
 *     <td>findUsersByDepartment()</td>
 *     <td>CE2.c</td>
 *     <td>⚠️ TODO</td>
 *     <td>Media</td>
 *   </tr>
 *   <tr>
 *     <td>searchUsers()</td>
 *     <td>CE2.c</td>
 *     <td>⚠️ TODO</td>
 *     <td>Alta</td>
 *   </tr>
 *   <tr>
 *     <td>findUsersWithPagination()</td>
 *     <td>CE2.c</td>
 *     <td>⚠️ TODO</td>
 *     <td>Media</td>
 *   </tr>
 *   <tr>
 *     <td>transferData()</td>
 *     <td>CE2.d</td>
 *     <td>✅ EJEMPLO</td>
 *     <td>Alta</td>
 *   </tr>
 *   <tr>
 *     <td>batchInsertUsers()</td>
 *     <td>CE2.d</td>
 *     <td>⚠️ TODO</td>
 *     <td>Media</td>
 *   </tr>
 *   <tr>
 *     <td>getDatabaseInfo()</td>
 *     <td>CE2.e</td>
 *     <td>⚠️ TODO</td>
 *     <td>Media</td>
 *   </tr>
 *   <tr>
 *     <td>getTableColumns()</td>
 *     <td>CE2.e</td>
 *     <td>⚠️ TODO</td>
 *     <td>Alta</td>
 *   </tr>
 *   <tr>
 *     <td>executeCountByDepartment()</td>
 *     <td>CE2.f</td>
 *     <td>⚠️ TODO (Avanzado)</td>
 *     <td>Alta</td>
 *   </tr>
 * </table>
 *
 * <h2>Base de Datos: H2</h2>
 * <p>
 * Este proyecto usa H2 Database en modo memoria para facilitar el aprendizaje:
 * <ul>
 *   <li>No requiere instalación externa</li>
 *   <li>Compatible con PostgreSQL mode</li>
 *   <li>Ideal para desarrollo y testing</li>
 *   <li>Console web disponible en http://localhost:8082/h2-console</li>
 * </ul>
 * </p>
 *
 * <h2>Evaluación</h2>
 * <p>
 * Los estudiantes serán evaluados en:
 * <ul>
 *   <li>Correcta implementación de los 10 métodos TODO</li>
 *   <li>Uso apropiado de try-with-resources</li>
 *   <li>Prevención de SQL injection con PreparedStatement</li>
 *   <li>Manejo correcto de transacciones</li>
 *   <li>Mapeo adecuado de tipos SQL a Java</li>
 *   <li>Gestión de errores con excepciones descriptivas</li>
 *   <li>Tests unitarios pasando (GREEN)</li>
 * </ul>
 * </p>
 *
 * @see com.dam.accesodatos.ra2.DatabaseUserService
 * @see com.dam.accesodatos.ra2.DatabaseUserServiceImpl
 * @see java.sql.Connection
 * @see java.sql.PreparedStatement
 * @see java.sql.ResultSet
 *
 * @author Proyecto Educativo MCP - RA2 JDBC
 * @version 1.0.0
 */
package com.dam.accesodatos.ra2;
