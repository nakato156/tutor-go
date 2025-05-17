-- ============================================================
-- SCRIPT PARA INSERTAR DATOS DE PRUEBA (PostgreSQL)
-- Para probar HUs 11, 13, 15
-- Asegúrate de que las tablas y tipos ENUM ya existan.
-- ============================================================

-- Eliminar datos existentes para empezar limpio (OPCIONAL, si quieres repetibilidad total)
-- ¡PRECAUCIÓN CON ESTO SI TIENES DATOS IMPORTANTES!
DELETE FROM enlaces_sesiones;
DELETE FROM resenas;
DELETE FROM pagos;
DELETE FROM sesiones;
DELETE FROM disponibilidades;
DELETE FROM estudiantes;
DELETE FROM tutores;
DELETE FROM usuarios;
DELETE FROM roles;

-- Reiniciar secuencias de IDs (OPCIONAL, para que los IDs empiecen desde 1)
-- Esto puede ser útil para que los IDs coincidan con los ejemplos de Postman.
ALTER SEQUENCE roles_id_seq RESTART WITH 1;
ALTER SEQUENCE usuarios_id_seq RESTART WITH 1;
ALTER SEQUENCE tutores_id_seq RESTART WITH 1;
ALTER SEQUENCE estudiantes_id_seq RESTART WITH 1;
ALTER SEQUENCE sesiones_id_seq RESTART WITH 1;
ALTER SEQUENCE pagos_id_seq RESTART WITH 1;
ALTER SEQUENCE resenas_id_seq RESTART WITH 1;
ALTER SEQUENCE enlaces_sesiones_id_seq RESTART WITH 1;
-- Añade ALTER SEQUENCE para disponibilidades_id_seq y notificaciones si las usas mucho.


-- 1. Insertar Roles (IDs: 1=ADMIN, 2=TUTOR, 3=ESTUDIANTE)
INSERT INTO roles (id, nombre) VALUES
                                   (1, 'ADMIN'),
                                   (2, 'TUTOR'),
                                   (3, 'ESTUDIANTE')
    ON CONFLICT (id) DO UPDATE SET nombre = EXCLUDED.nombre; -- O DO NOTHING si prefieres

-- 2. Insertar Usuarios
-- Usuario Tutor (será usuarios.id = 1, si la secuencia está reiniciada)
INSERT INTO usuarios (id, nombre, email, password_hash, role_id, foto_url) VALUES
    (1, 'Carlos Santana (Tutor)', 'carlos.tutor@example.com', 'hash_tutor123', (SELECT id FROM roles WHERE nombre = 'TUTOR'), 'https://example.com/carlos.jpg')
    ON CONFLICT (id) DO UPDATE SET email = EXCLUDED.email, nombre = EXCLUDED.nombre, password_hash = EXCLUDED.password_hash, role_id = EXCLUDED.role_id, foto_url = EXCLUDED.foto_url;

-- Usuario Estudiante (será usuarios.id = 2)
INSERT INTO usuarios (id, nombre, email, password_hash, role_id, foto_url) VALUES
    (2, 'Laura Palmer (Estudiante)', 'laura.estudiante@example.com', 'hash_estudiante456', (SELECT id FROM roles WHERE nombre = 'ESTUDIANTE'), 'https://example.com/laura.jpg')
    ON CONFLICT (id) DO UPDATE SET email = EXCLUDED.email, nombre = EXCLUDED.nombre, password_hash = EXCLUDED.password_hash, role_id = EXCLUDED.role_id, foto_url = EXCLUDED.foto_url;

-- Usuario adicional sin pagos (para probar historial vacío) (será usuarios.id = 3)
INSERT INTO usuarios (id, nombre, email, password_hash, role_id, foto_url) VALUES
    (3, 'Usuario Sin Pagos', 'sinpagos@example.com', 'hash_sinpagos789', (SELECT id FROM roles WHERE nombre = 'ESTUDIANTE'), 'https://example.com/sinpagos.jpg')
    ON CONFLICT (id) DO UPDATE SET email = EXCLUDED.email, nombre = EXCLUDED.nombre, password_hash = EXCLUDED.password_hash, role_id = EXCLUDED.role_id, foto_url = EXCLUDED.foto_url;


-- 3. Insertar Perfiles de Tutor y Estudiante
-- Perfil Tutor para Carlos Santana (usuarios.id=1). tutores.id será 1.
INSERT INTO tutores (id, usuario_id, tarifa_hora, rubro, bio, estrellas_promedio) VALUES
    (1, 1, 55, 'Programación Python', 'Desarrollador Python con experiencia en Django.', 0.0)
    ON CONFLICT (id) DO UPDATE SET usuario_id = EXCLUDED.usuario_id, tarifa_hora = EXCLUDED.tarifa_hora, rubro = EXCLUDED.rubro, bio = EXCLUDED.bio, estrellas_promedio = EXCLUDED.estrellas_promedio;

