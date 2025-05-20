package com.backend.clinica.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2Connection {
  private static final Logger LOGGER = LoggerFactory.getLogger(H2Connection.class);
  public static Connection getConnection() {
    Connection conn = null;
    try {
      Class.forName("org.h2.Driver");
      conn = DriverManager.getConnection("jdbc:h2:~/clinica", "sa", "sa");
    } catch (ClassNotFoundException | SQLException e) {
      throw new RuntimeException(e);
    }
    return conn;
  }

  public static void createTable() {
    Connection conn = null;
    try {
      Class.forName("org.h2.Driver");
      conn = DriverManager.getConnection("jdbc:h2:~/clinica;INIT=RUNSCRIPT FROM 'classpath:create.sql'", "sa", "sa");
    } catch (ClassNotFoundException | SQLException e) {
      LOGGER.error("Error al inicializar la base de datos: " + e);
    } finally {
      if (conn != null) {
        try {
          conn.close();
        } catch (SQLException e) {
          LOGGER.error("Error al cerrar la conexión: " + e);
        }
      }
    }
  }
}
