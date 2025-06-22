package com.example.ecomarketspa.servicio;

import com.example.ecomarketspa.entidades.productos;
import com.example.ecomarketspa.repositorio.repositorioProductos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class servicioProductos {
    @Autowired
    repositorioProductos repositorioProductos;

    public List<productos> listarProductos(){
        return repositorioProductos.findAll();
    }

    public Optional<productos> listarProducto(Long id){
        return repositorioProductos.findById(id);
    }

    public void guardarOActualizar(productos producto){
        repositorioProductos.save(producto);
    }

    public void borrar(Long id){
        repositorioProductos.deleteById(id);
    }
}