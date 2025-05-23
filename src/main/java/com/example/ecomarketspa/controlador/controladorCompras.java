package com.example.ecomarketspa.controlador;

import com.example.ecomarketspa.entidades.compras;
import com.example.ecomarketspa.servicio.servicioCompras;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/compras")
public class controladorCompras {

    @Autowired
    private servicioCompras servicioCompras;

    @GetMapping
    public List<compras> obtenerTodas() {
        return servicioCompras.listarCompras();
    }

    @GetMapping("/{id}")
    public ResponseEntity<compras> obtenerPorId(@PathVariable Long id) {
        Optional<compras> compra = servicioCompras.listarCompra(id);
        return compra.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuarios/{id_usuario}")
    public List<compras> obtenerPorUsuario(@PathVariable Long id_usuario) {
        return servicioCompras.listarComprasPorUsuario(id_usuario);
    }

    @GetMapping("/productos/{id_producto}")
    public List<compras> obtenerPorProducto(@PathVariable Long id_producto) {
        return servicioCompras.listarComprasPorProducto(id_producto);
    }

    @PostMapping("/realizar")
    public ResponseEntity<?> crearCompra(@RequestParam Long id_usuario,
                                         @RequestParam Long id_producto,
                                         @RequestParam(defaultValue = "1") int cantidad) {
        try {
            compras nuevaCompra = servicioCompras.crearCompra(id_usuario, id_producto, cantidad);
            return new ResponseEntity<>(nuevaCompra, HttpStatus.CREATED);
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
            return ResponseEntity.ok(compra);
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
            return ResponseEntity.ok(compra);
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
            return ResponseEntity.ok(compra);
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
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Compra eliminada correctamente");
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("error", "No se pudo eliminar la compra");
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}