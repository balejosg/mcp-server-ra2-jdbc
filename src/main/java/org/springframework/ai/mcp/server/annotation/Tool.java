package org.springframework.ai.mcp.server.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación Tool para marcar métodos como herramientas MCP disponibles para LLMs.
 * Esta es una implementación básica hasta que Spring AI MCP esté disponible.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Tool {
    String name() default "";
    String description() default "";
}
