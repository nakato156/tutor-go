-- Tabla Roles
CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

-- Tabla Usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    pasword_hash VARCHAR(255) NOT NULL,
    foto_url VARCHAR(255),
    hora_creacion TIMESTAMP NOT NULL,
    hora_actualizacion TIMESTAMP NOT NULL,
    rol_id INTEGER REFERENCES roles(id)
);

-- Tabla Tutores
CREATE TABLE IF NOT EXISTS tutores (
    id SERIAL PRIMARY KEY,
    tarifa_hora NUMERIC(10, 2) NOT NULL,
    rubro VARCHAR(100) NOT NULL,
    bio TEXT,
    estrellas_promedio NUMERIC(3, 2),
    usuario_id INTEGER UNIQUE REFERENCES usuarios(id)
);

-- Tabla Estudiantes
CREATE TABLE IF NOT EXISTS estudiantes (
    id SERIAL PRIMARY KEY,
    centro_estudio VARCHAR(255),
    usuario_id INTEGER UNIQUE REFERENCES usuarios(id)
);

-- Tabla Disponibilidades
CREATE TABLE IF NOT EXISTS disponibilidades (
    id SERIAL PRIMARY KEY,
    fecha DATE NOT NULL,
    hora_inicial TIMESTAMP NOT NULL,
    hora_final TIMESTAMP NOT NULL,
    tutor_id INTEGER REFERENCES tutores(id)
);

-- Tabla Sesiones
CREATE TABLE IF NOT EXISTS sesiones (
    id SERIAL PRIMARY KEY,
    fecha DATE NOT NULL,
    hora_inicial TIMESTAMP NOT NULL,
    hora_final TIMESTAMP NOT NULL,
    tipo_estado VARCHAR(20) NOT NULL,
    tutor_id INTEGER REFERENCES tutores(id),
    estudiante_id INTEGER REFERENCES estudiantes(id)
);

-- Tabla Enlaces_Sesiones
CREATE TABLE IF NOT EXISTS enlaces_sesiones (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    enlace VARCHAR(255) NOT NULL,
    sesion_id INTEGER REFERENCES sesiones(id)
);

-- Tabla Resenas
CREATE TABLE IF NOT EXISTS resenas (
    id SERIAL PRIMARY KEY,
    calificacion INTEGER NOT NULL,
    comentario TEXT,
    sesion_id INTEGER REFERENCES sesiones(id)
);

-- Tabla Pagos
CREATE TABLE IF NOT EXISTS pagos (
    id SERIAL PRIMARY KEY,
    monto NUMERIC(10, 2) NOT NULL,
    comision_plataforma NUMERIC(10, 2) NOT NULL,
    fecha TIMESTAMP NOT NULL,
    metodo_pago VARCHAR(50) NOT NULL,
    tipo_estado VARCHAR(20) NOT NULL,
    estudiante_id INTEGER REFERENCES estudiantes(id),
    tutor_id INTEGER REFERENCES tutores(id)
);

-- Tabla Notificacion_estudiantes
CREATE TABLE IF NOT EXISTS notificacion_estudiantes (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    texto TEXT NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    estudiante_id INTEGER REFERENCES estudiantes(id)
);
