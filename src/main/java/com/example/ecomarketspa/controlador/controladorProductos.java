package com.example.ecomarketspa.controlador;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.ecomarketspa.entidades.productos;
import com.example.ecomarketspa.servicio.servicioProductos;

@RestController
@RequestMapping(path = "api/v1/productos")
public class controladorProductos {

    @Autowired
    private final servicioProductos servicioProductos;

    public controladorProductos(servicioProductos servicioProductos) {
        this.servicioProductos = servicioProductos;
    }

    @GetMapping
    public List<productos> getAll(){
        return servicioProductos.listarProductos();
    }

    @GetMapping("/{id}")
    public Optional<productos> getBId(@PathVariable("id") Long id){
        return servicioProductos.listarProducto(id);
    }

    @PostMapping
    public void guardarActualizar(@RequestBody productos producto){
        servicioProductos.guardarOActualizar(producto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        servicioProductos.borrar(id);
    }
}