package com.dam.accesodatos.model;

import jakarta.validation.constraints.*;

/**
 * DTO para actualización de usuarios
 * Usado en herramientas MCP para validar datos de actualización
 * Todos los campos son opcionales para permitir actualizaciones parciales
 */
public class UserUpdateDto {

    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String name;

    @Email(message = "El email debe tener un formato válido")
    private String email;

    private String department;

    private String role;

    private Boolean active;

    public UserUpdateDto() {}

    public UserUpdateDto(String name, String email, String department, String role, Boolean active) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.role = role;
        this.active = active;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * Aplica las actualizaciones a un usuario existente
     */
    public void applyTo(User user) {
        if (this.name != null) user.setName(this.name);
        if (this.email != null) user.setEmail(this.email);
        if (this.department != null) user.setDepartment(this.department);
        if (this.role != null) user.setRole(this.role);
        if (this.active != null) user.setActive(this.active);
    }

    @Override
    public String toString() {
        return "UserUpdateDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", role='" + role + '\'' +
                ", active=" + active +
                '}';
    }
}
