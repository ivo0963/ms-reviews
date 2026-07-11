package com.biblioteca.ms_reviews.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    // NUEVO: Captura de validaciones del DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidaciones(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String campo = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();
            errores.put(campo, mensaje);
        });
        return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> manejarConflicto(RuntimeException ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("error", "Error de validación de regla de negocio");
        cuerpo.put("mensaje", ex.getMessage());
        return new ResponseEntity<>(cuerpo, HttpStatus.CONFLICT);
    }
}