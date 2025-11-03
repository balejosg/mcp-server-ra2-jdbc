# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an educational MCP (Model Context Protocol) server for the RA2 module "Acceso a Datos mediante Conectores (JDBC)" in DAM (Desarrollo de Aplicaciones Multiplataforma).

**IMPORTANT:** This project uses **JDBC VANILLA (pure JDBC)** WITHOUT Spring DataSource, JdbcTemplate, or any Spring data abstractions.

Students learn fundamental JDBC using:
- `DriverManager.getConnection()` (via DatabaseConfig helper)
- Manual connection management with try-with-resources
- `Class.forName()` for explicit driver loading
- Manual transaction control (no @Transactional)

This approach is pedagogically superior because students understand the complete JDBC lifecycle before learning Spring abstractions.

## Common Development Commands

### Build and Run

**From IntelliJ IDEA:**
- **Build**: Build Project button (Ctrl+F9) or Gradle panel → Tasks → build → build
- **Run server**: Run button next to McpAccesoDatosRa2Application.java or Gradle panel → Tasks → application → bootRun
- **Gradle panel**: View → Tool Windows → Gradle

**From command line (using Gradle wrapper):**
```bash
# Clean and compile
./gradlew clean compileJava

# Run all tests (will fail for TODO methods - this is expected)
./gradlew test

# Run specific test
./gradlew test --tests DatabaseUserServiceTest.testCreateUser

# Run with debug logging
./gradlew test --debug

# Start the MCP server
./gradlew bootRun
```

### Database Access

```bash
# H2 Console (while server is running)
# Open browser: http://localhost:8082/h2-console
# JDBC URL: jdbc:h2:mem:ra2db
# User: sa
# Password: (leave empty)
```

### Project Structure Commands

```bash
# Verify compilation
./gradlew compileJava

# Check dependencies
./gradlew dependencies
```

## Architecture Overview

### Core Components

**Main Application**: `McpAccesoDatosRa2Application.java` - Spring Boot entry point with educational MCP server configuration.

**Service Layer**:
- `DatabaseUserService.java` - Interface defining MCP tools for JDBC operations (complete)
- `DatabaseUserServiceImpl.java` - **STUDENT IMPLEMENTATION TARGET** - Contains:
  - 5 IMPLEMENTED methods (examples for students to learn from)
  - 10 TODO methods for students to implement

**Data Model**: `User.java` - Reusable POJO from RA1 project, works for both file I/O and JDBC.

**Database**: H2 in-memory database configured via `schema.sql` and `data.sql`.

**MCP Integration**: Service methods are annotated with `@Tool` to expose them as MCP tools for AI interaction.

### JDBC Implementation Patterns (VANILLA - No Spring)

The service methods demonstrate/require:
- **Connection Management**: DatabaseConfig.getConnection() → DriverManager, try-with-resources
- **CRUD Operations**: PreparedStatement for INSERT/SELECT/UPDATE/DELETE
- **Advanced Queries**: Dynamic SQL building, pagination with LIMIT/OFFSET
- **Transactions**: Manual commit/rollback (NO @Transactional), batch operations
- **Metadata**: DatabaseMetaData, ResultSetMetaData
- **Stored Procedures**: CallableStatement (advanced)

**Key Difference from Spring:**
- NO `@Autowired DataSource`
- NO `JdbcTemplate`
- Students call `DatabaseConfig.getConnection()` in every method
- Database initialized manually in @PostConstruct, not by Spring Boot

### TDD Approach

The project uses Test-Driven Development:
1. Tests are provided (or to be created)
2. Students implement methods to make tests pass
3. Tests validate proper use of required JDBC classes

## MCP Server Configuration

The server exposes **15 tools** via `application.yml`:

### ✅ IMPLEMENTED Methods (Examples for Students)

1. `test_connection` - Basic JDBC connection pattern
2. `create_user` - INSERT with PreparedStatement and getGeneratedKeys
3. `find_user_by_id` - SELECT and ResultSet mapping
4. `update_user` - UPDATE statement
5. `transfer_data` - Manual transaction with commit/rollback

### ⚠️ TODO Methods (Students Implement)

**CE2.a - Connection Management:**
- `get_connection_info` - DatabaseMetaData exploration

**CE2.b - CRUD Operations:**
- `delete_user` - DELETE statement
- `find_all_users` - SELECT all with iteration

