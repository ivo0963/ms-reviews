package com.biblioteca.ms_reviews.controller;

import com.biblioteca.ms_reviews.model.Resena;
import com.biblioteca.ms_reviews.model.dto.ResenaDTO;
import com.biblioteca.ms_reviews.service.ResenaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/resenas")
@Tag(name = "Reseñas", description = "Endpoints para la gestión de calificaciones y comentarios de libros por parte de los usuarios")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    @GetMapping
    @Operation(summary = "Listar todas las reseñas", description = "Obtiene el registro histórico de todas las reseñas dejadas en el sistema")
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
    @Operation(summary = "Reseñas por libro", description = "Obtiene todas las reseñas y calificaciones asociadas a un libro específico")
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
    @Operation(summary = "Crear reseña", description = "Registra una nueva calificación y comentario de un usuario hacia un libro")
    public ResponseEntity<EntityModel<Resena>> crear(@Valid @RequestBody ResenaDTO dto) {

        // Mapeo manual de DTO a Entidad
        Resena resena = new Resena();
        resena.setUsuarioId(dto.getUsuarioId());
        resena.setLibroId(dto.getLibroId());
        resena.setCalificacion(dto.getCalificacion());
        resena.setComentario(dto.getComentario());

        Resena creada = resenaService.crearResena(resena);
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(creada));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar reseña", description = "Elimina una reseña específica del sistema utilizando su ID")
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