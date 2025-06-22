package com.example.ecomarketspa.controlador;

import com.example.ecomarketspa.entidades.productos;
import com.example.ecomarketspa.servicio.servicioProductos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "api/v1/productos")
public class controladorProductos {

    @Autowired
    private final servicioProductos servicioProductos;

    public controladorProductos(servicioProductos servicioProductos) {
        this.servicioProductos = servicioProductos;
    }

    @GetMapping
    public CollectionModel<EntityModel<productos>> getAll() {
        List<EntityModel<productos>> productos = servicioProductos.listarProductos().stream()
                .map(producto -> EntityModel.of(producto,
                        linkTo(methodOn(controladorProductos.class).getBId(producto.getId())).withSelfRel(),
                        linkTo(methodOn(controladorProductos.class).getAll()).withRel("productos"),
                        linkTo(methodOn(controladorCompras.class).obtenerPorProducto(producto.getId())).withRel("compras-producto")))
                .collect(Collectors.toList());

        return CollectionModel.of(productos,
                linkTo(methodOn(controladorProductos.class).getAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<productos>> getBId(@PathVariable("id") Long id) {
        Optional<productos> producto = servicioProductos.listarProducto(id);

        if (producto.isPresent()) {
            EntityModel<productos> productoModel = EntityModel.of(producto.get(),
                    linkTo(methodOn(controladorProductos.class).getBId(id)).withSelfRel(),
                    linkTo(methodOn(controladorProductos.class).getAll()).withRel("productos"),
                    linkTo(methodOn(controladorCompras.class).obtenerPorProducto(id)).withRel("compras-producto"));

            return ResponseEntity.ok(productoModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public EntityModel<productos> guardar(@RequestBody productos producto) {
        servicioProductos.guardarOActualizar(producto);

        return EntityModel.of(producto,
                linkTo(methodOn(controladorProductos.class).getBId(producto.getId())).withSelfRel(),
                linkTo(methodOn(controladorProductos.class).getAll()).withRel("productos"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<productos>> actualizar(@PathVariable("id") Long id,
                                                             @RequestBody productos productoActualizado) {
        Optional<productos> productoExistente = servicioProductos.listarProducto(id);

        if (productoExistente.isPresent()) {
            productoActualizado.setId(id);
            servicioProductos.guardarOActualizar(productoActualizado);

            EntityModel<productos> productoModel = EntityModel.of(productoActualizado,
                    linkTo(methodOn(controladorProductos.class).getBId(id)).withSelfRel(),
                    linkTo(methodOn(controladorProductos.class).getAll()).withRel("productos"));

            return ResponseEntity.ok(productoModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CollectionModel<EntityModel<productos>>> delete(@PathVariable("id") Long id) {
        Optional<productos> producto = servicioProductos.listarProducto(id);

        if (producto.isPresent()) {
            servicioProductos.borrar(id);

            CollectionModel<EntityModel<productos>> enlaces = CollectionModel.empty(
                    linkTo(methodOn(controladorProductos.class).getAll()).withRel("productos"));

            return ResponseEntity.ok(enlaces);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}