**CE2.c - Advanced Queries:**
- `find_users_by_department` - WHERE clause filtering
- `search_users` - Dynamic query building
- `find_users_with_pagination` - LIMIT/OFFSET

**CE2.d - Transactions:**
- `batch_insert_users` - Batch operations with addBatch/executeBatch

**CE2.e - Metadata:**
- `get_database_info` - Full DatabaseMetaData
- `get_table_columns` - Column metadata

**CE2.f - Stored Procedures:**
- `execute_count_by_department` - CallableStatement (advanced)

Server runs on `localhost:8082` when started.

## Development Constraints

**Required JDBC Classes** (vanilla JDBC - NO Spring abstractions):
- **DatabaseConfig.getConnection()** - Helper that uses DriverManager (NOT DataSource)
- **java.sql.DriverManager** - Direct connection management
- **java.sql.Connection** - Database connection
- **java.sql.PreparedStatement** - Parameterized queries (preferred over Statement)
- **java.sql.ResultSet** - Query results
- **java.sql.Statement** - For RETURN_GENERATED_KEYS
- **java.sql.DatabaseMetaData** - Database information
- **java.sql.ResultSetMetaData** - Column information
- **java.sql.CallableStatement** - Stored procedures
- **java.sql.Timestamp** - Date/time mapping
- **java.sql.Types** - SQL type constants
- **Class.forName()** - Explicit driver loading

**Prohibited**:
- ❌ javax.sql.DataSource (Spring injection)
- ❌ JdbcTemplate (Spring abstraction)
- ❌ @Transactional (Spring transactions)
- ❌ JPA, Hibernate, Spring Data JPA
- ❌ Any ORM framework

Students must use **pure JDBC vanilla** with DriverManager.

## Implementation Notes

- All methods must use try-with-resources for proper resource management
- PreparedStatement should be used instead of Statement to prevent SQL injection
- Proper exception handling with descriptive RuntimeException messages
- LocalDateTime ↔ Timestamp conversion for date fields
- Connection auto-commit must be manually managed for transactions
- Batch operations should use addBatch() and executeBatch()

## Database Schema

### Main Table: users

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

### Test Data

8 users pre-loaded from `data.sql`:
- Various departments: IT, HR, Finance, Marketing, Sales
- Some active, one inactive
- Used for testing queries and operations

## Testing Approach

### Test Structure (To Be Implemented)

```java
@SpringBootTest
@Sql(scripts = {"/test-schema.sql", "/test-data.sql"},
     executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class DatabaseUserServiceTest {
    @Autowired
    private DatabaseUserService service;

    @Test
    void testCreateUser_insertsAndReturnsWithId() {
        // Given
        UserCreateDto dto = new UserCreateDto("Test", "test@example.com", "IT", "Dev");

        // When
        User created = service.createUser(dto);

        // Then
        assertNotNull(created.getId());
        assertEquals("Test", created.getName());
    }
}
```

### Expected Behavior

- **5 implemented methods**: Tests should PASS (GREEN)
- **10 TODO methods**: Tests should FAIL with UnsupportedOperationException (RED)
- Students implement TODO methods to make tests GREEN

## Educational Objectives

### Learning Progression

**Week 1-2**: Basic CRUD (deleteUser, findAll)
**Week 3-4**: Advanced queries (findUsersByDepartment, pagination, dynamic search)
**Week 5-6**: Transactions and batch (batchInsertUsers, metadata)
**Week 7-8**: Advanced features (stored procedures - optional)

### Key Concepts

1. **Resource Management**: Try-with-resources for Connection, PreparedStatement, ResultSet
2. **SQL Injection Prevention**: Always use PreparedStatement with placeholders
3. **Type Mapping**: SQL types (BIGINT, VARCHAR, BOOLEAN, TIMESTAMP) ↔ Java types
4. **Transaction Control**: setAutoCommit(false), commit(), rollback()
5. **Error Handling**: Catch SQLException, provide descriptive messages
6. **Metadata**: DatabaseMetaData for schema info, ResultSetMetaData for columns

## Important Patterns

### Try-with-resources (MANDATORY) - JDBC Vanilla Pattern

```java
// PATRÓN JDBC PURO - Sin Spring DataSource
try (Connection conn = DatabaseConfig.getConnection();
     PreparedStatement pstmt = conn.prepareStatement(sql);
     ResultSet rs = pstmt.executeQuery()) {

    // Work with database
    // DatabaseConfig.getConnection() uses DriverManager internally

} catch (SQLException e) {
    throw new RuntimeException("Descriptive error: " + e.getMessage(), e);
}
```

