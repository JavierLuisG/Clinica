package com.backend.clinica.service;

import com.backend.clinica.dao.impl.DomicilioDaoH2;
import com.backend.clinica.dao.impl.PacienteDaoH2;
import com.backend.clinica.model.Domicilio;
import com.backend.clinica.model.Paciente;
import com.backend.clinica.service.impl.DomicilioService;
import com.backend.clinica.service.impl.PacienteService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PacienteServiceTest {
  static Logger LOGGER = LoggerFactory.getLogger(PacienteServiceTest.class);
  private static PacienteService ps;
  private static DomicilioService ds;

  @BeforeAll
  static void setUp() {
    createTable();
    ps = new PacienteService(new PacienteDaoH2(), new DomicilioDaoH2());
    ds = new DomicilioService(new DomicilioDaoH2());
  }

  static void createTable() {
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

  @Test
  @DisplayName("Agregar un paciente con domicilio nuevo y buscarlo en la db")
  void testInsertPacienteWithNewDomicilio() {
    // Crear un nuevo domicilio
    Domicilio domicilioCreado = new Domicilio("Cl. Siempre Viva", "247", "Spring-field", "Texas");

    // Crear paciente con ese domicilio
    Paciente pacienteCreado = new Paciente("Bart", "Simpson", "22123378", LocalDate.now(), domicilioCreado);
    Paciente resPaciente = ps.createPaciente(pacienteCreado);

    assertNotNull(resPaciente.getDomicilio(), "El domicilio debería haberse creado");
    assertNotNull(resPaciente, "El paciente debería haberse creado");
    assertNotNull(resPaciente.getDomicilio().getId(), "El domicilio debería tener un ID asignado");
    assertEquals("Bart", resPaciente.getNombre(), "El nombre del paciente debe coincidir");
    assertEquals("Simpson", resPaciente.getApellido(), "El apellido del paciente debe coincidir");
    assertEquals("22123378", resPaciente.getDni(), "El DNI del paciente debe coincidir");

    // Verificar que se puede recuperar por DNI
    Paciente pacienteRecuperado = ps.getPacienteByDni("22123378");
    assertNotNull(pacienteRecuperado, "Se debería poder recuperar el paciente por DNI");
    assertEquals(resPaciente.getId(), pacienteRecuperado.getId(), "Los IDs deben coincidir");
  }

  @Test
  @DisplayName("Buscar paciente por DNI")
  void testGetPacienteByDni() {
    Paciente pacienteObtenido = ps.getPacienteByDni("87654321");
    assertNotNull(pacienteObtenido, "El paciente debería existir");
    assertEquals("Bruce", pacienteObtenido.getNombre(), "El nombre debe coincidir");
    assertEquals("Wayne", pacienteObtenido.getApellido(), "El apellido debe coincidir");
    assertEquals("87654321", pacienteObtenido.getDni(), "El DNI debe coincidir");
    assertNotNull(pacienteObtenido.getDomicilio(), "El domicilio no debería ser nulo");
    assertEquals("Calle Falsa", pacienteObtenido.getDomicilio().getCalle(), "La calle debe coincidir");
  }

  @Test
  @DisplayName("Buscar todos los pacientes con sus domicilios")
  void testGetAllPacientes() {
    List<Paciente> listaPacientes = ps.getAllPacientes();
    assertNotNull(listaPacientes, "La lista no debería ser nula");
    assertTrue(listaPacientes.size() >= 3, "Debería haber al menos 3 pacientes");

    // Verificar que todos los pacientes tienen un domicilio asignado
    for (Paciente paciente : listaPacientes) {
      assertNotNull(paciente.getDomicilio(), "Todos los pacientes deberían tener un domicilio");
    }
  }

  @Test
  @DisplayName("Listar todos los domicilios")
  void testGetAllDomicilios() {
    List<Domicilio> listaDomicilios = ds.getAllDomicilios();
    assertNotNull(listaDomicilios, "La lista no debería ser nula");
    assertTrue(listaDomicilios.size() >= 3, "Debería haber al menos 3 domicilios");
  }

  @Test
  @DisplayName("Actualizar información de un paciente")
  void testUpdatePaciente() {
    // Obtener un paciente existente
    Paciente pacienteOriginal = ps.getPacienteByDni("12345678");
    assertNotNull(pacienteOriginal, "El paciente debería existir");

    // Cambiar algunos datos
    String nuevoNombre = "Homer nuevo";
    String nuevoApellido = "Simpson nuevo";

    // Crear paciente con datos actualizados pero manteniendo el mismo domicilio
    Paciente pacienteActualizado = new Paciente(
            nuevoNombre,
            nuevoApellido,
            pacienteOriginal.getDni(),
            pacienteOriginal.getFechaRegistro(),
            pacienteOriginal.getDomicilio()
    );

    // Actualizar paciente
    Paciente resultado = ps.updatePaciente("12345678", pacienteActualizado);

    // Verificar actualización
    assertNotNull(resultado, "El resultado no debería ser nulo");
    assertEquals(nuevoNombre, resultado.getNombre(), "El nombre debería haberse actualizado");
    assertEquals(nuevoApellido, resultado.getApellido(), "El apellido debería haberse actualizado");
    assertEquals(pacienteOriginal.getDni(), resultado.getDni(), "El DNI debería seguir siendo el mismo");
    assertEquals(pacienteOriginal.getDomicilio().getId(), resultado.getDomicilio().getId(), "El ID del domicilio debería seguir siendo el mismo");

    // Verificar recuperando el paciente otra vez
    Paciente verificacion = ps.getPacienteByDni("12345678");
    assertEquals(nuevoNombre, verificacion.getNombre(), "El nombre debería seguir actualizado");
  }

  @Test
  @DisplayName("Actualizar información de un domicilio")
  void testUpdateDomicilio() {
    //Obtener paciente
    Paciente pacienteObtenido = ps.getPacienteByDni("56781234");

    // Obtener domicilio del paciente
    Domicilio domicilioPaciente = pacienteObtenido.getDomicilio();
    assertNotNull(domicilioPaciente.getId(), "El domicilio debería existir y con Id");

    // Cambiar algunos datos
    String nuevaCalle = "Evergreen Terrace";
    String nuevaLocalidad = "Springfield";

    domicilioPaciente.setCalle(nuevaCalle);
    domicilioPaciente.setLocalidad(nuevaLocalidad);

    // Actualizar domicilio en paciente
    Paciente pacienteActualizado = ps.updatePaciente(pacienteObtenido.getDni(), pacienteObtenido);

    // Verificar actualización
    assertNotNull(pacienteActualizado, "El resultado no debería ser nulo");
    assertEquals(nuevaCalle, pacienteActualizado.getDomicilio().getCalle(), "La calle debería haberse actualizado");
    assertEquals(nuevaLocalidad, pacienteActualizado.getDomicilio().getLocalidad(), "La localidad debería haberse actualizado");
    assertEquals(domicilioPaciente.getNumero(), pacienteActualizado.getDomicilio().getNumero(), "El número debería seguir siendo el mismo");

    // Verificar recuperando el domicilio otra vez
    Paciente verificacion = ps.getPacienteByDni("56781234");
    assertEquals(nuevaCalle, verificacion.getDomicilio().getCalle(), "La calle debería seguir actualizada");
  }

  @Test
  @DisplayName("Eliminar un paciente")
  void testDeletePaciente() {
    // Crear un paciente que luego se eliminará
    Domicilio domicilioCreado = new Domicilio("de prueba", "de prueba", "de prueba", "de prueba");
    Paciente pacienteParaEliminar = new Paciente("Temporal", "temporal", "99887768", LocalDate.now(), domicilioCreado);
    Paciente pacienteCreado = ps.createPaciente(pacienteParaEliminar);
    assertNotNull(pacienteCreado, "El paciente debería haberse creado");

    // Verificar que existe
    Paciente verificacionExistencia = ps.getPacienteByDni("99887768");
    assertNotNull(verificacionExistencia, "El paciente debería existir antes de eliminarlo");

    // Eliminar paciente
    boolean resultado = ps.deletePaciente("99887768");
    assertTrue(resultado, "La eliminación debería ser exitosa");

    // Verificar que ya no se puede recuperar
    Paciente verificacionEliminacion = ps.getPacienteByDni("99887768");
    assertNull(verificacionEliminacion, "El paciente no debería existir después de eliminarlo");
  }

  @Test
  @DisplayName("Probar manejo de errores con entradas inválidas")
  void testErrorHandling() {
    // Buscar paciente con DNI nulo
    Paciente pacienteNulo = ps.getPacienteByDni(null);
    assertNull(pacienteNulo, "Un DNI nulo debería devolver null");

    // Buscar paciente con DNI inexistente
    Paciente pacienteInexistente = ps.getPacienteByDni("00000000");
    assertNull(pacienteInexistente, "Un DNI inexistente debería devolver null");

    // Buscar domicilio con ID nulo
    Domicilio domicilioNulo = ds.getDomicilioById(null);
    assertNull(domicilioNulo, "Un ID nulo debería devolver null");

    // Buscar domicilio con ID inexistente
    Domicilio domicilioInexistente = ds.getDomicilioById(9999);
    assertNull(domicilioInexistente, "Un ID inexistente debería devolver null");
  }
}