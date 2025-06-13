package com.backend.clinica.service.impl;

import com.backend.clinica.dto.request.PacienteRequestDto;
import com.backend.clinica.dto.response.DomicilioResponseDto;
import com.backend.clinica.dto.response.PacienteResponseDto;
import com.backend.clinica.entity.Domicilio;
import com.backend.clinica.entity.Paciente;
import com.backend.clinica.exception.IllegalArgException;
import com.backend.clinica.exception.ResourceNotFoundException;
import com.backend.clinica.repository.IPacienteRepository;
import com.backend.clinica.service.IPacienteService;
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
  public PacienteResponseDto createPaciente(PacienteRequestDto paciente) throws IllegalArgException {
    if (paciente == null || paciente.getDomicilio() == null) {
      throw new IllegalArgException("Ingrese correctamente los datos del Paciente");
    }
    if (pacienteRepository.findByDni(paciente.getDni()).isPresent()) {
      throw new IllegalArgException("Ya existe Paciente con Dni: " + paciente.getDni());
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

    createdDomicilio.setPaciente(createdPaciente);

    Paciente savedPaciente = pacienteRepository.save(createdPaciente);
    LOGGER.info("Paciente guardado: {}", savedPaciente.getDni());
    return mapToDto(savedPaciente);
  }

  @Override
  public PacienteResponseDto getPacienteByDni(String dni) throws IllegalArgException, ResourceNotFoundException {
    if (dni == null) {
      throw new IllegalArgException("Ingrese correctamente el Dni del Paciente");
    }
    Paciente findPaciente = getPacienteOrThrow(dni);
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
  public PacienteResponseDto updatePaciente(String dni, PacienteRequestDto paciente) throws ResourceNotFoundException, IllegalArgException {
    if (dni == null || paciente == null || paciente.getDomicilio() == null) {
      throw new IllegalArgException("Datos inválidos para actualizar Paciente: " + dni);
    }
    Paciente findPaciente = getPacienteOrThrow(dni);

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
  public void deletePaciente(String dni) throws IllegalArgException, ResourceNotFoundException {
    if (dni == null) {
      throw new IllegalArgException("Ingrese correctamente el Dni del Paciente");
    }
    Paciente paciente = getPacienteOrThrow(dni);
    LOGGER.info("Paciente eliminado: {}", dni);
    pacienteRepository.delete(paciente);
  }

  private Paciente getPacienteOrThrow(String dni) throws ResourceNotFoundException {
    return pacienteRepository.findByDni(dni)
        .orElseThrow(() -> new ResourceNotFoundException("Paciente " + dni + " no encontrado"));
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
