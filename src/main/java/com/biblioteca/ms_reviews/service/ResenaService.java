package com.biblioteca.ms_reviews.service;

import com.biblioteca.ms_reviews.exception.ResourceNotFoundException;
import com.biblioteca.ms_reviews.model.Resena;
import com.biblioteca.ms_reviews.repository.ResenaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    public Resena crearResena(Resena resena) {
        // Regla 1: Un usuario solo puede reseñar un libro una vez
        if (resenaRepository.existsByUsuarioIdAndLibroId(resena.getUsuarioId(), resena.getLibroId())) {
            throw new RuntimeException("El usuario ya ha dejado una reseña para este libro.");
        }

        // Regla 2: La calificación debe ser del 1 al 5
        if (resena.getCalificacion() < 1 || resena.getCalificacion() > 5) {
            throw new RuntimeException("La calificación debe estar entre 1 y 5 estrellas.");
        }

        resena.setFechaCreacion(LocalDate.now());
        return resenaRepository.save(resena);
    }

    public List<Resena> obtenerTodas() {
        return resenaRepository.findAll();
    }

    public List<Resena> obtenerPorLibro(Long libroId) {
        return resenaRepository.findByLibroId(libroId);
    }

    public void eliminarResena(Long id) {
        Resena resena = resenaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La reseña con ID " + id + " no existe."));
        resenaRepository.delete(resena);
    }
}