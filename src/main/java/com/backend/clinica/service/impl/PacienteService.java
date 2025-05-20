package com.backend.clinica.service.impl;

import com.backend.clinica.dao.IDao;
import com.backend.clinica.dao.impl.DomicilioDaoH2;
import com.backend.clinica.dao.impl.PacienteDaoH2;
import com.backend.clinica.db.H2Connection;
import com.backend.clinica.model.Domicilio;
import com.backend.clinica.model.Paciente;
import com.backend.clinica.service.IPacienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
public class PacienteService implements IPacienteService<String, Paciente> {
  private final Logger LOGGER = LoggerFactory.getLogger(PacienteService.class);

  private final IDao<String, Paciente> pacienteIDao;
  private final IDao<Integer, Domicilio> domicilioIDao;

  public PacienteService(IDao<String, Paciente> pacienteIDao, IDao<Integer, Domicilio> domicilioIDao) {
    this.pacienteIDao = pacienteIDao;
    this.domicilioIDao = domicilioIDao;
  }

  @Override
  public Paciente createPaciente(Paciente paciente) {
    Connection conn = null;
    try {
      conn = H2Connection.getConnection();
      conn.setAutoCommit(false);

      if (paciente.getDomicilio().getId() == null) {
        Domicilio domicilioCreado = ((DomicilioDaoH2) domicilioIDao).create(conn, paciente.getDomicilio());
        paciente.setDomicilio(domicilioCreado);
      }
      Paciente pacienteCreado = ((PacienteDaoH2) pacienteIDao).create(conn, paciente);

      conn.commit();
      return pacienteCreado;
    } catch (SQLException e) {
      rollBackCommit(conn, e);
      LOGGER.error("Error al insertar paciente con domicilio: " + e);
      return null;
    } finally {
      closeConnection(conn);
    }
  }

  @Override
  public Paciente getPacienteByDni(String dni) {
    if (dni != null) {
      return pacienteIDao.readOne(dni);
    }
    return null;
  }

  @Override
  public List<Paciente> getAllPacientes() {
    return pacienteIDao.readAll();
  }

  @Override
  public Paciente updatePaciente(String dni, Paciente paciente) {
    Connection conn = null;
    if (dni != null) {
      try {
        conn = H2Connection.getConnection();
        conn.setAutoCommit(false);
        ((DomicilioDaoH2) domicilioIDao).update(conn, paciente.getDomicilio().getId(), paciente.getDomicilio());
        ((PacienteDaoH2) pacienteIDao).update(conn, dni, paciente);

        conn.commit();
        return pacienteIDao.readOne(dni);
      } catch (SQLException e) {
        rollBackCommit(conn, e);
        LOGGER.error("Error al insertar paciente con domicilio: " + e);
        return null;
      } finally {
        closeConnection(conn);
      }
    }
    return null;
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
