package com.backend.clinica.service.impl;

import com.backend.clinica.dto.request.TurnoRequestDto;
import com.backend.clinica.dto.response.*;
import com.backend.clinica.entity.Odontologo;
import com.backend.clinica.entity.Paciente;
import com.backend.clinica.entity.Turno;
import com.backend.clinica.exception.IllegalArgException;
import com.backend.clinica.exception.ResourceNotFoundException;
import com.backend.clinica.repository.IOdontologoRepository;
import com.backend.clinica.repository.IPacienteRepository;
import com.backend.clinica.repository.ITurnoRepository;
import com.backend.clinica.service.ITurnoService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

  @Transactional
  @Override
  public TurnoResponseDto createTurno(TurnoRequestDto turno) throws ResourceNotFoundException, IllegalArgException {
    if (turno == null || turno.getOdontologoCodigo() == null || turno.getPacienteDni() == null) {
      throw new IllegalArgException("Ingrese correctamente los datos del Turno");
    }
    Odontologo findOdontologo = getOdontologoOrThrow(turno.getOdontologoCodigo());
    Paciente findPaciente = getPacienteOrThrow(turno.getPacienteDni());

    Turno createdTurno = new Turno();
    createdTurno.setFechaConsulta(LocalDateTime.parse(turno.getFechaConsulta()));
    createdTurno.setOdontologo(findOdontologo);
    createdTurno.setPaciente(findPaciente);

    Turno savedTurno = turnoRepository.save(createdTurno);
    LOGGER.info("Turno guardado: {}", savedTurno.getId());
    return mapToResponseDto(savedTurno);
  }
  @Transactional
  @Override
  public TurnoResponseDto getTurnoById(Integer id) throws ResourceNotFoundException, IllegalArgException {
    if (id == null || id <= 0) {
      throw new IllegalArgException("Ingrese correctamente el id de Turno");
    }
    Turno findTurno = getTurnoOrThrow(id);
    return mapToResponseDto(findTurno);
  }
  @Transactional
  @Override
  public List<TurnoResponseDto> getAllTurnos() {
    List<TurnoResponseDto> turnoResponseDtoList = new ArrayList<>();
    List<Turno> turnoList = turnoRepository.findAll();
    for (Turno turno : turnoList) {
      turnoResponseDtoList.add(mapToResponseDto(turno));
    }
    return turnoResponseDtoList;
  }

  @Transactional
  @Override
  public TurnoResponseDto updateTurno(Integer id, TurnoRequestDto turno) throws IllegalArgException, ResourceNotFoundException {
    if (id == null || id <= 0 || turno == null || turno.getOdontologoCodigo() == null || turno.getPacienteDni() == null) {
      throw new IllegalArgException("Datos inválidos para actualizar Turno: " + id);
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

  @Transactional
  @Override
  public void deleteTurno(Integer id) throws ResourceNotFoundException, IllegalArgException {
    if (id == null || id <= 0) {
      throw new IllegalArgException("Ingrese correctamente el id del Turno");
    }
    Turno findTurno = getTurnoOrThrow(id);
    LOGGER.info("Turno eliminado: {}", id);
    turnoRepository.delete(findTurno);
  }

  @Transactional
  @Override
  public List<TurnoResponseDto> findByStartDateBetween(LocalDateTime firstDate, LocalDateTime endDate) throws IllegalArgException {
    if (firstDate.isAfter(endDate)) {
      throw new IllegalArgException("La fecha inicial no puede ser posterior a la fecha final");
    }
    List<TurnoResponseDto> turnoResponseDtoList = new ArrayList<>();
    List<Turno> turnoList = turnoRepository.findByStartDateBetween(firstDate, endDate);
    for (Turno turno : turnoList) {
      turnoResponseDtoList.add(mapToResponseDto(turno));
    }
    return turnoResponseDtoList;
  }

  @Transactional
  @Override
  public List<TurnoResponseDto> findByOdontologoCodigo(String codigo) throws IllegalArgException {
    if (codigo == null) {
      throw new IllegalArgException("Ingrese correctamente el Código del Odontólogo");
    }
    List<TurnoResponseDto> turnoResponseDtoList = new ArrayList<>();
    List<Turno> turnoList = turnoRepository.findByOdontologoCodigo(codigo);
    for (Turno turno : turnoList) {
      turnoResponseDtoList.add(mapToResponseDto(turno));
    }
    return turnoResponseDtoList;
  }

  @Transactional
  @Override
  public List<TurnoResponseDto> findByPacienteDni(String dni) throws IllegalArgException {
    if (dni == null) {
      throw new IllegalArgException("Ingrese correctamente el Dni del Paciente");
    }
    List<TurnoResponseDto> turnoResponseDtoList = new ArrayList<>();
    List<Turno> turnoList = turnoRepository.findByPacienteDni(dni);
    for (Turno turno : turnoList) {
      turnoResponseDtoList.add(mapToResponseDto(turno));
    }
    return turnoResponseDtoList;
  }

  private Odontologo getOdontologoOrThrow(String codigo) throws ResourceNotFoundException {
    return odontologoRepository.findByCodigo(codigo)
        .orElseThrow(() -> new ResourceNotFoundException("Odontólogo " + codigo + " no encontrado"));
  }

  private Paciente getPacienteOrThrow(String dni) throws ResourceNotFoundException {
    return pacienteRepository.findByDni(dni)
        .orElseThrow(() -> new ResourceNotFoundException("Paciente " + dni + " no encontrado"));
  }

  private Turno getTurnoOrThrow(Integer id) throws ResourceNotFoundException {
    return turnoRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Turno " + id + " no encontrado"));
  }

  private TurnoResponseDto mapToResponseDto(Turno turno) {
    TurnoResponseDto turnoResponseDto = modelMapper.map(turno, TurnoResponseDto.class);
    turnoResponseDto.setOdontologoResponseDto(modelMapper.map(turno.getOdontologo(), OdontologoResponseDto.class));
    Set<EspecialidadResponseDto> especialidadesDto = turno.getOdontologo().getEspecialidades().stream().map(e -> modelMapper.map(e, EspecialidadResponseDto.class)).collect(Collectors.toSet());
    turnoResponseDto.getOdontologoResponseDto().setEspecialidadesResponseDto(especialidadesDto);
    turnoResponseDto.setPacienteResponseDto(modelMapper.map(turno.getPaciente(), PacienteResponseDto.class));
    turnoResponseDto.getPacienteResponseDto().setDomicilioResponseDto(modelMapper.map(turno.getPaciente().getDomicilio(), DomicilioResponseDto.class));
    return turnoResponseDto;
  }
}
