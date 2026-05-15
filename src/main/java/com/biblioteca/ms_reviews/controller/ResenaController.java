package com.biblioteca.ms_reviews.controller;

import com.biblioteca.ms_reviews.model.Resena;
import com.biblioteca.ms_reviews.service.ResenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    @GetMapping
    public List<Resena> listar() {
        return resenaService.obtenerTodas();
    }

    @GetMapping("/libro/{libroId}")
    public ResponseEntity<List<Resena>> listarPorLibro(@PathVariable Long libroId) {
        return ResponseEntity.ok(resenaService.obtenerPorLibro(libroId));
    }

    @PostMapping
    public ResponseEntity<Resena> crear(@RequestBody Resena resena) {
        return ResponseEntity.ok(resenaService.crearResena(resena));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        resenaService.eliminarResena(id);
        return ResponseEntity.noContent().build();
    }
}