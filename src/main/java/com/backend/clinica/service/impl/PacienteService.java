package com.backend.clinica.service.impl;

import com.backend.clinica.dao.IDao;
import com.backend.clinica.dao.impl.DomicilioDaoH2;
import com.backend.clinica.dao.impl.PacienteDaoH2;
import com.backend.clinica.db.H2Connection;
import com.backend.clinica.dto.request.PacienteRequestDto;
import com.backend.clinica.dto.response.DomicilioResponseDto;
import com.backend.clinica.dto.response.PacienteResponseDto;
import com.backend.clinica.entity.Domicilio;
import com.backend.clinica.entity.Paciente;
import com.backend.clinica.service.IPacienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
              LocalDateTime.parse(paciente.getFechaRegistro()),
              savedDomicilio);
      Paciente savedPaciente = ((PacienteDaoH2) pacienteIDao).create(conn, createdPaciente);
      conn.commit();

      Paciente getSavePaciente = pacienteIDao.readOne(savedPaciente.getDni());
      if (getSavePaciente == null) {
        return null;
      }
      return mapToDto(getSavePaciente);
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
    Paciente getPaciente = pacienteIDao.readOne(dni);
    if (getPaciente == null) {
      return null;
    }
    return mapToDto(getPaciente);
  }

  @Override
  public List<PacienteResponseDto> getAllPacientes() {
    List<PacienteResponseDto> pacienteResponseDtoList = new ArrayList<>();
    List<Paciente> pacienteList = pacienteIDao.readAll();
    if (pacienteList.isEmpty()) {
      return null;
    }
    for (Paciente paciente : pacienteList) {
      pacienteResponseDtoList.add(mapToDto(paciente));
    }
    return pacienteResponseDtoList;
  }

  @Override
  public PacienteResponseDto updatePaciente(String dni, PacienteRequestDto paciente) {
    if (dni == null || paciente == null || paciente.getDomicilio() == null) {
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
              LocalDateTime.parse(paciente.getFechaRegistro()),
              createdDomicilio);
      ((PacienteDaoH2) pacienteIDao).update(conn, createdPaciente.getDni(), createdPaciente);
      conn.commit();

      Paciente getUpdatedPaciente = pacienteIDao.readOne(dni);
      if (getUpdatedPaciente == null) {
        return null;
      }
      return mapToDto(getUpdatedPaciente);
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
    if (dni == null) {
      return false;
    }
    return pacienteIDao.delete(dni);
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
