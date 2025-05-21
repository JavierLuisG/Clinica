package com.backend.clinica.service;

import com.backend.clinica.dao.impl.DomicilioDaoH2;
import com.backend.clinica.dao.impl.OdontologoDaoH2;
import com.backend.clinica.dao.impl.PacienteDaoH2;
import com.backend.clinica.dao.impl.TurnoDaoH2;
import com.backend.clinica.db.H2Connection;
import com.backend.clinica.model.Odontologo;
import com.backend.clinica.model.Paciente;
import com.backend.clinica.model.Turno;
import com.backend.clinica.service.impl.OdontologoService;
import com.backend.clinica.service.impl.PacienteService;
import com.backend.clinica.service.impl.TurnoService;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TurnoServiceTest {
  private static TurnoService turnoService;
  private static OdontologoService odontologoService;
  private static PacienteService pacienteService;

  @BeforeAll
  static void setUp() {
    H2Connection.createTable();
    turnoService = new TurnoService(new TurnoDaoH2());
    odontologoService = new OdontologoService(new OdontologoDaoH2());
    pacienteService = new PacienteService(new PacienteDaoH2(), new DomicilioDaoH2());
  }

  @Test
  @Order(1)
  @DisplayName("Agregar un turno con odontologo y paciente ya existentes")
  void testInsertOne() {
    Odontologo odontologo = odontologoService.getOdontologoByCodigo("OD00125");
    Paciente paciente = pacienteService.getPacienteByDni("56781234");
    Turno turno = new Turno(LocalDate.of(2025, 7, 15), odontologo, paciente);
    Turno insertado = turnoService.createTurno(turno);
    assertNotNull(insertado);
    assertNotNull(insertado.getId());
    assertEquals(3, insertado.getOdontologo().getId());
    assertEquals(3, insertado.getPaciente().getId());
  }

  @Test
  @Order(2)
  @DisplayName("Buscar turno por id")
  void testReadOne() {
    Turno turno = turnoService.getTurnoById(1);
    assertNotNull(turno, "El turno debería existir");
    assertEquals("2025-05-25", turno.getFechaConsulta().toString(), "La fecha debe coincidir");
    assertEquals(1, turno.getId(), "El id de turno debe coincidir");
    assertEquals(2, turno.getOdontologo().getId(), "El id de odontólogo debe coincidir");
    assertEquals(1, turno.getPaciente().getId(), "El id de paciente debe coincidir");
  }

  @Test
  @Order(3)
  @DisplayName("Buscar todos los turnos")
  void testReadAll() {
    List<Turno> listTurnos = turnoService.getAllTurnos();
    assertNotNull(listTurnos, "La lista no debería ser nula");
    assertTrue(listTurnos.size() >= 3, "Debería haber al menos 3 turnos");

    for (Turno turno : listTurnos) {
      assertNotNull(turno.getOdontologo(), "Todos los turnos deben tener un odontólogo");
      assertNotNull(turno.getPaciente(), "Todos los turnos deben tener un paciente");
    }
  }

  @Test
  @Order(4)
  @DisplayName("Actualizar informacion de un turno")
  void testUpdate() {
    Turno turnoOriginal = turnoService.getTurnoById(7);
    assertNotNull(turnoOriginal, "El turno debería existir");

    Odontologo odontologo = odontologoService.getOdontologoByCodigo("OD00127");

    Turno turnoNuevo = new Turno(turnoOriginal.getFechaConsulta(), odontologo, turnoOriginal.getPaciente());

    Turno turnoActualizado = turnoService.updateTurno(1, turnoNuevo);

    assertNotNull(turnoActualizado, "El resultado no debería ser nulo");

    Turno verificarTurnoActualizado = turnoService.getTurnoById(turnoActualizado.getId());
    assertEquals("OD00127", verificarTurnoActualizado.getOdontologo().getCodigo(), "El código debería estar actualizado");
  }

  @Test
  @Order(5)
  @DisplayName("Eliminar un turno")
  void testDelete() {
    boolean eliminado = turnoService.deleteTurno(6);
    assertTrue(eliminado, "La eliminación de turno debería ser exitosa");
    assertNull(turnoService.getTurnoById(6), "El turno no debería de existir después de eliminado");
  }
}

