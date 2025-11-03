package com.dam.accesodatos.model;

/**
 * DTO para consultas de usuarios con filtros opcionales
 * Usado en herramientas MCP para búsquedas parametrizadas
 */
public class UserQueryDto {

    private String department;
    private String role;
    private Boolean active;
    private Integer limit;
    private Integer offset;

    public UserQueryDto() {
        this.limit = 10; // Por defecto 10 registros
        this.offset = 0; // Desde el inicio
    }

    public UserQueryDto(String department, String role, Boolean active, Integer limit, Integer offset) {
        this.department = department;
        this.role = role;
        this.active = active;
        this.limit = limit != null ? limit : 10;
        this.offset = offset != null ? offset : 0;
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

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "UserQueryDto{" +
                "department='" + department + '\'' +
                ", role='" + role + '\'' +
                ", active=" + active +
                ", limit=" + limit +
                ", offset=" + offset +
                '}';
    }
}
