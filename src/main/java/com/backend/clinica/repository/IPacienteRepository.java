package com.backend.clinica.repository;

import com.backend.clinica.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPacienteRepository extends JpaRepository<Paciente, Integer> {
  Optional<Paciente> findByDni(String dni);
}
