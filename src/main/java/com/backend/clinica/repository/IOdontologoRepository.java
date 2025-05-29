package com.backend.clinica.repository;

import com.backend.clinica.entity.Odontologo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IOdontologoRepository extends JpaRepository<Odontologo, Integer> {
  Optional<Odontologo> findByCodigo(String codigo);
}
