package com.dam.accesodatos;

import com.dam.accesodatos.config.DatabaseConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.mcp.server.annotation.EnableMcpServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Aplicación Spring Boot para RA2: Acceso a datos mediante JDBC PURO
 *
 * IMPORTANTE PEDAGÓGICO:
 * Esta aplicación NO usa Spring Data, DataSource, ni JdbcTemplate.
 * Los estudiantes aprenden JDBC vanilla usando DriverManager directamente.
 *
 * Esta aplicación proporciona:
 * - Servidor MCP con herramientas JDBC para interactuar con LLMs
 * - API REST para testing manual (opcional)
 * - Base de datos H2 en memoria (sin pool de Spring)
 * - 15 herramientas (5 ejemplos implementados + 10 TODOs para estudiantes)
 *
 * Configuración:
 * - Puerto HTTP: 8082 (para no conflictir con RA1 que usa 8081)
 * - Puerto MCP: 3001
 * - Base de datos: H2 en memoria (jdbc:h2:mem:ra2db)
 * - Console H2: http://localhost:8082/h2-console
 * - JDBC: DriverManager.getConnection() - SIN Spring DataSource
 *
 * Para arrancar:
 * ./gradlew bootRun
 *
 * o desde IntelliJ: Run → McpAccesoDatosRa2Application
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableMcpServer
public class McpAccesoDatosRa2Application {

    public static void main(String[] args) {
        SpringApplication.run(McpAccesoDatosRa2Application.class, args);
    }

    /**
     * Inicializa la base de datos usando JDBC puro al arrancar la aplicación.
     *
     * NOTA PEDAGÓGICA:
     * - No usamos Spring Boot's DataSourceInitializer
     * - Los estudiantes ven la inicialización manual de la BD
     * - DatabaseConfig.initializeDatabase() usa DriverManager directamente
     */
    @PostConstruct
    public void initializeDatabase() {
        System.out.println("\n========================================");
        System.out.println("Inicializando base de datos con JDBC puro");
        System.out.println("(SIN Spring DataSource - Usando DriverManager)");
        System.out.println("========================================\n");

        DatabaseConfig.initializeDatabase();

        System.out.println("\n" + DatabaseConfig.getConfigInfo());
        System.out.println("\n========================================\n");
    }
}
