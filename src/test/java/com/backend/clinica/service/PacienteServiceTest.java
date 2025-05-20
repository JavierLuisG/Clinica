package com.backend.clinica.service;

import com.backend.clinica.dao.impl.DomicilioDaoH2;
import com.backend.clinica.dao.impl.PacienteDaoH2;
import com.backend.clinica.model.Domicilio;
import com.backend.clinica.model.Paciente;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PacienteServiceTest {
  static Logger LOGGER = LoggerFactory.getLogger(DomicilioDaoH2.class);
  PacienteService ps = new PacienteService(new PacienteDaoH2(), new DomicilioDaoH2());

  @BeforeAll
  static void createTable() {
    Connection conn = null;
    try {
      Class.forName("org.h2.Driver");
      conn = DriverManager.getConnection("jdbc:h2:~/clinica;INIT=RUNSCRIPT FROM 'create.sql'", "sa", "sa");
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
      LOGGER.error(e.getMessage());
    } finally {
      try {
        conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
        LOGGER.error(e.getMessage());
      }
    }
  }

  @BeforeEach
  void beforeEach() {
    LOGGER.info("-----------------------------------------------------");
  }

  @Test
  @DisplayName("Agregar un paciente con domicilio y buscarlo en la db por ID")
  void testInsertOne() {
    Domicilio domicilioCreado = new Domicilio("Cl. Siempre Viva", "247", "Spring-field", "Texas");
    Domicilio resDomicilio = ps.createDomicilio(domicilioCreado);
    Paciente pacienteCreado = new Paciente("Bart", "Simpson", "22123378", LocalDate.now(), resDomicilio);
    assertNotNull(ps.createPaciente(pacienteCreado));
  }

  @Test
  @DisplayName("Buscar por DNI")
  void testGetOne() {
    Paciente pacienteObtenido = ps.getPaciente("87654321");
    String expected = "Paciente id: 2 - state: true - nombre: Bruce - apellido: Wayne - dni: 87654321 - " +
            "fecha registro: 2025-05-20, Domicilio id: 2 - state: true - calle: Calle Falsa - numero: 123 - ciudad: Nueva Jersey - localidad: Ciudad Gótica";
    assertEquals(expected, pacienteObtenido.toString());
  }

  @Test
  @DisplayName("Buscar todos los pacientes con sus domicilios")
  void testGetAll() {
    List<Paciente> listaPacientes = ps.getAllPacientes();
    assertEquals(5, listaPacientes.size());
  }

  @Test
  @DisplayName("Asignar direccion ya creada a paciente nuevo")
  void testYetDomicilio() {
    Domicilio domicilioObtenido = ps.getDomicilio(2);
    Paciente pacienteCreado = new Paciente("Carlos", "Vives", "53223378", LocalDate.now(), domicilioObtenido);
    assertNotNull(ps.createPaciente(pacienteCreado));
  }

  @Test
  @DisplayName("Listar todos los domicilios")
  void testListaDomicilios() {
    List<Domicilio> listaDomicilios;
    listaDomicilios = ps.getAllDomicilios();
    assertEquals(4, listaDomicilios.size());
  }
}