package com.backend.clinica.dao.impl;

import com.backend.clinica.dao.IDao;
import com.backend.clinica.db.H2Connection;
import com.backend.clinica.model.Domicilio;
import com.backend.clinica.model.Paciente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDaoH2 implements IDao<String, Paciente> {
  Logger LOGGER = LoggerFactory.getLogger(DomicilioDaoH2.class);
  private final String INSERT =
          "INSERT INTO PACIENTES (NOMBRE, APELLIDO, DNI, FECHA_REGISTRO, ID_DOMICILIO) VALUES (?,?,?,?,?)";
  private final String SELECT_BY_DNI =
          "SELECT " +
                  "P.ID AS PACIENTE_ID, P.NOMBRE, P.APELLIDO, P.DNI, P.FECHA_REGISTRO, " +
                  "D.ID AS DOMICILIO_ID, D.CALLE, D.NUMERO, D.LOCALIDAD, D.CIUDAD " +
                  "FROM PACIENTES P " +
                  "INNER JOIN DOMICILIOS D ON P.ID_DOMICILIO = D.ID " +
                  "WHERE P.DNI = ? AND P.STATE = TRUE";
  private final String SELECT_ALL =
          "SELECT " +
                  "P.ID AS PACIENTE_ID, P.NOMBRE, P.APELLIDO, P.DNI, P.FECHA_REGISTRO, " +
                  "D.ID AS DOMICILIO_ID, D.CALLE, D.NUMERO, D.LOCALIDAD, D.CIUDAD " +
                  "FROM PACIENTES P " +
                  "INNER JOIN DOMICILIOS D ON P.ID_DOMICILIO = D.ID " +
                  "WHERE P.STATE = TRUE";
  private final String UPDATE =
          "UPDATE PACIENTES SET NOMBRE = ?, APELLIDO = ?, ID_DOMICILIO = ? WHERE DNI = ?";
  private final String DELETE =
          "UPDATE PACIENTES SET STATE = ? WHERE DNI = ?";

  @Override
  public Paciente create(Paciente paciente) {
    System.out.println(paciente);
    Connection conn = null;
    Paciente pacienteEncontrado = null;
    try {
      conn = H2Connection.getConnection();
      conn.setAutoCommit(false);
      try (PreparedStatement ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
        ps.setString(1, paciente.getNombre());
        ps.setString(2, paciente.getApellido());
        ps.setString(3, paciente.getDni());
        ps.setDate(4, Date.valueOf(paciente.getFechaRegistro()));
        ps.setInt(5, paciente.getDomicilio().getId());

        ps.executeUpdate();
        try (ResultSet rs = ps.getGeneratedKeys()) {
          if (rs.next()) {
            pacienteEncontrado = new Paciente(
                    rs.getInt(1),
                    paciente.getNombre(),
                    paciente.getApellido(),
                    paciente.getDni(),
                    paciente.getFechaRegistro(),
                    paciente.getDomicilio());
            LOGGER.info("Insertado: " + pacienteEncontrado);
          }
        }
      }
      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
        LOGGER.error("Transacción revertida. Error: " + e);
      } catch (SQLException ex) {
        LOGGER.error("Error al revertir transacción: " + e);
      }
      LOGGER.error("Error al insertar paciente: " + e);
    } finally {
      closeConnection(conn);
    }
    return pacienteEncontrado;
  }

  @Override
  public Paciente readOne(String dni) {
    Paciente pacienteEncontrado = null;
    try (Connection conn = H2Connection.getConnection();
         PreparedStatement ps = conn.prepareStatement(SELECT_BY_DNI)
    ) {
      ps.setString(1, dni);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          Domicilio dom = new Domicilio(
                  rs.getInt(6),
                  rs.getString(7),
                  rs.getString(8),
                  rs.getString(9),
                  rs.getString(10));
          pacienteEncontrado = new Paciente(
                  rs.getInt(1),
                  rs.getString(2),
                  rs.getString(3),
                  rs.getString(4),
                  rs.getDate(5).toLocalDate(),
                  dom);
          LOGGER.info("Seleccionado: " + pacienteEncontrado);
        }
      }
    } catch (SQLException e) {
      LOGGER.error("Error al buscar paciente: " + e);
    }
    return pacienteEncontrado;
  }

  @Override
  public List<Paciente> readAll() {
    List<Paciente> listaPacientes = new ArrayList<>();
    try (Connection conn = H2Connection.getConnection();
         PreparedStatement ps = conn.prepareStatement(SELECT_ALL)
    ) {
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Domicilio dom = new Domicilio(
                  rs.getInt(6),
                  rs.getString(7),
                  rs.getString(8),
                  rs.getString(9),
                  rs.getString(10));
          Paciente pacienteEncontrado = new Paciente(
                  rs.getInt(1),
                  rs.getString(2),
                  rs.getString(3),
                  rs.getString(4),
                  rs.getDate(5).toLocalDate(),
                  dom);
          listaPacientes.add(pacienteEncontrado);
          LOGGER.info(pacienteEncontrado.toString());
        }
      }
    } catch (SQLException e) {
      LOGGER.error("Error al listar pacientes: " + e);
    }
    return listaPacientes;
  }

  @Override
  public Paciente update(String s, Paciente paciente) {
    return null;
  }

  @Override
  public boolean delete(String s) {
    return false;
  }

  private void closeConnection(Connection conn) {
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        LOGGER.error("Error al cerrar la conexión: " + e);
      }
    }
  }
}
