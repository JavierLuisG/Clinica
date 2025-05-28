package com.backend.clinica.dao.impl;

import com.backend.clinica.dao.IDao;
import com.backend.clinica.db.H2Connection;
import com.backend.clinica.entity.Domicilio;
import com.backend.clinica.entity.Odontologo;
import com.backend.clinica.entity.Paciente;
import com.backend.clinica.entity.Turno;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class TurnoDaoH2 implements IDao<Integer, Turno> {
  Logger LOGGER = LoggerFactory.getLogger(TurnoDaoH2.class);
  private final String INSERT =
          "INSERT INTO TURNOS (FECHA_CONSULTA, ID_ODONTOLOGO, ID_PACIENTE) VALUES (?,?,?)";
  private final String SELECT_BY_ID =
          "SELECT " +
                  "T.ID AS TURNO_ID, T.FECHA_CONSULTA, T.STATE AS TURNO_STATE, " +
                  "O.ID AS ODONTOLOGO_ID, O.CODIGO AS ODONTOLOGO_CODIGO, O.NOMBRE AS ODONTOLOGO_NOMBRE, O.APELLIDO AS ODONTOLOGO_APELLIDO, " +
                  "P.ID AS PACIENTE_ID, P.NOMBRE AS PACIENTE_NOMBRE, P.APELLIDO AS PACIENTE_APELLIDO, P.DNI, P.FECHA_REGISTRO, " +
                  "D.ID AS DOMICILIO_ID, D.CALLE, D.NUMERO, D.LOCALIDAD, D.CIUDAD " +
                  "FROM TURNOS T " +
                  "INNER JOIN ODONTOLOGOS O ON T.ID_ODONTOLOGO = O.ID " +
                  "INNER JOIN PACIENTES P ON T.ID_PACIENTE = P.ID " +
                  "INNER JOIN DOMICILIOS D ON P.ID_DOMICILIO = D.ID " +
                  "WHERE T.ID = ? AND T.STATE = TRUE";
  private final String SELECT_ALL =
          "SELECT " +
                  "T.ID AS TURNO_ID, T.FECHA_CONSULTA, T.STATE AS TURNO_STATE, " +
                  "O.ID AS ODONTOLOGO_ID, O.CODIGO, O.NOMBRE AS ODONTOLOGO_NOMBRE, O.APELLIDO AS ODONTOLOGO_APELLIDO, " +
                  "P.ID AS PACIENTE_ID, P.NOMBRE AS PACIENTE_NOMBRE, P.APELLIDO AS PACIENTE_APELLIDO, P.DNI, P.FECHA_REGISTRO, " +
                  "D.ID AS DOMICILIO_ID, D.CALLE, D.NUMERO, D.LOCALIDAD, D.CIUDAD " +
                  "FROM TURNOS T " +
                  "INNER JOIN ODONTOLOGOS O ON T.ID_ODONTOLOGO = O.ID " +
                  "INNER JOIN PACIENTES P ON T.ID_PACIENTE = P.ID " +
                  "INNER JOIN DOMICILIOS D ON P.ID_DOMICILIO = D.ID " +
                  "WHERE T.STATE = TRUE";
  private final String UPDATE =
          "UPDATE TURNOS SET FECHA_CONSULTA = ?, ID_ODONTOLOGO = ?, ID_PACIENTE = ? WHERE ID = ?";
  private final String DELETE =
          "UPDATE TURNOS SET STATE = ? WHERE ID = ?";

  @Override
  public Turno create(Turno turno) {
    Turno createTurno = null;
    Connection conn = null;
    try {
      conn = H2Connection.getConnection();
      conn.setAutoCommit(false);
      try (PreparedStatement ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
        ps.setDate(1, Date.valueOf(turno.getFechaConsulta()));
        ps.setInt(2, turno.getOdontologo().getId());
        ps.setInt(3, turno.getPaciente().getId());

        if (ps.executeUpdate() > 0) {
          try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
              int generatedId = rs.getInt(1);
              createTurno = new Turno(
                      generatedId,
                      turno.getFechaConsulta(),
                      turno.getOdontologo(),
                      turno.getPaciente());
              LOGGER.info("Insertado: " + createTurno);
            }
          }
        }
      }
      conn.commit();
    } catch (SQLException e) {
      rollBackCommit(conn, e);
      LOGGER.error("Error al insertar odontólogo: " + e);
    } finally {
      closeConnection(conn);
    }
    return createTurno;
  }

  @Override
  public Turno readOne(Integer id) {
    Turno responseTurno = null;
    try (Connection conn = H2Connection.getConnection();
         PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
      ps.setInt(1, id);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          Domicilio getDomicilio = new Domicilio(
                  rs.getInt("DOMICILIO_ID"),
                  rs.getString("CALLE"),
                  rs.getString("NUMERO"),
                  rs.getString("LOCALIDAD"),
                  rs.getString("CIUDAD"));
          Paciente getPaciente = new Paciente(
                  rs.getInt("PACIENTE_ID"),
                  rs.getString("PACIENTE_NOMBRE"),
                  rs.getString("PACIENTE_APELLIDO"),
                  rs.getString("DNI"),
                  rs.getDate("FECHA_REGISTRO").toLocalDate(),
                  getDomicilio);
          Odontologo getOdontologo = new Odontologo(
                  rs.getInt("ODONTOLOGO_ID"),
                  rs.getString("CODIGO"),
                  rs.getString("ODONTOLOGO_NOMBRE"),
                  rs.getString("ODONTOLOGO_APELLIDO"));
          responseTurno = new Turno(
                  rs.getInt("TURNO_ID"),
                  rs.getDate("FECHA_CONSULTA").toLocalDate(),
                  getOdontologo,
                  getPaciente);
          LOGGER.info("Seleccionado: " + responseTurno);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return responseTurno;
  }

  @Override
  public List<Turno> readAll() {
    List<Turno> listResponseTurnos = new ArrayList<>();
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
                rs.getString("PACIENTE_NOMBRE"),
                rs.getString("PACIENTE_APELLIDO"),
                rs.getString("DNI"),
                rs.getDate("FECHA_REGISTRO").toLocalDate(),
                getDomicilio);
        Odontologo getOdontologo = new Odontologo(
                rs.getInt("ODONTOLOGO_ID"),
                rs.getString("CODIGO"),
                rs.getString("NOMBRE"),
                rs.getString("APELLIDO"));
        Turno getTurno = new Turno(
                rs.getInt("TURNO_ID"),
                rs.getDate("FECHA_CONSULTA").toLocalDate(),
                getOdontologo,
                getPaciente);
        listResponseTurnos.add(getTurno);
        LOGGER.info(getTurno.toString());
      }
      LOGGER.info("...");
    } catch (SQLException e) {
      LOGGER.error("Error al listar turnos: " + e);
    }
    return listResponseTurnos;
  }

  @Override
  public Turno update(Integer id, Turno turno) {
    Turno responseTurno = null;
    Connection conn = null;
    try {
      conn = H2Connection.getConnection();
      conn.setAutoCommit(false);

      try (PreparedStatement ps = conn.prepareStatement(UPDATE)) {
        ps.setDate(1, Date.valueOf(turno.getFechaConsulta()));
        ps.setInt(2, turno.getOdontologo().getId());
        ps.setInt(3, turno.getPaciente().getId());
        ps.setInt(4, id);

        if (ps.executeUpdate() > 0) {
          conn.commit(); // debe ser antes del readOne() para que se ejecute realmente el cambio con el commit
          responseTurno = readOne(id); // ya realizado el cambio si se busca
          LOGGER.info("Actualizado: " + responseTurno);
        }
      }
    } catch (SQLException e) {
      rollBackCommit(conn, e);
      LOGGER.error("Error al actualizar turno: " + e);
    } finally {
      closeConnection(conn);
    }
    return responseTurno;
  }

  @Override
  public boolean delete(Integer id) {
    boolean deleteTurno = false;
    Connection conn = null;
    try {
      conn = H2Connection.getConnection();
      conn.setAutoCommit(false);
      try (PreparedStatement ps = conn.prepareStatement(DELETE)) {
        ps.setBoolean(1, false);
        ps.setInt(2, id);

        if (ps.executeUpdate() > 0) {
          deleteTurno = true;
          LOGGER.info("Eliminado turno: " + id);
          conn.commit();
        }
      }
    } catch (SQLException e) {
      rollBackCommit(conn, e);
      LOGGER.error("Error al eliminar turno: " + e);
    } finally {
      closeConnection(conn);
    }
    return deleteTurno;
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
