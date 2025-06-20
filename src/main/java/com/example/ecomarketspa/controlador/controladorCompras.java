package com.example.ecomarketspa.controlador;

import com.example.ecomarketspa.entidades.compras;
import com.example.ecomarketspa.servicio.servicioCompras;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/compras")
public class controladorCompras {

    @Autowired
    private servicioCompras servicioCompras;

    @GetMapping
    public CollectionModel<EntityModel<compras>> obtenerTodas() {
        List<EntityModel<compras>> comprasModel = servicioCompras.listarCompras().stream()
                .map(compra -> EntityModel.of(compra,
                        linkTo(methodOn(controladorCompras.class).obtenerPorId(compra.getId_compra())).withSelfRel(),
                        linkTo(methodOn(controladorCompras.class).obtenerTodas()).withRel("compras"),
                        linkTo(methodOn(controladorUsuarios.class).getBId(compra.getUsuario().getId_usuario())).withRel("usuario"),
                        linkTo(methodOn(controladorProductos.class).getBId(compra.getProducto().getId())).withRel("producto")))
                .collect(Collectors.toList());

        return CollectionModel.of(comprasModel,
                linkTo(methodOn(controladorCompras.class).obtenerTodas()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<compras>> obtenerPorId(@PathVariable Long id) {
        Optional<compras> compra = servicioCompras.listarCompra(id);

        if (compra.isPresent()) {
            EntityModel<compras> compraModel = EntityModel.of(compra.get(),
                    linkTo(methodOn(controladorCompras.class).obtenerPorId(id)).withSelfRel(),
                    linkTo(methodOn(controladorCompras.class).obtenerTodas()).withRel("compras"),
                    linkTo(methodOn(controladorUsuarios.class).getBId(compra.get().getUsuario().getId_usuario())).withRel("usuario"),
                    linkTo(methodOn(controladorProductos.class).getBId(compra.get().getProducto().getId())).withRel("producto"));

            return ResponseEntity.ok(compraModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuarios/{id_usuario}")
    public CollectionModel<EntityModel<compras>> obtenerPorUsuario(@PathVariable Long id_usuario) {
        List<EntityModel<compras>> comprasModel = servicioCompras.listarComprasPorUsuario(id_usuario).stream()
                .map(compra -> EntityModel.of(compra,
                        linkTo(methodOn(controladorCompras.class).obtenerPorId(compra.getId_compra())).withSelfRel(),
                        linkTo(methodOn(controladorCompras.class).obtenerPorUsuario(id_usuario)).withRel("compras-usuario"),
                        linkTo(methodOn(controladorUsuarios.class).getBId(id_usuario)).withRel("usuario")))
                .collect(Collectors.toList());

        return CollectionModel.of(comprasModel,
                linkTo(methodOn(controladorCompras.class).obtenerPorUsuario(id_usuario)).withSelfRel(),
                linkTo(methodOn(controladorCompras.class).obtenerTodas()).withRel("todas-compras"));
    }

    @GetMapping("/productos/{id_producto}")
    public CollectionModel<EntityModel<compras>> obtenerPorProducto(@PathVariable Long id_producto) {
        List<EntityModel<compras>> comprasModel = servicioCompras.listarComprasPorProducto(id_producto).stream()
                .map(compra -> EntityModel.of(compra,
                        linkTo(methodOn(controladorCompras.class).obtenerPorId(compra.getId_compra())).withSelfRel(),
                        linkTo(methodOn(controladorCompras.class).obtenerPorProducto(id_producto)).withRel("compras-producto"),
                        linkTo(methodOn(controladorProductos.class).getBId(id_producto)).withRel("producto")))
                .collect(Collectors.toList());

        return CollectionModel.of(comprasModel,
                linkTo(methodOn(controladorCompras.class).obtenerPorProducto(id_producto)).withSelfRel(),
                linkTo(methodOn(controladorCompras.class).obtenerTodas()).withRel("todas-compras"));
    }

    @PostMapping("/realizar")
    public ResponseEntity<?> crearCompra(@RequestParam Long id_usuario,
                                         @RequestParam Long id_producto,
                                         @RequestParam(defaultValue = "1") int cantidad) {
        try {
            compras nuevaCompra = servicioCompras.crearCompra(id_usuario, id_producto, cantidad);

            EntityModel<compras> compraModel = EntityModel.of(nuevaCompra,
                    linkTo(methodOn(controladorCompras.class).obtenerPorId(nuevaCompra.getId_compra())).withSelfRel(),
                    linkTo(methodOn(controladorCompras.class).obtenerTodas()).withRel("compras"),
                    linkTo(methodOn(controladorCompras.class).confirmarCompra(nuevaCompra.getId_compra())).withRel("confirmar"),
                    linkTo(methodOn(controladorCompras.class).cancelarCompra(nuevaCompra.getId_compra())).withRel("cancelar"));

            return new ResponseEntity<>(compraModel, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("error", e.getMessage());
            return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmarCompra(@PathVariable Long id) {
        try {
            compras compra = servicioCompras.confirmarCompra(id);

            EntityModel<compras> compraModel = EntityModel.of(compra,
                    linkTo(methodOn(controladorCompras.class).obtenerPorId(id)).withSelfRel(),
                    linkTo(methodOn(controladorCompras.class).obtenerTodas()).withRel("compras"));

            return ResponseEntity.ok(compraModel);
        } catch (RuntimeException e) {
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("error", e.getMessage());
            return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id,
                                              @RequestParam compras.EstadoCompra estado) {
        try {
            compras compra = servicioCompras.actualizarEstadoCompra(id, estado);

            EntityModel<compras> compraModel = EntityModel.of(compra,
                    linkTo(methodOn(controladorCompras.class).obtenerPorId(id)).withSelfRel(),
                    linkTo(methodOn(controladorCompras.class).obtenerTodas()).withRel("compras"));

            return ResponseEntity.ok(compraModel);
        } catch (RuntimeException e) {
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("error", e.getMessage());
            return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarCompra(@PathVariable Long id) {
        try {
            compras compra = servicioCompras.cancelarCompra(id);

            EntityModel<compras> compraModel = EntityModel.of(compra,
                    linkTo(methodOn(controladorCompras.class).obtenerPorId(id)).withSelfRel(),
                    linkTo(methodOn(controladorCompras.class).obtenerTodas()).withRel("compras"));

            return ResponseEntity.ok(compraModel);
        } catch (RuntimeException e) {
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("error", e.getMessage());
            return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCompra(@PathVariable Long id) {
        try {
            servicioCompras.borrar(id);

            CollectionModel<?> enlaces = CollectionModel.empty(
                    linkTo(methodOn(controladorCompras.class).obtenerTodas()).withRel("compras"));

            return ResponseEntity.ok(enlaces);
        } catch (Exception e) {
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("error", "No se pudo eliminar la compra");
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}