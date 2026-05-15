package com.biblioteca.ms_reviews.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> manejarNoEncontrado(ResourceNotFoundException ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("error", "Reseña no encontrada");
        cuerpo.put("mensaje", ex.getMessage());
        return new ResponseEntity<>(cuerpo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> manejarConflicto(RuntimeException ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("error", "Error de validación");
        cuerpo.put("mensaje", ex.getMessage());
        return new ResponseEntity<>(cuerpo, HttpStatus.CONFLICT);
    }
}