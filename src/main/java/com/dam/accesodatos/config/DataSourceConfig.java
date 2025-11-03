package com.dam.accesodatos.config;

import org.springframework.context.annotation.Configuration;

/**
 * Configuración del DataSource para H2 Database
 *
 * En este proyecto, la configuración del DataSource se realiza
 * automáticamente por Spring Boot a través de application.yml.
 *
 * Este archivo está aquí por completitud, pero no es necesario
 * configurar nada manualmente para H2.
 *
 * Si en el futuro se necesita personalizar el DataSource
 * (ej: pool de conexiones HikariCP), se puede hacer aquí:
 *
 * @Bean
 * public DataSource dataSource() {
 *     HikariConfig config = new HikariConfig();
 *     config.setJdbcUrl("jdbc:h2:mem:ra2db");
 *     config.setUsername("sa");
 *     config.setPassword("");
 *     config.setMaximumPoolSize(10);
 *     return new HikariDataSource(config);
 * }
 */
@Configuration
public class DataSourceConfig {
    // Configuración automática por Spring Boot via application.yml
}
