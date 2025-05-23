package com.example.ecomarketspa.repositorio;

import com.example.ecomarketspa.entidades.usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface repositorioUsuarios extends JpaRepository<usuarios, Long> {
}
