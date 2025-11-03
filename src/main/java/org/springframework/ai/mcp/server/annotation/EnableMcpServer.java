package org.springframework.ai.mcp.server.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación para habilitar el servidor MCP en una aplicación Spring Boot.
 * Esta es una implementación básica hasta que Spring AI MCP esté disponible.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableMcpServer {
}
