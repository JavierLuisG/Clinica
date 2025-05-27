package com.backend.clinica.service.impl;

import com.backend.clinica.dao.IDao;
import com.backend.clinica.dto.request.TurnoRequestDto;
import com.backend.clinica.dto.response.DomicilioResponseDto;
import com.backend.clinica.dto.response.OdontologoResponseDto;
import com.backend.clinica.dto.response.PacienteResponseDto;
import com.backend.clinica.dto.response.TurnoResponseDto;
import com.backend.clinica.model.Domicilio;
import com.backend.clinica.model.Odontologo;
import com.backend.clinica.model.Paciente;
import com.backend.clinica.model.Turno;
import com.backend.clinica.service.ITurnoService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TurnoService implements ITurnoService<Integer, TurnoRequestDto, TurnoResponseDto> {
  private final IDao<Integer, Turno> turnoIDao;
  private final IDao<String, Odontologo> odontologoIDao;
  private final IDao<String, Paciente> pacienteIDao;
  private final ModelMapper modelMapper;

  public TurnoService(IDao<Integer, Turno> turnoIDao, IDao<String, Odontologo> odontologoIDao, IDao<String, Paciente> pacienteIDao, ModelMapper modelMapper) {
    this.turnoIDao = turnoIDao;
    this.odontologoIDao = odontologoIDao;
    this.pacienteIDao = pacienteIDao;
    this.modelMapper = modelMapper;
  }

  @Override
  public TurnoResponseDto createTurno(TurnoRequestDto turno) {
    if (turno == null || turno.getOdontologoCodigo() == null || turno.getPacienteDni() == null) {
      return null;
    }
    Odontologo odontologo = odontologoIDao.readOne(turno.getOdontologoCodigo());
    Paciente paciente = pacienteIDao.readOne(turno.getPacienteDni());
    if (odontologo == null || paciente == null) {
      return null;
    }
    Turno created = new Turno(
            LocalDate.parse(turno.getFechaConsulta()),
            odontologo,
            paciente);
    Turno saved = turnoIDao.create(created);
    if (saved == null) {
      return null;
    }
    Turno getSaveTurno = turnoIDao.readOne(saved.getId());
    if (getSaveTurno == null) {
      return null;
    }
    return mapToResponseDto(getSaveTurno);
  }

  @Override
  public TurnoResponseDto getTurnoById(Integer id) {
    if (id == null) {
      return null;
    }
    Turno getTurno = turnoIDao.readOne(id);
    if (getTurno == null) {
      return null;
    }
    return mapToResponseDto(getTurno);
  }

  @Override
  public List<TurnoResponseDto> getAllTurnos() {
    List<TurnoResponseDto> turnoResponseDtoList = new ArrayList<>();
    List<Turno> turnoList = turnoIDao.readAll();
    if (turnoList.isEmpty()) {
      return null;
    }
    for (Turno turno : turnoList) {
      turnoResponseDtoList.add(mapToResponseDto(turno));
    }
    return turnoResponseDtoList;
  }

  @Override
  public TurnoResponseDto updateTurno(Integer id, TurnoRequestDto turno) {
    if (id == null || turno == null || turno.getOdontologoCodigo() == null || turno.getPacienteDni() == null) {
      return null;
    }
    Odontologo odontologo = odontologoIDao.readOne(turno.getOdontologoCodigo());
    Paciente paciente = pacienteIDao.readOne(turno.getPacienteDni());
    Turno getTurno = turnoIDao.readOne(id);
    if (getTurno == null) {
      return null;
    }
    Turno created = new Turno(
            getTurno.getId(),
            LocalDate.parse(turno.getFechaConsulta()),
            odontologo,
            paciente
    );
    Turno updated = turnoIDao.update(id, created);
    if (updated == null) {
      return null;
    }
    Turno getUpdatedTurno = turnoIDao.readOne(updated.getId());
    if (getUpdatedTurno == null) {
      return null;
    }
    return mapToResponseDto(getUpdatedTurno);
  }

  @Override
  public boolean deleteTurno(Integer id) {
    if (id == null) {
      return false;
    }
    return turnoIDao.delete(id);
  }

  private TurnoResponseDto mapToResponseDto(Turno turno) {
    TurnoResponseDto turnoResponseDto = modelMapper.map(turno, TurnoResponseDto.class);
    turnoResponseDto.setOdontologoResponseDto(modelMapper.map(turno.getOdontologo(), OdontologoResponseDto.class));
    turnoResponseDto.setPacienteResponseDto(modelMapper.map(turno.getPaciente(), PacienteResponseDto.class));
    return turnoResponseDto;
  }
}
