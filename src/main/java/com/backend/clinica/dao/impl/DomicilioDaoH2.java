package com.backend.clinica.dao.impl;

import com.backend.clinica.dao.IDao;
import com.backend.clinica.db.H2Connection;
import com.backend.clinica.entity.Domicilio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class DomicilioDaoH2 implements IDao<Integer, Domicilio> {
  Logger LOGGER = LoggerFactory.getLogger(DomicilioDaoH2.class);
  private final String INSERT =
          "INSERT INTO DOMICILIOS (CALLE, NUMERO, LOCALIDAD, CIUDAD) VALUES (?,?,?,?)";
  private final String SELECT_BY_ID =
          "SELECT * FROM DOMICILIOS WHERE ID = ? AND STATE = TRUE";
  private final String SELECT_ALL =
          "SELECT * FROM DOMICILIOS WHERE STATE = TRUE";
  private final String UPDATE =
          "UPDATE DOMICILIOS SET CALLE = ?, NUMERO = ?, LOCALIDAD = ?, CIUDAD = ? WHERE ID = ?";
  private final String DELETE =
          "UPDATE DOMICILIOS SET STATE = ? WHERE ID = ?";

  public Domicilio create(Connection conn, Domicilio domicilio) throws SQLException {
    Domicilio createDomicilio = null;
    try (PreparedStatement ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, domicilio.getCalle());
      ps.setString(2, domicilio.getNumero());
      ps.setString(3, domicilio.getLocalidad());
      ps.setString(4, domicilio.getCiudad());

      if (ps.executeUpdate() > 0) {
        try (ResultSet rs = ps.getGeneratedKeys()) {
          if (rs.next()) {
            int generatedId = rs.getInt(1);
            createDomicilio = new Domicilio(
                    generatedId,
                    domicilio.getCalle(),
                    domicilio.getNumero(),
                    domicilio.getLocalidad(),
                    domicilio.getCiudad());
            LOGGER.info("Insertado: " + createDomicilio);
          }
        }
      }
    }
    return createDomicilio;
  }

  @Override
  public Domicilio create(Domicilio domicilio) {
    Domicilio createDomicilio = null;
    Connection conn = null;
    try {
      conn = H2Connection.getConnection();
      conn.setAutoCommit(false);
      try (PreparedStatement ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
        ps.setString(1, domicilio.getCalle());
        ps.setString(2, domicilio.getNumero());
        ps.setString(3, domicilio.getLocalidad());
        ps.setString(4, domicilio.getCiudad());

        if (ps.executeUpdate() > 0) {
          try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
              int generatedId = rs.getInt(1);
              createDomicilio = new Domicilio(
                      generatedId,
                      domicilio.getCalle(),
                      domicilio.getNumero(),
                      domicilio.getLocalidad(),
                      domicilio.getCiudad());
              LOGGER.info("Insertado: " + createDomicilio);
            }
          }
        }
      }
      conn.commit();
    } catch (SQLException e) {
      rollBackCommit(conn, e);
      LOGGER.error("Error al insertar domicilio: " + e);
    } finally {
      closeConnection(conn);
    }
    return createDomicilio;
  }

  @Override
  public Domicilio readOne(Integer id) {
    Domicilio responseDomicilio = null;
    try (Connection conn = H2Connection.getConnection();
         PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)
    ) {
      ps.setInt(1, id);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          responseDomicilio = new Domicilio(
                  rs.getInt("ID"),
                  rs.getString("CALLE"),
                  rs.getString("NUMERO"),
                  rs.getString("LOCALIDAD"),
                  rs.getString("CIUDAD"));
          LOGGER.info("Seleccionado: " + responseDomicilio);
        }
      }
    } catch (SQLException e) {
      LOGGER.error("Error al seleccionar domicilio: " + e);
    }
    return responseDomicilio;
  }

  @Override
  public List<Domicilio> readAll() {
    List<Domicilio> listResponseDomicilios = new ArrayList<>();
    try (Connection conn = H2Connection.getConnection();
         PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
         ResultSet rs = ps.executeQuery()
    ) {
      LOGGER.info("List...");
      while (rs.next()) {
        Domicilio getDomicilio = new Domicilio(
                rs.getInt("ID"),
                rs.getString("CALLE"),
                rs.getString("NUMERO"),
                rs.getString("LOCALIDAD"),
                rs.getString("CIUDAD"));
        listResponseDomicilios.add(getDomicilio);
        LOGGER.info(getDomicilio.toString());
      }
      LOGGER.info("...");
    } catch (SQLException e) {
      LOGGER.error("Error al listar domicilios: " + e);
    }
    return listResponseDomicilios;
  }

  public void update(Connection conn, Integer id, Domicilio domicilio) throws SQLException {
    try (PreparedStatement ps = conn.prepareStatement(UPDATE)) {
      ps.setString(1, domicilio.getCalle());
      ps.setString(2, domicilio.getNumero());
      ps.setString(3, domicilio.getLocalidad());
      ps.setString(4, domicilio.getCiudad());
      ps.setInt(5, id);

      if (ps.executeUpdate() > 0) {
        LOGGER.info("Actualizado domicilio: " + id);
      }
    }
  }

  @Override
  public Domicilio update(Integer id, Domicilio domicilio) {
    Domicilio responseDomicilio = null;
    Connection conn = null;
    try {
      conn = H2Connection.getConnection();
      conn.setAutoCommit(false);

      try (PreparedStatement ps = conn.prepareStatement(UPDATE)) {
        ps.setString(1, domicilio.getCalle());
        ps.setString(2, domicilio.getNumero());
        ps.setString(3, domicilio.getLocalidad());
        ps.setString(4, domicilio.getCiudad());
        ps.setInt(5, id);

        if (ps.executeUpdate() > 0) {
          conn.commit();
          responseDomicilio = readOne(id);
          LOGGER.info("Actualizado: " + responseDomicilio);
        }
      }
    } catch (SQLException e) {
      rollBackCommit(conn, e);
      LOGGER.error("Error al actualizar domicilio: " + e);
    } finally {
      closeConnection(conn);
    }
    return responseDomicilio;
  }

  @Override
  public boolean delete(Integer id) {
    boolean deleteDomicilio = false;
    Connection conn = null;
    try {
      conn = H2Connection.getConnection();
      conn.setAutoCommit(false);
      try (PreparedStatement ps = conn.prepareStatement(DELETE)) {
        ps.setBoolean(1, false);
        ps.setInt(2, id);

        if (ps.executeUpdate() > 0) {
          deleteDomicilio = true;
          conn.commit();
          LOGGER.info("Eliminado domicilio: " + id);
        }
      }
    } catch (SQLException e) {
      rollBackCommit(conn, e);
      LOGGER.error("Error al eliminar domicilio: " + e);
    } finally {
      closeConnection(conn);
    }
    return deleteDomicilio;
  }

  public void rollBackCommit(Connection conn, SQLException e) {
    try {
      conn.rollback();
      LOGGER.warn("Transacción revertida: " + e);
    } catch (SQLException ex) {
      LOGGER.error("Error en transacción revertida: " + e);
    }
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
