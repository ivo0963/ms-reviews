package com.biblioteca.ms_reviews.controller;

import com.biblioteca.ms_reviews.model.Resena;
import com.biblioteca.ms_reviews.service.ResenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    @GetMapping
    public CollectionModel<EntityModel<Resena>> listar() {
        List<EntityModel<Resena>> resenas = resenaService.obtenerTodas()
                .stream()
                .map(this::toModel)
                .toList();

        return CollectionModel.of(
                resenas,
                linkTo(methodOn(ResenaController.class).listar()).withSelfRel()
        );
    }

    @GetMapping("/libro/{libroId}")
    public ResponseEntity<CollectionModel<EntityModel<Resena>>> listarPorLibro(@PathVariable Long libroId) {
        List<EntityModel<Resena>> resenas = resenaService.obtenerPorLibro(libroId)
                .stream()
                .map(this::toModel)
                .toList();

        return ResponseEntity.ok(CollectionModel.of(
                resenas,
                linkTo(methodOn(ResenaController.class).listarPorLibro(libroId)).withSelfRel(),
                linkTo(methodOn(ResenaController.class).listar()).withRel("todas-las-resenas")
        ));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Resena>> crear(@RequestBody Resena resena) {
        Resena creada = resenaService.crearResena(resena);
        return ResponseEntity.ok(toModel(creada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        resenaService.eliminarResena(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<Resena> toModel(Resena resena) {
        return EntityModel.of(
                resena,
                linkTo(methodOn(ResenaController.class).listarPorLibro(resena.getLibroId())).withRel("resenas-del-libro"),
                linkTo(methodOn(ResenaController.class).listar()).withRel("todas-las-resenas")
        );
    }
}