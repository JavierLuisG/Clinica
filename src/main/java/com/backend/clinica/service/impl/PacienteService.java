package com.backend.clinica.service.impl;

import com.backend.clinica.dto.request.PacienteRequestDto;
import com.backend.clinica.dto.response.DomicilioResponseDto;
import com.backend.clinica.dto.response.PacienteResponseDto;
import com.backend.clinica.entity.Domicilio;
import com.backend.clinica.entity.Paciente;
import com.backend.clinica.repository.IPacienteRepository;
import com.backend.clinica.service.IPacienteService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PacienteService implements IPacienteService<String, PacienteRequestDto, PacienteResponseDto> {
  private final Logger LOGGER = LoggerFactory.getLogger(PacienteService.class);
  private final IPacienteRepository pacienteRepository;

  public PacienteService(IPacienteRepository pacienteRepository) {
    this.pacienteRepository = pacienteRepository;
  }

  @Override
  public PacienteResponseDto createPaciente(PacienteRequestDto paciente) {
    if (paciente == null || paciente.getDomicilio() == null) {
      throw new IllegalArgumentException("Datos incompletos.");
    }
    if (pacienteRepository.findByDni(paciente.getDni()).isPresent()) {
      throw new IllegalArgumentException("Ya existe odontólogo con código: " + paciente.getDni());
    }
    Domicilio createdDomicilio = new Domicilio(
            paciente.getDomicilio().getCalle(),
            paciente.getDomicilio().getNumero(),
            paciente.getDomicilio().getLocalidad(),
            paciente.getDomicilio().getCiudad());
    Paciente createdPaciente = new Paciente(
            paciente.getNombre(),
            paciente.getApellido(),
            paciente.getDni(),
            LocalDateTime.parse(paciente.getFechaRegistro()),
            createdDomicilio);
    Paciente savedPaciente = pacienteRepository.save(createdPaciente);
    LOGGER.info("Paciente guardado: {}", savedPaciente.getDni());
    return mapToDto(savedPaciente);
  }

  @Override
  public PacienteResponseDto getPacienteByDni(String dni) {
    if (dni == null) {
      throw new IllegalArgumentException("Ingrese dni.");
    }
    Paciente findPaciente = pacienteRepository
            .findByDni(dni)
            .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado: " + dni));
    return mapToDto(findPaciente);
  }

  @Override
  public List<PacienteResponseDto> getAllPacientes() {
    List<PacienteResponseDto> pacienteResponseDtoList = new ArrayList<>();
    List<Paciente> pacienteList = pacienteRepository.findAll();
    for (Paciente paciente : pacienteList) {
      pacienteResponseDtoList.add(mapToDto(paciente));
    }
    return pacienteResponseDtoList;
  }

  @Override
  public PacienteResponseDto updatePaciente(String dni, PacienteRequestDto paciente) {
    if (dni == null || paciente == null || paciente.getDomicilio() == null) {
      throw new IllegalArgumentException("Datos invalidos para actualizar.");
    }
    Paciente findPaciente = pacienteRepository
            .findByDni(dni)
            .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado: " + dni));

    findPaciente.getDomicilio().setCalle(paciente.getDomicilio().getCalle());
    findPaciente.getDomicilio().setNumero(paciente.getDomicilio().getNumero());
    findPaciente.getDomicilio().setLocalidad(paciente.getDomicilio().getLocalidad());
    findPaciente.getDomicilio().setCiudad(paciente.getDomicilio().getCiudad());
    findPaciente.setNombre(paciente.getNombre());
    findPaciente.setApellido(paciente.getApellido());

    Paciente updatedPaciente = pacienteRepository.save(findPaciente);
    LOGGER.info("Paciente actualizado: {}", dni);
    return mapToDto(updatedPaciente);
  }

  @Override
  public boolean deletePaciente(String dni) {
    if (dni == null) {
      throw new IllegalArgumentException("Ingrese dni.");
    }
    return pacienteRepository.findByDni(dni)
            .map(paciente -> {
              pacienteRepository.delete(paciente);
              LOGGER.info("Paciente eliminado: {}", dni);
              return true;
            }).orElse(false);
  }

  private DomicilioResponseDto mapToDto(Domicilio domicilio) {
    return new DomicilioResponseDto(
            domicilio.getId(),
            domicilio.getCalle(),
            domicilio.getNumero(),
            domicilio.getLocalidad(),
            domicilio.getCiudad());
  }

  private PacienteResponseDto mapToDto(Paciente paciente) {
    return new PacienteResponseDto(
            paciente.getId(),
            paciente.getNombre(),
            paciente.getApellido(),
            paciente.getDni(),
            paciente.getFechaRegistro().toString(),
            mapToDto(paciente.getDomicilio()));
  }
}
