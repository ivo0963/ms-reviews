package com.biblioteca.ms_reviews.repository;

import com.biblioteca.ms_reviews.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResenaRepository extends JpaRepository<Resena, Long> {
    // Para ver todas las reseñas de un libro específico
    List<Resena> findByLibroId(Long libroId);

    // Para ver todas las reseñas que ha dejado un usuario
    List<Resena> findByUsuarioId(Long usuarioId);

    // Validar si el usuario ya dejó una reseña para ese libro
    boolean existsByUsuarioIdAndLibroId(Long usuarioId, Long libroId);
}