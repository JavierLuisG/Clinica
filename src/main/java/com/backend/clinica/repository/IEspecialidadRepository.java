package com.backend.clinica.repository;

import com.backend.clinica.entity.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IEspecialidadRepository extends JpaRepository<Especialidad, Integer> {
  Optional<Especialidad> findByTipo(String tipo);
}
