package com.dam.accesodatos.model;

import jakarta.validation.constraints.*;

/**
 * DTO para creación de usuarios
 * Usado en herramientas MCP para validar entrada de datos
 */
public class UserCreateDto {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String name;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @NotBlank(message = "El departamento es obligatorio")
    private String department;

    @NotBlank(message = "El rol es obligatorio")
    private String role;

    public UserCreateDto() {}

    public UserCreateDto(String name, String email, String department, String role) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Convierte el DTO a entidad User
     */
    public User toUser() {
        return new User(this.name, this.email, this.department, this.role);
    }

    @Override
    public String toString() {
        return "UserCreateDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
