-- RA2: Datos iniciales para testing y desarrollo
-- Estos datos se cargan automáticamente al arrancar la aplicación Spring Boot

-- Insertar usuarios de prueba
INSERT INTO users (id, name, email, department, role, active, created_at, updated_at) VALUES
(1, 'Juan Pérez', 'juan.perez@empresa.com', 'IT', 'Developer', true, '2024-01-15 09:30:00', '2024-01-15 09:30:00'),
(2, 'María García', 'maria.garcia@empresa.com', 'HR', 'Manager', true, '2024-01-16 10:15:00', '2024-01-20 14:20:00'),
(3, 'Carlos López', 'carlos.lopez@empresa.com', 'Finance', 'Analyst', true, '2024-01-17 11:00:00', '2024-01-17 11:00:00'),
(4, 'Ana Martínez', 'ana.martinez@empresa.com', 'IT', 'Senior Developer', true, '2024-01-18 08:45:00', '2024-01-25 16:30:00'),
(5, 'Luis Rodríguez', 'luis.rodriguez@empresa.com', 'Marketing', 'Specialist', true, '2024-01-19 13:20:00', '2024-01-19 13:20:00'),
(6, 'Elena Fernández', 'elena.fernandez@empresa.com', 'IT', 'DevOps', false, '2024-01-20 09:00:00', '2024-02-01 10:00:00'),
(7, 'Pedro Sánchez', 'pedro.sanchez@empresa.com', 'Sales', 'Representative', true, '2024-01-21 10:30:00', '2024-01-21 10:30:00'),
(8, 'Laura González', 'laura.gonzalez@empresa.com', 'HR', 'Recruiter', true, '2024-01-22 14:00:00', '2024-01-22 14:00:00');

-- Insertar estadísticas de ejemplo para usuarios (opcional - para JOINs)
INSERT INTO user_statistics (user_id, login_count, last_login) VALUES
(1, 42, '2024-02-15 08:30:00'),
(2, 28, '2024-02-14 16:45:00'),
(3, 15, '2024-02-13 09:00:00'),
(4, 67, '2024-02-15 07:15:00'),
(5, 31, '2024-02-12 11:20:00');

-- Resetear la secuencia de IDs para que el próximo ID sea 100
-- Esto permite que los tests inserten con IDs predecibles
ALTER TABLE users ALTER COLUMN id RESTART WITH 100;
