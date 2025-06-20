package com.example.ecomarketspa.controlador;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.ecomarketspa.entidades.usuarios;
import com.example.ecomarketspa.servicio.servicioUsuarios;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "api/v1/usuarios")
public class controladorUsuarios {

    @Autowired
    private final servicioUsuarios servicioUsuarios;

    public controladorUsuarios(servicioUsuarios servicioUsuarios) {
        this.servicioUsuarios = servicioUsuarios;
    }

    @GetMapping
    public CollectionModel<EntityModel<usuarios>> getAll() {
        List<EntityModel<usuarios>> usuariosModel = servicioUsuarios.listarUsuarios().stream()
                .map(usuario -> EntityModel.of(usuario,
                        linkTo(methodOn(controladorUsuarios.class).getBId(usuario.getId_usuario())).withSelfRel(),
                        linkTo(methodOn(controladorUsuarios.class).getAll()).withRel("usuarios"),
                        linkTo(methodOn(controladorCompras.class).obtenerPorUsuario(usuario.getId_usuario())).withRel("compras")))
                .collect(Collectors.toList());

        return CollectionModel.of(usuariosModel,
                linkTo(methodOn(controladorUsuarios.class).getAll()).withSelfRel());
    }

    @GetMapping("/{id_usuario}")
    public ResponseEntity<EntityModel<usuarios>> getBId(@PathVariable("id_usuario") Long id_usuario) {
        Optional<usuarios> usuario = servicioUsuarios.listarUsuarios(id_usuario);

        if (usuario.isPresent()) {
            EntityModel<usuarios> usuarioModel = EntityModel.of(usuario.get(),
                    linkTo(methodOn(controladorUsuarios.class).getBId(id_usuario)).withSelfRel(),
                    linkTo(methodOn(controladorUsuarios.class).getAll()).withRel("usuarios"),
                    linkTo(methodOn(controladorCompras.class).obtenerPorUsuario(id_usuario)).withRel("compras"));

            return ResponseEntity.ok(usuarioModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public EntityModel<usuarios> guardar(@RequestBody usuarios usuario) {
        servicioUsuarios.guardarOActualizar(usuario);

        return EntityModel.of(usuario,
                linkTo(methodOn(controladorUsuarios.class).getBId(usuario.getId_usuario())).withSelfRel(),
                linkTo(methodOn(controladorUsuarios.class).getAll()).withRel("usuarios"));
    }

    @PutMapping("/{id_usuario}")
    public ResponseEntity<EntityModel<usuarios>> actualizar(@PathVariable("id_usuario") Long id_usuario,
                                                            @RequestBody usuarios usuarioActualizado) {
        Optional<usuarios> usuarioExistente = servicioUsuarios.listarUsuarios(id_usuario);

        if (usuarioExistente.isPresent()) {
            usuarioActualizado.setId_usuario(id_usuario);
            servicioUsuarios.guardarOActualizar(usuarioActualizado);

            EntityModel<usuarios> usuarioModel = EntityModel.of(usuarioActualizado,
                    linkTo(methodOn(controladorUsuarios.class).getBId(id_usuario)).withSelfRel(),
                    linkTo(methodOn(controladorUsuarios.class).getAll()).withRel("usuarios"));

            return ResponseEntity.ok(usuarioModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id_usuario}")
    public ResponseEntity<CollectionModel<EntityModel<usuarios>>> delete(@PathVariable("id_usuario") Long id_usuario) {
        Optional<usuarios> usuario = servicioUsuarios.listarUsuarios(id_usuario);

        if (usuario.isPresent()) {
            servicioUsuarios.borrar(id_usuario);

            CollectionModel<EntityModel<usuarios>> enlaces = CollectionModel.empty(
                    linkTo(methodOn(controladorUsuarios.class).getAll()).withRel("usuarios"));

            return ResponseEntity.ok(enlaces);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}