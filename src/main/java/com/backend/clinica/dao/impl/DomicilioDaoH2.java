package com.backend.clinica.dao.impl;

import com.backend.clinica.dao.IDao;
import com.backend.clinica.db.H2Connection;
import com.backend.clinica.model.Domicilio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

  @Override
  public Domicilio create(Domicilio domicilio) {
    Connection conn = null;
    Domicilio domicilioEncontrado = null;
    try {
      conn = H2Connection.getConnection();
      conn.setAutoCommit(false);
      try (PreparedStatement ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
        ps.setString(1, domicilio.getCalle());
        ps.setString(2, domicilio.getNumero());
        ps.setString(3, domicilio.getLocalidad());
        ps.setString(4, domicilio.getCiudad());

        ps.executeUpdate();
        try (ResultSet rs = ps.getGeneratedKeys()) {
          if (rs.next()) {
            domicilioEncontrado = new Domicilio(
                    rs.getInt(1),
                    domicilio.getCalle(),
                    domicilio.getNumero(),
                    domicilio.getLocalidad(),
                    domicilio.getCiudad());
            LOGGER.info("Insertado: " + domicilioEncontrado);
          }
        }
      }
      conn.commit();
    } catch (SQLException e) {
      if (conn != null) {
        try {
          conn.rollback();
          LOGGER.error("Transacción revertida. Error: " + e);
        } catch (SQLException ex) {
          LOGGER.error("Error al revertir transacción: " + e);
        }
      }
      LOGGER.error("Error al insertar domicilio: " + e);
    } finally {
      closeConnection(conn);
    }
    return domicilioEncontrado;
  }

  @Override
  public Domicilio readOne(Integer id) {
    Domicilio domicilioEncontrado = null;
    try (Connection conn = H2Connection.getConnection();
         PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)
    ) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          domicilioEncontrado = new Domicilio(
                  rs.getInt(1),
                  rs.getString(2),
                  rs.getString(3),
                  rs.getString(4),
                  rs.getString(5));
          LOGGER.info("Seleccionado: " + domicilioEncontrado);
        }
      }
    } catch (SQLException e) {
      LOGGER.error("Error al buscar domicilio: " + e);
    }
    return domicilioEncontrado;
  }

  @Override
  public List<Domicilio> readAll() {
    List<Domicilio> listaDomicilios = new ArrayList<>();
    try (Connection conn = H2Connection.getConnection();
         PreparedStatement ps = conn.prepareStatement(SELECT_ALL)
    ) {
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Domicilio domicilioEncontrado = new Domicilio(
                  rs.getInt(1),
                  rs.getString(2),
                  rs.getString(3),
                  rs.getString(4),
                  rs.getString(5));
          listaDomicilios.add(domicilioEncontrado);
          LOGGER.info(domicilioEncontrado.toString());
        }
      }
    } catch (SQLException e) {
      LOGGER.error("Error al listar domicilios: " + e);
    }
    return listaDomicilios;
  }

  @Override
  public Domicilio update(Integer integer, Domicilio domicilio) {
    return null;
  }

  @Override
  public boolean delete(Integer integer) {
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
