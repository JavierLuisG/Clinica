package com.backend.clinica.dao.impl;

import com.backend.clinica.dao.IDao;
import com.backend.clinica.db.H2Connection;
import com.backend.clinica.model.Odontologo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class OdontologoDaoH2 implements IDao<String, Odontologo> {
  private final Logger LOGGER = LoggerFactory.getLogger(PacienteDaoH2.class);
  private final String INSERT =
          "INSERT INTO ODONTOLOGOS (CODIGO, NOMBRE, APELLIDO) VALUES (?,?,?)";
  private final String SELECT_BY_CODIGO =
          "SELECT * FROM ODONTOLOGOS WHERE CODIGO = ? AND STATE = TRUE";
  private final String SELECT_ALL =
          "SELECT * FROM ODONTOLOGOS WHERE STATE = TRUE";
  private final String UPDATE =
          "UPDATE ODONTOLOGOS SET NOMBRE = ?, APELLIDO = ? WHERE CODIGO = ?";
  private final String DELETE =
          "UPDATE ODONTOLOGOS SET STATE = ? WHERE CODIGO = ?";

  @Override
  public Odontologo create(Odontologo odontologo) {
    Odontologo createOdontologo = null;
    Connection conn = null;
    try {
      conn = H2Connection.getConnection();
      conn.setAutoCommit(false);
      try (PreparedStatement ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
        ps.setString(1, odontologo.getCodigo());
        ps.setString(2, odontologo.getApellido());
        ps.setString(3, odontologo.getNombre());

        if (ps.executeUpdate() > 0) {
          try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
              int generatedId = rs.getInt(1);
              createOdontologo = new Odontologo(
                      generatedId,
                      odontologo.getCodigo(),
                      odontologo.getNombre(),
                      odontologo.getApellido());
              LOGGER.info("Insertado: " + createOdontologo);
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
    return createOdontologo;
  }

  @Override
  public Odontologo readOne(String codigo) {
    Odontologo responseOdontologo = null;
    try (Connection conn = H2Connection.getConnection();
         PreparedStatement ps = conn.prepareStatement(SELECT_BY_CODIGO)
    ) {
      ps.setString(1, codigo);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          responseOdontologo = new Odontologo(
                  rs.getInt("ID"),
                  rs.getString("CODIGO"),
                  rs.getString("NOMBRE"),
                  rs.getString("APELLIDO"));
          LOGGER.info("Seleccionado: " + responseOdontologo);
        }
      }
    } catch (SQLException e) {
      LOGGER.error("Error al seleccionar odontólogo: " + e);
    }
    return responseOdontologo;
  }

  @Override
  public List<Odontologo> readAll() {
    List<Odontologo> listResponseOdontologo = new ArrayList<>();
    try (Connection conn = H2Connection.getConnection();
         PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
         ResultSet rs = ps.executeQuery()
    ) {
      LOGGER.info("List...");
      while (rs.next()) {
        Odontologo getOdontologo = new Odontologo(
                rs.getInt("ID"),
                rs.getString("CODIGO"),
                rs.getString("NOMBRE"),
                rs.getString("APELLIDO"));
        listResponseOdontologo.add(getOdontologo);
        LOGGER.info(getOdontologo.toString());
      }
      LOGGER.info("...");
    } catch (SQLException e) {
      LOGGER.error("Error al listar odontólogos: " + e);
    }
    return listResponseOdontologo;
  }

  @Override
  public Odontologo update(String codigo, Odontologo odontologo) {
    Odontologo responseOdontologo = null;
    Connection conn = null;
    try {
      conn = H2Connection.getConnection();
      conn.setAutoCommit(false);

      try (PreparedStatement ps = conn.prepareStatement(UPDATE)) {
        ps.setString(1, odontologo.getNombre());
        ps.setString(2, odontologo.getApellido());
        ps.setString(3, codigo);

        if (ps.executeUpdate() > 0) {
          conn.commit(); // debe ser antes del readOne() para que se ejecute realmente el cambio con el commit
          responseOdontologo = readOne(codigo); // ya realizado el cambio si se busca
          LOGGER.info("Actualizado: " + responseOdontologo);
        }
      }
    } catch (SQLException e) {
      rollBackCommit(conn, e);
      LOGGER.error("Error al actualizar odontólogo: " + e);
    } finally {
      closeConnection(conn);
    }
    return responseOdontologo;
  }

  @Override
  public boolean delete(String codigo) {
    boolean deleteOdontologo = false;
    Connection conn = null;
    try {
      conn = H2Connection.getConnection();
      conn.setAutoCommit(false);
      try (PreparedStatement ps = conn.prepareStatement(DELETE)) {
        ps.setBoolean(1, false);
        ps.setString(2, codigo);

        if (ps.executeUpdate() > 0) {
          deleteOdontologo = true;
          LOGGER.info("Eliminado odontólogo: " + codigo);
          conn.commit();
        }
      }
    } catch (SQLException e) {
      rollBackCommit(conn, e);
      LOGGER.error("Error al eliminar odontólogo: " + e);
    } finally {
      closeConnection(conn);
    }
    return deleteOdontologo;
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
