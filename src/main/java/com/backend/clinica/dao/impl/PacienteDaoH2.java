package com.backend.clinica.dao.impl;

import com.backend.clinica.dao.IDao;
import com.backend.clinica.db.H2Connection;
import com.backend.clinica.entity.Domicilio;
import com.backend.clinica.entity.Paciente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
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
          "UPDATE PACIENTES SET NOMBRE = ?, APELLIDO = ?, ID_DOMICILIO = ? WHERE DNI = ? AND STATE = TRUE";
  private final String DELETE =
          "UPDATE PACIENTES SET STATE = ? WHERE DNI = ? AND STATE = TRUE";

  public Paciente create(Connection conn, Paciente paciente) throws SQLException {
    Paciente createPaciente = null;
    try (PreparedStatement ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, paciente.getNombre());
      ps.setString(2, paciente.getApellido());
      ps.setString(3, paciente.getDni());
      ps.setDate(4, Date.valueOf(paciente.getFechaRegistro()));
      ps.setInt(5, paciente.getDomicilio().getId());

      if (ps.executeUpdate() > 0) {
        try (ResultSet rs = ps.getGeneratedKeys()) {
          if (rs.next()) {
            int generatedId = rs.getInt(1);
            createPaciente = new Paciente(
                    generatedId,
                    paciente.getNombre(),
                    paciente.getApellido(),
                    paciente.getDni(),
                    paciente.getFechaRegistro(),
                    paciente.getDomicilio());
            LOGGER.info("Insertado: " + createPaciente);
          }
        }
      }
    }
    return createPaciente;
  }

  @Override
  public Paciente create(Paciente paciente) {
    Paciente createPaciente = null;
    Connection conn = null;
    try {
      conn = H2Connection.getConnection();
      conn.setAutoCommit(false);

      try (PreparedStatement ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
        ps.setString(1, paciente.getNombre());
        ps.setString(2, paciente.getApellido());
        ps.setString(3, paciente.getDni());
        ps.setDate(4, Date.valueOf(paciente.getFechaRegistro()));
        ps.setInt(5, paciente.getDomicilio().getId());

        if (ps.executeUpdate() > 0) {
          try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
              int generatedId = rs.getInt(1);
              createPaciente = new Paciente(
                      generatedId,
                      paciente.getNombre(),
                      paciente.getApellido(),
                      paciente.getDni(),
                      paciente.getFechaRegistro(),
                      paciente.getDomicilio());
              LOGGER.info("Insertado: " + createPaciente);
            }
          }
        }
      }
      conn.commit();
    } catch (SQLException e) {
      rollBackCommit(conn, e);
      LOGGER.error("Error al insertar paciente: " + e);
    } finally {
      closeConnection(conn);
    }
    return createPaciente;
  }

  @Override
  public Paciente readOne(String dni) {
    Paciente responsePaciente = null;
    try (Connection conn = H2Connection.getConnection();
         PreparedStatement ps = conn.prepareStatement(SELECT_BY_DNI)
    ) {
      ps.setString(1, dni);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          Domicilio getDomicilio = new Domicilio(
                  rs.getInt("DOMICILIO_ID"),
                  rs.getString("CALLE"),
                  rs.getString("NUMERO"),
                  rs.getString("LOCALIDAD"),
                  rs.getString("CIUDAD"));
          responsePaciente = new Paciente(
                  rs.getInt("PACIENTE_ID"),
                  rs.getString("NOMBRE"),
                  rs.getString("APELLIDO"),
                  rs.getString("DNI"),
                  rs.getDate("FECHA_REGISTRO").toLocalDate(),
                  getDomicilio);
          LOGGER.info("Seleccionado: " + responsePaciente);
        }
      }
    } catch (SQLException e) {
      LOGGER.error("Error al seleccionar paciente: " + e);
    }
    return responsePaciente;
  }

  @Override
  public List<Paciente> readAll() {
    List<Paciente> listResponsePacientes = new ArrayList<>();
    try (Connection conn = H2Connection.getConnection();
         PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
         ResultSet rs = ps.executeQuery()
    ) {
      LOGGER.info("List...");
      while (rs.next()) {
        Domicilio getDomicilio = new Domicilio(
                rs.getInt("DOMICILIO_ID"),
                rs.getString("CALLE"),
                rs.getString("NUMERO"),
                rs.getString("LOCALIDAD"),
                rs.getString("CIUDAD"));
        Paciente getPaciente = new Paciente(
                rs.getInt("PACIENTE_ID"),
                rs.getString("NOMBRE"),
                rs.getString("APELLIDO"),
                rs.getString("DNI"),
                rs.getDate("FECHA_REGISTRO").toLocalDate(),
                getDomicilio);
        listResponsePacientes.add(getPaciente);
        LOGGER.info(getPaciente.toString());
      }
      LOGGER.info("...");
    } catch (SQLException e) {
      LOGGER.error("Error al listar paciente: " + e);
    }
    return listResponsePacientes;
  }

  public void update(Connection conn, String dni, Paciente paciente) throws SQLException {
    try (PreparedStatement ps = conn.prepareStatement(UPDATE)) {
      ps.setString(1, paciente.getNombre());
      ps.setString(2, paciente.getApellido());
      ps.setInt(3, paciente.getDomicilio().getId());
      ps.setString(4, dni);

      if (ps.executeUpdate() > 0) {
        LOGGER.info("Actualizado paciente: " + dni);
      }
    }
  }

  @Override
  public Paciente update(String dni, Paciente paciente) {
    Paciente responsePaciente = null;
    Connection conn = null;
    try {
      conn = H2Connection.getConnection();
      conn.setAutoCommit(false);

      try (PreparedStatement ps = conn.prepareStatement(UPDATE)) {
        ps.setString(1, paciente.getNombre());
        ps.setString(2, paciente.getApellido());
        ps.setInt(3, paciente.getDomicilio().getId());
        ps.setString(4, dni);

        if (ps.executeUpdate() > 0) {
          conn.commit(); // debe ser antes del readOne() para que se ejecute realmente el cambio con el commit
          responsePaciente = readOne(dni); // ya realizado el cambio si se busca
          LOGGER.info("Actualizado: " + responsePaciente);
        }
      }
    } catch (SQLException e) {
      rollBackCommit(conn, e);
      LOGGER.error("Error al actualizar paciente: " + e);
    } finally {
      closeConnection(conn);
    }
    return responsePaciente;
  }

  @Override
  public boolean delete(String dni) {
    boolean deletePaciente = false;
    Connection conn = null;
    try {
      conn = H2Connection.getConnection();
      conn.setAutoCommit(false);
      try (PreparedStatement ps = conn.prepareStatement(DELETE)) {
        ps.setBoolean(1, false);
        ps.setString(2, dni);

        if (ps.executeUpdate() > 0) {
          deletePaciente = true;
          LOGGER.info("Eliminado paciente: " + dni);
          conn.commit();
        }
      }
    } catch (SQLException e) {
      rollBackCommit(conn, e);
      LOGGER.error("Error al eliminar paciente: " + e);
    } finally {
      closeConnection(conn);
    }
    return deletePaciente;
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
