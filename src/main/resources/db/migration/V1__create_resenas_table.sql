CREATE TABLE resenas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    libro_id BIGINT NOT NULL,
    calificacion INT NOT NULL, -- Ej: De 1 a 5 estrellas
    comentario TEXT,
    fecha_creacion DATE NOT NULL
);