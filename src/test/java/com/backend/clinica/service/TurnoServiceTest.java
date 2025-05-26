package com.backend.clinica.service;

import com.backend.clinica.dao.impl.OdontologoDaoH2;
import com.backend.clinica.dao.impl.PacienteDaoH2;
import com.backend.clinica.dao.impl.TurnoDaoH2;
import com.backend.clinica.db.H2Connection;
import com.backend.clinica.dto.request.TurnoRequestDto;
import com.backend.clinica.dto.response.TurnoResponseDto;
import com.backend.clinica.model.Odontologo;
import com.backend.clinica.service.impl.OdontologoService;
import com.backend.clinica.service.impl.TurnoService;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TurnoServiceTest {
  private static TurnoService turnoService;
  private static OdontologoService odontologoService;

  @BeforeAll
  static void setUp() {
    H2Connection.createTable();
    turnoService = new TurnoService(new TurnoDaoH2(), new OdontologoDaoH2(), new PacienteDaoH2());
    odontologoService = new OdontologoService(new OdontologoDaoH2());
  }

  @Test
  @Order(1)
  @DisplayName("Agregar un turno con odontologo y paciente ya existentes")
  void testInsertOne() {
    TurnoRequestDto turnoRequestDto = new TurnoRequestDto(
            "2025-07-15",
            "OD00125",
            "56781234");

    TurnoResponseDto insertado = turnoService.createTurno(turnoRequestDto);

    assertNotNull(insertado);
    assertNotNull(insertado.getId());
    assertEquals(3, insertado.getPacienteResponseDto().getId());
    assertEquals(3, insertado.getPacienteResponseDto().getId());
  }

  @Test
  @Order(2)
  @DisplayName("Buscar turno por id")
  void testReadOne() {
    TurnoResponseDto turno = turnoService.getTurnoById(1);

    assertNotNull(turno, "El turno debería existir");
    assertEquals("2025-05-25", turno.getFechaConsulta(), "La fecha debe coincidir");
    assertEquals(1, turno.getId(), "El id de turno debe coincidir");
    assertEquals(2, turno.getOdontologoResponseDto().getId(), "El id de odontólogo debe coincidir");
    assertEquals(1, turno.getPacienteResponseDto().getId(), "El id de paciente debe coincidir");
  }

  @Test
  @Order(3)
  @DisplayName("Buscar todos los turnos")
  void testReadAll() {
    List<TurnoResponseDto> listTurnos = turnoService.getAllTurnos();
    assertNotNull(listTurnos, "La lista no debería ser nula");
    assertTrue(listTurnos.size() >= 3, "Debería haber al menos 3 turnos");

    for (TurnoResponseDto turno : listTurnos) {
      assertNotNull(turno.getOdontologoResponseDto(), "Todos los turnos deben tener un odontólogo");
      assertNotNull(turno.getPacienteResponseDto(), "Todos los turnos deben tener un paciente");
    }
  }

  @Test
  @Order(4)
  @DisplayName("Actualizar informacion de un turno")
  void testUpdate() {
    TurnoResponseDto turnoOriginal = turnoService.getTurnoById(7);
    assertNotNull(turnoOriginal, "El turno debería existir");

    Odontologo odontologo = odontologoService.getOdontologoByCodigo("OD00127");
    assertNotNull(odontologo, "El odontólogo debería existir para asignarlo en el turno Nro 1");

    TurnoRequestDto turnoNuevo = new TurnoRequestDto(turnoOriginal.getFechaConsulta(), odontologo.getCodigo(), turnoOriginal.getPacienteResponseDto().getDni());
    TurnoResponseDto turnoActualizado = turnoService.updateTurno(1, turnoNuevo);
    assertNotNull(turnoActualizado, "El resultado no debería ser nulo");

    TurnoResponseDto verificarTurnoActualizado = turnoService.getTurnoById(turnoActualizado.getId());
    assertEquals("OD00127", verificarTurnoActualizado.getOdontologoResponseDto().getCodigo(), "El código debería estar actualizado");
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

