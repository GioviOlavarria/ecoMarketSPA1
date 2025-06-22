package com.example.ecomarketspa.repositorio;

import com.example.ecomarketspa.entidades.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface repositorioCompras extends JpaRepository<compras, Long>{
    List<compras> findByUsuario(usuarios usuario);

    List<compras> findByProducto(productos producto);

    List<compras> findByProductoAndEstado(productos producto, compras.EstadoCompra estado);

    List<compras> findByUsuarioAndEstado(usuarios usuario, compras.EstadoCompra estado);

    List<compras> findByFechaCompra(LocalDate fecha);

    List<compras> findByFechaCompraBetween(LocalDate fechaInicio, LocalDate fechaFin);
}