-- Perfil Estudiante para Laura Palmer (usuarios.id=2). estudiantes.id será 1.
INSERT INTO estudiantes (id, usuario_id, centro_estudio) VALUES
    (1, 2, 'Universidad de Springwood')
    ON CONFLICT (id) DO UPDATE SET usuario_id = EXCLUDED.usuario_id, centro_estudio = EXCLUDED.centro_estudio;

-- Perfil Estudiante para Usuario Sin Pagos (usuarios.id=3). estudiantes.id será 2.
INSERT INTO estudiantes (id, usuario_id, centro_estudio) VALUES
    (2, 3, 'Instituto Vacío')
    ON CONFLICT (id) DO UPDATE SET usuario_id = EXCLUDED.usuario_id, centro_estudio = EXCLUDED.centro_estudio;

-- 4. Insertar Sesiones
-- Sesión 1: Confirmada y Completada (para HU11 y HU13)
-- Tutor Carlos (tutores.id=1) y Estudiante Laura (estudiantes.id=1). sesiones.id será 1.
INSERT INTO sesiones (id, tutor_id, estudiante_id, fecha, hora_inicial, hora_final, tipo_estado) VALUES
    (1, 1, 1, '2024-03-10', '2024-03-10 14:00:00', '2024-03-10 15:00:00', 'confirmado'::estado_sesion_enum)
    ON CONFLICT (id) DO UPDATE SET tutor_id = EXCLUDED.tutor_id, estudiante_id = EXCLUDED.estudiante_id, fecha=EXCLUDED.fecha, hora_inicial=EXCLUDED.hora_inicial, hora_final=EXCLUDED.hora_final, tipo_estado=EXCLUDED.tipo_estado;

-- Sesión 2: Pendiente (para probar HU11 - no poder adjuntar enlaces)
-- Tutor Carlos (tutores.id=1) y Estudiante Laura (estudiantes.id=1). sesiones.id será 2.
INSERT INTO sesiones (id, tutor_id, estudiante_id, fecha, hora_inicial, hora_final, tipo_estado) VALUES
    (2, 1, 1, '2025-06-01', '2025-06-01 10:00:00', '2025-06-01 11:00:00', 'pendiente'::estado_sesion_enum)
    ON CONFLICT (id) DO UPDATE SET tutor_id = EXCLUDED.tutor_id, estudiante_id = EXCLUDED.estudiante_id, fecha=EXCLUDED.fecha, hora_inicial=EXCLUDED.hora_inicial, hora_final=EXCLUDED.hora_final, tipo_estado=EXCLUDED.tipo_estado;

-- Sesión 3: Confirmada pero NO completada (para probar HU13 - no poder calificar)
-- Tutor Carlos (tutores.id=1) y Estudiante Laura (estudiantes.id=1). sesiones.id será 3.
INSERT INTO sesiones (id, tutor_id, estudiante_id, fecha, hora_inicial, hora_final, tipo_estado) VALUES
    (3, 1, 1, '2025-07-15', '2025-07-15 16:00:00', '2025-07-15 17:00:00', 'confirmado'::estado_sesion_enum)
    ON CONFLICT (id) DO UPDATE SET tutor_id = EXCLUDED.tutor_id, estudiante_id = EXCLUDED.estudiante_id, fecha=EXCLUDED.fecha, hora_inicial=EXCLUDED.hora_inicial, hora_final=EXCLUDED.hora_final, tipo_estado=EXCLUDED.tipo_estado;


-- 5. Insertar Pagos (para HU15)
-- Pago de Estudiante Laura (estudiantes.id=1) a Tutor Carlos (tutores.id=1) por la Sesión 1. pagos.id será 1.
INSERT INTO pagos (id, tutor_id, estudiante_id, monto, comision_plataforma, metodo_pago, tipo_estado) VALUES
    (1, 1, 1, 55.00, 5.50, 'paypal'::metodo_pago_enum, 'confirmado'::estado_pago_enum)
    ON CONFLICT (id) DO UPDATE SET tutor_id=EXCLUDED.tutor_id, estudiante_id=EXCLUDED.estudiante_id, monto=EXCLUDED.monto, comision_plataforma=EXCLUDED.comision_plataforma, metodo_pago=EXCLUDED.metodo_pago, tipo_estado=EXCLUDED.tipo_estado;

-- Puedes añadir más pagos si quieres probar historiales más largos.

SELECT 'Datos de prueba insertados/actualizados.' as status;