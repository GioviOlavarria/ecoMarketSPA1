package com.example.ecomarketspa.repositorio;

import com.example.ecomarketspa.entidades.productos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface repositorioProductos extends JpaRepository<productos, Long> {
}