-- ============================================================
-- SCRIPT SQL - SOLO INSERCIÓN DE DATOS PARA ENLACES_SESIONES Y DEPENDENCIAS
-- Asume que las tablas y tipos ENUM ya existen.
-- Los IDs se especifican explícitamente.
-- ============================================================

-- Roles (ID 2 para TUTOR, ID 3 para ESTUDIANTE)
INSERT INTO roles (id, nombre) VALUES
                                   (2, 'TUTOR'),
                                   (3, 'ESTUDIANTE')
    ON CONFLICT (id) DO UPDATE SET nombre = EXCLUDED.nombre; -- Actualiza si el ID ya existe

-- Usuarios (Usuario Tutor ID=1, Usuario Estudiante ID=2)
INSERT INTO usuarios (id, nombre, email, password_hash, role_id, foto_url) VALUES
                                                                               (1, 'Tutor Para Enlaces', 'tutor.enlaces.pro@example.com', 'hash_tutor_enl_pro', 2, 'https://i.pravatar.cc/150?u=tutor_enl_pro'),
                                                                               (2, 'Estudiante Para Enlaces', 'est.enlaces.pro@example.com', 'hash_est_enl_pro', 3, 'https://i.pravatar.cc/150?u=est_enl_pro'),
                                                                               (3, 'Otro Tutor Enlaces', 'otro.tutor.enl@example.com', 'hash_otro_tutor', 2, 'https://i.pravatar.cc/150?u=otrotutor_enl')
    ON CONFLICT (id) DO UPDATE SET email = EXCLUDED.email, nombre = EXCLUDED.nombre, password_hash = EXCLUDED.password_hash, role_id = EXCLUDED.role_id, foto_url = EXCLUDED.foto_url;

-- Perfiles Tutor (Tutor ID=1 para Usuario ID=1; Tutor ID=2 para Usuario ID=3)
INSERT INTO tutores (id, usuario_id, tarifa_hora, rubro, bio, estrellas_promedio) VALUES
                                                                                      (1, 1, 55, 'Desarrollo Web', 'Tutor especializado en Frontend para pruebas.', 0.0),
                                                                                      (2, 3, 60, 'Bases de Datos', 'Experto en SQL y NoSQL.', 0.0)
    ON CONFLICT (id) DO UPDATE SET usuario_id = EXCLUDED.usuario_id, tarifa_hora = EXCLUDED.tarifa_hora, rubro = EXCLUDED.rubro, bio = EXCLUDED.bio, estrellas_promedio = EXCLUDED.estrellas_promedio;

-- Perfil Estudiante (Estudiante ID=1 para Usuario ID=2)
INSERT INTO estudiantes (id, usuario_id, centro_estudio) VALUES
    (1, 2, 'Academia de Código Enlace')
    ON CONFLICT (id) DO UPDATE SET usuario_id = EXCLUDED.usuario_id, centro_estudio = EXCLUDED.centro_estudio;

-- Sesiones
-- Sesión 1: Tutor 1 y Estudiante 1, Confirmada, Completada (para añadir enlaces)
INSERT INTO sesiones (id, tutor_id, estudiante_id, fecha, hora_inicial, hora_final, tipo_estado) VALUES
    (1, 1, 1, '2024-03-10', '2024-03-10 10:00:00', '2024-03-10 11:00:00', 'confirmado'::estado_sesion_enum);

-- Sesión 2: Tutor 1 y Estudiante 1, Confirmada, Futura (para añadir enlaces)
INSERT INTO sesiones (id, tutor_id, estudiante_id, fecha, hora_inicial, hora_final, tipo_estado) VALUES
    (2, 1, 1, '2025-07-01', '2025-07-01 14:00:00', '2025-07-01 15:00:00', 'confirmado'::estado_sesion_enum);

-- Sesión 3: Tutor 2 y Estudiante 1, Confirmada, Completada (para añadir enlaces)
INSERT INTO sesiones (id, tutor_id, estudiante_id, fecha, hora_inicial, hora_final, tipo_estado) VALUES
    (3, 2, 1, '2024-03-15', '2024-03-15 09:00:00', '2024-03-15 10:00:00', 'confirmado'::estado_sesion_enum);

-- Sesión 4: Tutor 1 y Estudiante 1, Pendiente, Futura (para probar que NO se pueden añadir enlaces)
INSERT INTO sesiones (id, tutor_id, estudiante_id, fecha, hora_inicial, hora_final, tipo_estado) VALUES
    (4, 1, 1, '2025-08-01', '2025-08-01 16:00:00', '2025-08-01 17:00:00', 'pendiente'::estado_sesion_enum);

-- ENLACES_SESIONES
-- Para Sesión 1 (Tutor ID 1, Estudiante ID 1)
INSERT INTO enlaces_sesiones (id, sesion_id, nombre, enlace) VALUES
                                                                 (1, 1, 'Guía HTML y CSS', 'https://developer.mozilla.org/es/docs/Learn/Getting_started_with_the_web/HTML_basics'),
                                                                 (2, 1, 'Recursos JavaScript', 'https://javascript.info/'),
                                                                 (3, 1, 'Ejemplo Proyecto Frontend', 'https://github.com/example/frontend-project'),
                                                                 (4, 1, 'Video Conferencia Grabada', 'https://zoom.us/rec/play/abcdef12345'),
                                                                 (5, 1, 'Presentación de la Clase', 'https://slides.example.com/clase1-frontend')
    ON CONFLICT (id) DO UPDATE SET sesion_id = EXCLUDED.sesion_id, nombre = EXCLUDED.nombre, enlace = EXCLUDED.enlace;

-- Para Sesión 3 (Tutor ID 2, Estudiante ID 1) - Inicialmente sin enlaces, para probar añadir desde cero
-- INSERT INTO enlaces_sesiones (id, sesion_id, nombre, enlace) VALUES ... ;

-- Podemos añadir algunos más para otras sesiones si queremos tener más datos preexistentes
INSERT INTO enlaces_sesiones (id, sesion_id, nombre, enlace) VALUES
                                                                 (6, 2, 'Material Próxima Sesión Web', 'https://example.com/proxima-sesion-web.pdf'),
                                                                 (7, 2, 'Enlace Meet Sesión Futura', 'https://meet.google.com/futuro-abc-def')
    ON CONFLICT (id) DO UPDATE SET sesion_id = EXCLUDED.sesion_id, nombre = EXCLUDED.nombre, enlace = EXCLUDED.enlace;


SELECT 'Script de SOLO INSERCIÓN para Enlaces y dependencias completado.' as status;
SELECT * from usuarios;