package com.backend.clinica.service.impl;

import com.backend.clinica.dao.IDao;
import com.backend.clinica.dao.impl.DomicilioDaoH2;
import com.backend.clinica.dao.impl.PacienteDaoH2;
import com.backend.clinica.db.H2Connection;
import com.backend.clinica.dto.request.PacienteRequestDto;
import com.backend.clinica.dto.response.DomicilioResponseDto;
import com.backend.clinica.dto.response.PacienteResponseDto;
import com.backend.clinica.model.Domicilio;
import com.backend.clinica.model.Paciente;
import com.backend.clinica.service.IPacienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PacienteService implements IPacienteService<String, PacienteRequestDto, PacienteResponseDto> {
  private final Logger LOGGER = LoggerFactory.getLogger(PacienteService.class);

  private final IDao<String, Paciente> pacienteIDao;
  private final IDao<Integer, Domicilio> domicilioIDao;

  public PacienteService(IDao<String, Paciente> pacienteIDao, IDao<Integer, Domicilio> domicilioIDao) {
    this.pacienteIDao = pacienteIDao;
    this.domicilioIDao = domicilioIDao;
  }

  @Override
  public PacienteResponseDto createPaciente(PacienteRequestDto paciente) {
    if (paciente == null || paciente.getDomicilio() == null) {
      return null;
    }
    Connection conn = null;
    try {
      conn = H2Connection.getConnection();
      conn.setAutoCommit(false);

      Domicilio createdDomicilio = new Domicilio(
              paciente.getDomicilio().getCalle(),
              paciente.getDomicilio().getNumero(),
              paciente.getDomicilio().getLocalidad(),
              paciente.getDomicilio().getCiudad());
      Domicilio savedDomicilio = ((DomicilioDaoH2) domicilioIDao).create(conn, createdDomicilio);
      Paciente createdPaciente = new Paciente(
              paciente.getNombre(),
              paciente.getApellido(),
              paciente.getDni(),
              LocalDate.parse(paciente.getFechaRegistro()),
              savedDomicilio);
      Paciente savedPaciente = ((PacienteDaoH2) pacienteIDao).create(conn, createdPaciente);
      DomicilioResponseDto domicilioResponseDto = new DomicilioResponseDto(
              savedDomicilio.getId(),
              savedDomicilio.getCalle(),
              savedDomicilio.getNumero(),
              savedDomicilio.getLocalidad(),
              savedDomicilio.getCiudad());
      PacienteResponseDto pacienteResponseDto = new PacienteResponseDto(
              savedPaciente.getId(),
              savedPaciente.getNombre(),
              savedPaciente.getApellido(),
              savedPaciente.getDni(),
              savedPaciente.getFechaRegistro().toString(),
              domicilioResponseDto);
      conn.commit();
      return pacienteResponseDto;
    } catch (SQLException e) {
      rollBackCommit(conn, e);
      LOGGER.error("Error al insertar paciente con domicilio: " + e);
      return null;
    } finally {
      closeConnection(conn);
    }
  }

  @Override
  public PacienteResponseDto getPacienteByDni(String dni) {
    if (dni == null) {
      return null;
    }
    Paciente paciente = pacienteIDao.readOne(dni);
    if (paciente == null) {
      return null;
    }
    DomicilioResponseDto domicilioResponseDto = new DomicilioResponseDto(
            paciente.getDomicilio().getId(),
            paciente.getDomicilio().getCalle(),
            paciente.getDomicilio().getNumero(),
            paciente.getDomicilio().getLocalidad(),
            paciente.getDomicilio().getCiudad());
    PacienteResponseDto pacienteResponseDto = new PacienteResponseDto(
            paciente.getId(),
            paciente.getNombre(),
            paciente.getApellido(),
            paciente.getDni(),
            paciente.getFechaRegistro().toString(),
            domicilioResponseDto);
    return pacienteResponseDto;
  }

  @Override
  public List<PacienteResponseDto> getAllPacientes() {
    List<PacienteResponseDto> pacienteResponseDtoList = new ArrayList<>();
    List<Paciente> pacienteList = pacienteIDao.readAll();
    if (pacienteList.isEmpty()) {
      return null;
    }
    for (Paciente paciente : pacienteList) {
      DomicilioResponseDto domicilioResponseDto = new DomicilioResponseDto(
              paciente.getDomicilio().getId(),
              paciente.getDomicilio().getCalle(),
              paciente.getDomicilio().getNumero(),
              paciente.getDomicilio().getLocalidad(),
              paciente.getDomicilio().getCiudad());
      pacienteResponseDtoList.add(new PacienteResponseDto(
              paciente.getId(),
              paciente.getNombre(),
              paciente.getApellido(),
              paciente.getDni(),
              paciente.getFechaRegistro().toString(),
              domicilioResponseDto));
    }
    return pacienteResponseDtoList;
  }

  @Override
  public PacienteResponseDto updatePaciente(String dni, PacienteRequestDto paciente) {
    if (dni == null || paciente == null) {
      return null;
    }
    Connection conn = null;
    try {
      conn = H2Connection.getConnection();
      conn.setAutoCommit(false);

      Paciente getPaciente = pacienteIDao.readOne(dni);
      if (getPaciente == null) {
        return null;
      }
      Domicilio createdDomicilio = new Domicilio(
              getPaciente.getId(),
              paciente.getDomicilio().getCalle(),
              paciente.getDomicilio().getNumero(),
              paciente.getDomicilio().getLocalidad(),
              paciente.getDomicilio().getCiudad());
      ((DomicilioDaoH2) domicilioIDao).update(conn, createdDomicilio.getId(), createdDomicilio);
      Paciente createdPaciente = new Paciente(
              getPaciente.getId(),
              paciente.getNombre(),
              paciente.getApellido(),
              paciente.getDni(),
              LocalDate.parse(paciente.getFechaRegistro()),
              createdDomicilio);
      ((PacienteDaoH2) pacienteIDao).update(conn, createdPaciente.getDni(), createdPaciente);
      conn.commit();

      Paciente getPacienteUpdated = pacienteIDao.readOne(dni);
      DomicilioResponseDto domicilioResponseDto = new DomicilioResponseDto(
              getPacienteUpdated.getDomicilio().getId(),
              getPacienteUpdated.getDomicilio().getCalle(),
              getPacienteUpdated.getDomicilio().getNumero(),
              getPacienteUpdated.getDomicilio().getLocalidad(),
              getPacienteUpdated.getDomicilio().getCiudad());
      PacienteResponseDto pacienteResponseDto = new PacienteResponseDto(
              getPacienteUpdated.getId(),
              getPacienteUpdated.getNombre(),
              getPacienteUpdated.getApellido(),
              getPacienteUpdated.getDni(),
              getPacienteUpdated.getFechaRegistro().toString(),
              domicilioResponseDto);
      return pacienteResponseDto;
    } catch (SQLException e) {
      rollBackCommit(conn, e);
      LOGGER.error("Error al insertar paciente con domicilio: " + e);
      return null;
    } finally {
      closeConnection(conn);
    }
  }

  @Override
  public boolean deletePaciente(String dni) {
    if (dni != null) {
      return pacienteIDao.delete(dni);
    }
    return false;
  }

  public void rollBackCommit(Connection conn, SQLException e) {
    try {
      conn.rollback();
      LOGGER.warn("Transacción revertida: " + e);
    } catch (SQLException ex) {
      LOGGER.error("Error en transacción revertida: " + e);
    }
  }

  public void closeConnection(Connection conn) {
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        LOGGER.warn("Error al cerrar la conexión: " + e);
      }
    }
  }
}
