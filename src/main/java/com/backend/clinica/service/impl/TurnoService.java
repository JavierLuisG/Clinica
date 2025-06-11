package com.backend.clinica.service.impl;

import com.backend.clinica.dto.request.TurnoRequestDto;
import com.backend.clinica.dto.response.DomicilioResponseDto;
import com.backend.clinica.dto.response.OdontologoResponseDto;
import com.backend.clinica.dto.response.PacienteResponseDto;
import com.backend.clinica.dto.response.TurnoResponseDto;
import com.backend.clinica.entity.Odontologo;
import com.backend.clinica.entity.Paciente;
import com.backend.clinica.entity.Turno;
import com.backend.clinica.exception.ResourceNotFoundException;
import com.backend.clinica.repository.IOdontologoRepository;
import com.backend.clinica.repository.IPacienteRepository;
import com.backend.clinica.repository.ITurnoRepository;
import com.backend.clinica.service.ITurnoService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TurnoService implements ITurnoService<Integer, TurnoRequestDto, TurnoResponseDto> {
  private final Logger LOGGER = LoggerFactory.getLogger(TurnoService.class);
  private final ITurnoRepository turnoRepository;
  private final IOdontologoRepository odontologoRepository;
  private final IPacienteRepository pacienteRepository;
  private final ModelMapper modelMapper;

  public TurnoService(ITurnoRepository turnoRepository, IOdontologoRepository odontologoRepository, IPacienteRepository pacienteRepository, ModelMapper modelMapper) {
    this.turnoRepository = turnoRepository;
    this.odontologoRepository = odontologoRepository;
    this.pacienteRepository = pacienteRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  public TurnoResponseDto createTurno(TurnoRequestDto turno) {
    if (turno == null || turno.getOdontologoCodigo() == null || turno.getPacienteDni() == null) {
      throw new IllegalArgumentException("Datos incompletos.");
    }
    Odontologo findOdontologo = getOdontologoOrThrow(turno.getOdontologoCodigo());
    Paciente findPaciente = getPacienteOrThrow(turno.getPacienteDni());

    Turno createdTurno = new Turno(
        LocalDateTime.parse(turno.getFechaConsulta()),
        findOdontologo,
        findPaciente);
    Turno savedTurno = turnoRepository.save(createdTurno);
    LOGGER.info("Turno guardado: {}", savedTurno.getId());
    return mapToResponseDto(savedTurno);
  }

  @Override
  public TurnoResponseDto getTurnoById(Integer id) {
    if (id == null) {
      throw new IllegalArgumentException("Ingrese id.");
    }
    Turno findTurno = getTurnoOrThrow(id);
    return mapToResponseDto(findTurno);
  }

  @Override
  public List<TurnoResponseDto> getAllTurnos() {
    List<TurnoResponseDto> turnoResponseDtoList = new ArrayList<>();
    List<Turno> turnoList = turnoRepository.findAll();
    for (Turno turno : turnoList) {
      turnoResponseDtoList.add(mapToResponseDto(turno));
    }
    return turnoResponseDtoList;
  }

  @Override
  public TurnoResponseDto updateTurno(Integer id, TurnoRequestDto turno) {
    if (id == null || turno == null || turno.getOdontologoCodigo() == null || turno.getPacienteDni() == null) {
      throw new IllegalArgumentException("Datos invalidos para actualizar.");
    }
    Odontologo findOdontologo = getOdontologoOrThrow(turno.getOdontologoCodigo());
    Paciente findPaciente = getPacienteOrThrow(turno.getPacienteDni());
    Turno findTurno = getTurnoOrThrow(id);

    findTurno.setFechaConsulta(LocalDateTime.parse(turno.getFechaConsulta()));
    findTurno.setOdontologo(findOdontologo);
    findTurno.setPaciente(findPaciente);

    Turno updatedTurno = turnoRepository.save(findTurno);
    LOGGER.info("Turno actualizado: {}", id);
    return mapToResponseDto(updatedTurno);
  }

  @Override
  public void deleteTurno(Integer id) throws ResourceNotFoundException {
    if (id == null) {
      throw new IllegalArgumentException("Ingrese id");
    }
    Turno turno = turnoRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Turno " + id + " no encontrado"));
    LOGGER.info("Turno eliminado: {}", id);
    turnoRepository.delete(turno);
  }

  @Override
  public List<TurnoResponseDto> findByStartDateBetween(LocalDateTime firstDate, LocalDateTime endDate) {
    if (firstDate == null || endDate == null) {
      throw new IllegalArgumentException("Ingrese las fechas correctamente.");
    }
    List<TurnoResponseDto> turnoResponseDtoList = new ArrayList<>();
    List<Turno> turnoList = turnoRepository.findByStartDateBetween(firstDate, endDate);
    for (Turno turno : turnoList) {
      turnoResponseDtoList.add(mapToResponseDto(turno));
    }
    return turnoResponseDtoList;
  }

  @Override
  public List<TurnoResponseDto> findByOdontologoCodigo(String codigo) {
    if (codigo == null) {
      throw new IllegalArgumentException("Ingrese código.");
    }
    List<TurnoResponseDto> turnoResponseDtoList = new ArrayList<>();
    List<Turno> turnoList = turnoRepository.findByOdontologoCodigo(codigo);
    for (Turno turno : turnoList) {
      turnoResponseDtoList.add(mapToResponseDto(turno));
    }
    return turnoResponseDtoList;
  }

  @Override
  public List<TurnoResponseDto> findByPacienteDni(String dni) {
    if (dni == null) {
      throw new IllegalArgumentException("Ingrese dni.");
    }
    List<TurnoResponseDto> turnoResponseDtoList = new ArrayList<>();
    List<Turno> turnoList = turnoRepository.findByPacienteDni(dni);
    for (Turno turno : turnoList) {
      turnoResponseDtoList.add(mapToResponseDto(turno));
    }
    return turnoResponseDtoList;
  }

  private Odontologo getOdontologoOrThrow(String codigo) {
    return odontologoRepository.findByCodigo(codigo)
        .orElseThrow(() -> new EntityNotFoundException("Odontólogo no encontrado: " + codigo));
  }

  private Paciente getPacienteOrThrow(String dni) {
    return pacienteRepository.findByDni(dni)
        .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado: " + dni));
  }

  private Turno getTurnoOrThrow(Integer id) {
    return turnoRepository.findById(id)
        .orElseThrow(() ->
            new EntityNotFoundException("Turno no encontrado: " + id));
  }

  private TurnoResponseDto mapToResponseDto(Turno turno) {
    TurnoResponseDto turnoResponseDto = modelMapper.map(turno, TurnoResponseDto.class);
    turnoResponseDto.setOdontologoResponseDto(modelMapper.map(turno.getOdontologo(), OdontologoResponseDto.class));
    turnoResponseDto.setPacienteResponseDto(modelMapper.map(turno.getPaciente(), PacienteResponseDto.class));
    turnoResponseDto.getPacienteResponseDto().setDomicilioResponseDto(modelMapper.map(turno.getPaciente().getDomicilio(), DomicilioResponseDto.class));
    return turnoResponseDto;
  }
}
