# Stored Procedures y Functions en H2

## Contexto Educativo

Este proyecto usa H2 Database en modo memoria para aprendizaje de JDBC. H2 tiene soporte limitado para stored procedures comparado con PostgreSQL o MySQL, pero permite crear funciones Java que se pueden llamar via SQL.

## Métodos de Implementación

### Opción 1: Funciones Java (Recomendado para H2)

```java
// Crear clase Java con método estático
public class DatabaseFunctions {
    public static int countUsersByDepartment(Connection conn, String department) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE department = ? AND active = true";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, department);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }
}

// Registrar en H2
CREATE ALIAS COUNT_USERS_BY_DEPT FOR "com.dam.accesodatos.DatabaseFunctions.countUsersByDepartment";

// Llamar desde SQL
SELECT COUNT_USERS_BY_DEPT('IT');
```

### Opción 2: Funciones SQL simples en H2

```sql
-- H2 permite crear funciones SQL básicas
CREATE ALIAS IF NOT EXISTS GET_ACTIVE_USER_COUNT AS $$
    int getCount(java.sql.Connection conn) throws java.sql.SQLException {
        java.sql.Statement stmt = conn.createStatement();
        java.sql.ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE active = true");
        rs.next();
        return rs.getInt(1);
    }
$$;

-- Llamar
SELECT GET_ACTIVE_USER_COUNT();
```

### Opción 3: Queries directas (Más simple para aprendizaje)

En lugar de stored procedures complejos, los estudiantes pueden:

```java
// Implementar "procedimientos" como métodos Java que encapsulan queries
public Map<String, Integer> getUserStatisticsByDepartment() {
    String sql = "SELECT department, COUNT(*) as count FROM users WHERE active = true GROUP BY department";
    Map<String, Integer> stats = new HashMap<>();

    try (Connection conn = dataSource.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            stats.put(rs.getString("department"), rs.getInt("count"));
        }
    }

    return stats;
}
```

## Ejercicios Propuestos

### CE2.f: Stored Procedures y CallableStatement

**Objetivo**: Aprender a llamar procedimientos/funciones almacenadas usando `CallableStatement`

**Ejemplo 1: Función que devuelve valor**

```java
// Método a implementar por estudiantes
public int executeCountUsersByDepartment(String department) {
    // TODO CE2.f: Implementar llamada a función almacenada
    // 1. Crear CallableStatement con sintaxis: {? = call FUNCTION_NAME(?)}
    // 2. Registrar parámetro de salida: cstmt.registerOutParameter(1, Types.INTEGER)
    // 3. Setear parámetro de entrada: cstmt.setString(2, department)
    // 4. Ejecutar: cstmt.execute()
    // 5. Obtener resultado: cstmt.getInt(1)

    throw new UnsupportedOperationException("TODO: Implementar");
}
```

**Ejemplo 2: Procedimiento que modifica datos**

```java
// Procedimiento: actualizar rol de usuario
// CREATE PROCEDURE UPDATE_USER_ROLE(user_id BIGINT, new_role VARCHAR)
public void callUpdateUserRole(Long userId, String newRole) {
    // TODO CE2.f: Implementar llamada a procedimiento
    // Sintaxis: {call PROCEDURE_NAME(?, ?)}

    throw new UnsupportedOperationException("TODO: Implementar");
}
```

## Compatibilidad PostgreSQL

Si en el futuro se migra a PostgreSQL:

```sql
-- PostgreSQL: Función PL/pgSQL
CREATE OR REPLACE FUNCTION count_users_by_department(dept_name VARCHAR)
RETURNS INTEGER AS $$
DECLARE
    user_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO user_count
    FROM users
    WHERE department = dept_name AND active = true;

    RETURN user_count;
END;
$$ LANGUAGE plpgsql;

-- PostgreSQL: Procedimiento almacenado
CREATE OR REPLACE PROCEDURE update_user_role(
    p_user_id BIGINT,
    p_new_role VARCHAR
)
LANGUAGE plpgsql AS $$
BEGIN
    UPDATE users
    SET role = p_new_role, updated_at = CURRENT_TIMESTAMP
    WHERE id = p_user_id;
END;
$$;
```

## Recursos

- [H2 CREATE ALIAS](http://www.h2database.com/html/grammar.html#create_alias)
- [JDBC CallableStatement Tutorial](https://docs.oracle.com/javase/tutorial/jdbc/basics/storedprocedures.html)
- [PostgreSQL PL/pgSQL](https://www.postgresql.org/docs/current/plpgsql.html)