**Key points:**
- `DatabaseConfig.getConnection()` → calls `DriverManager.getConnection()`
- NO `@Autowired DataSource`
- Students see direct JDBC connection management

### PreparedStatement with Parameters

```java
String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
try (Connection conn = DatabaseConfig.getConnection();
     PreparedStatement pstmt = conn.prepareStatement(sql)) {

    pstmt.setString(1, name);   // Index starts at 1
    pstmt.setString(2, email);
    int rows = pstmt.executeUpdate();
}
```

**Note:** Always get Connection in each method - no connection pooling abstraction.

### ResultSet Navigation

```java
while (rs.next()) {
    Long id = rs.getLong("id");
    String name = rs.getString("name");
    Boolean active = rs.getBoolean("active");
    Timestamp created = rs.getTimestamp("created_at");
    LocalDateTime createdAt = created.toLocalDateTime();

    User user = new User(id, name, ...);
}
```

### Manual Transaction

```java
Connection conn = dataSource.getConnection();
try {
    conn.setAutoCommit(false);

    // Multiple operations
    pstmt1.executeUpdate();
    pstmt2.executeUpdate();

    conn.commit();  // Success

} catch (SQLException e) {
    conn.rollback();  // Error
    throw new RuntimeException(e);
} finally {
    conn.setAutoCommit(true);
    conn.close();
}
```

## When Assisting Students

1. **Guide, Don't Solve**: Point to the implemented examples, explain patterns, don't write full solutions
2. **Emphasize JDBC Classes**: Students must use specific JDBC classes, not shortcuts
3. **Validate Against TODOs**: Each TODO has step-by-step instructions - students should follow them
4. **Check Resource Management**: Every Connection/PreparedStatement/ResultSet must use try-with-resources
5. **Prevent SQL Injection**: Never concatenate user input into SQL strings, always use PreparedStatement
6. **Explain Errors**: SQLException messages can be cryptic, help interpret them

## Common Student Mistakes

1. **Forgetting try-with-resources** → Connection leaks
2. **String concatenation in SQL** → SQL injection vulnerability
3. **Index starting at 0** → `pstmt.setString(0, ...)` fails (should be 1)
4. **Not calling rs.next()** → Reading ResultSet without navigation
5. **Wrong type mapping** → `rs.getString("id")` instead of `rs.getLong("id")`
6. **Forgetting setAutoCommit** → Transactions don't work properly
7. **Not checking affectedRows** → UPDATE/DELETE silently fails

## Debugging Tips

### Enable SQL Logging

Already enabled in `application.yml`:
```yaml
logging:
  level:
    org.springframework.jdbc: DEBUG
```

### H2 Console

Best tool for debugging:
1. Start application
2. Open http://localhost:8082/h2-console
3. Connect with: `jdbc:h2:mem:ra2db` / `sa` / (no password)
4. Test SQL queries before implementing
5. Verify data after operations

### Common Errors

- **"Table not found"**: Check schema.sql loaded
- **"Unique index violation"**: Email already exists
- **"Parameter index out of range"**: Check placeholder count vs setXXX calls
- **"Connection is closed"**: Not using try-with-resources correctly

## Files Students Modify

**PRIMARY TARGET:**
- `src/main/java/com/dam/accesodatos/ra2/DatabaseUserServiceImpl.java`
  - Implement 10 TODO methods
  - Follow patterns from 5 implemented examples

**NEVER MODIFY:**
- `DatabaseUserService.java` (interface)
- `User.java` (model)
- `schema.sql`, `data.sql` (database setup)
- `application.yml` (configuration)
- `build.gradle` (dependencies)

## Resources

- [JDBC Tutorial (Oracle)](https://docs.oracle.com/javase/tutorial/jdbc/)
- [H2 Database Documentation](http://www.h2database.com/html/main.html)
- [Spring Boot JDBC](https://docs.spring.io/spring-boot/docs/current/reference/html/data.html#data.sql)

## Project Goals

✅ Understand JDBC fundamentals (no ORM abstractions)
✅ Learn proper resource management with try-with-resources
✅ Prevent SQL injection with PreparedStatement
✅ Master CRUD operations and transactions
✅ Explore database metadata programmatically

This is a **pure JDBC learning project** - students must understand low-level database access before moving to higher-level abstractions like JPA.
