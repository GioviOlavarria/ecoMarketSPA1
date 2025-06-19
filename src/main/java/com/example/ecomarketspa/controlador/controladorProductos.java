package com.example.ecomarketspa.controlador;

import com.example.ecomarketspa.entidades.productos;
import com.example.ecomarketspa.servicio.servicioProductos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
                        linkTo(methodOn(controladorProductos.class).getAll()).withRel("productos")))
                .collect(Collectors.toList());

        return CollectionModel.of(productos,
                linkTo(methodOn(controladorProductos.class).getAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<productos> getBId(@PathVariable("id") Long id) {
        Optional<productos> producto = servicioProductos.listarProducto(id);

        if (producto.isPresent()) {
            return EntityModel.of(producto.get(),
                    linkTo(methodOn(controladorProductos.class).getBId(id)).withSelfRel(),
                    linkTo(methodOn(controladorProductos.class).getAll()).withRel("productos"));
        } else {
            throw new RuntimeException("Producto no encontrado con id: " + id);
        }
    }

    @PostMapping
    public EntityModel<productos> guardarActualizar(@RequestBody productos producto) {
        servicioProductos.guardarOActualizar(producto);

        return EntityModel.of(producto,
                linkTo(methodOn(controladorProductos.class).getBId(producto.getId())).withSelfRel(),
                linkTo(methodOn(controladorProductos.class).getAll()).withRel("productos"));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        servicioProductos.borrar(id);
    }
}