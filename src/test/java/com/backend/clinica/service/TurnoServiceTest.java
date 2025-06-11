package com.backend.clinica.service;

import com.backend.clinica.dto.request.DomicilioRequestDto;
import com.backend.clinica.dto.request.OdontologoRequestDto;
import com.backend.clinica.dto.request.PacienteRequestDto;
import com.backend.clinica.dto.request.TurnoRequestDto;
import com.backend.clinica.dto.response.OdontologoResponseDto;
import com.backend.clinica.dto.response.PacienteResponseDto;
import com.backend.clinica.dto.response.TurnoResponseDto;
import com.backend.clinica.exception.IllegalArgException;
import com.backend.clinica.exception.ResourceNotFoundException;
import com.backend.clinica.service.impl.OdontologoService;
import com.backend.clinica.service.impl.PacienteService;
import com.backend.clinica.service.impl.TurnoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TurnoServiceTest {
  @Autowired
  private TurnoService turnoService;
  @Autowired
  private OdontologoService odontologoService;
  @Autowired
  private PacienteService pacienteService;
  private static final String CODIGO_ODONTOLOGO = "OD00001";
  private static final String DNI_PACIENTE = "12345678";
  private static Integer turnoCreadoId;

  @Test
  @Order(1)
  @DisplayName("Crear un turno con paciente y odontólogo existentes")
  void testCreateTurno() {
    DomicilioRequestDto domicilioRequestDto = new DomicilioRequestDto("Av Siempre Viva", "742", "Springfield", "Texas");
    PacienteRequestDto pacienteRequestDto = new PacienteRequestDto("Homero", "Simpson", DNI_PACIENTE, LocalDateTime.now().toString(), domicilioRequestDto);
    PacienteResponseDto createdPaciente = pacienteService.createPaciente(pacienteRequestDto);

    // Creamos odontólogo si no existe
    OdontologoRequestDto odontologoRequestDto = new OdontologoRequestDto(CODIGO_ODONTOLOGO, "Lisa", "Simpson");
    OdontologoResponseDto createdOdontologo = odontologoService.createOdontologo(odontologoRequestDto);

    TurnoRequestDto turnoDto = new TurnoRequestDto("2025-08-15T10:30:00", createdOdontologo.getCodigo(), createdPaciente.getDni());
    TurnoResponseDto creado = turnoService.createTurno(turnoDto);

    assertNotNull(creado, "Turno debería estar creado");
    assertNotNull(creado.getId(), "Turno debería tener id");
    assertEquals(CODIGO_ODONTOLOGO, creado.getOdontologoResponseDto().getCodigo(), "Código debería coincidir");
    assertEquals(DNI_PACIENTE, creado.getPacienteResponseDto().getDni(), "Dni debería coincidir");

    turnoCreadoId = creado.getId();
  }

  @Test
  @Order(2)
  @DisplayName("Buscar turno por ID")
  void testGetTurnoById() {
    TurnoResponseDto turno = turnoService.getTurnoById(turnoCreadoId);
    assertNotNull(turno);
    assertEquals(turnoCreadoId, turno.getId());
    assertEquals(DNI_PACIENTE, turno.getPacienteResponseDto().getDni());
  }

  @Test
  @Order(3)
  @DisplayName("Listar todos los turnos")
  void testGetAllTurnos() {
    List<TurnoResponseDto> turnos = turnoService.getAllTurnos();
    assertNotNull(turnos);
    assertTrue(turnos.size() >= 1, "Debería haber al menos un turno");
    for (TurnoResponseDto turno : turnos) {
      assertNotNull(turno.getOdontologoResponseDto());
      assertNotNull(turno.getPacienteResponseDto());
    }
  }

  @Test
  @Order(4)
  @DisplayName("Actualizar un turno")
  void testUpdateTurno() {
    OdontologoResponseDto nuevoOdontologo = odontologoService.createOdontologo(new OdontologoRequestDto("OD99999", "Marvin", "Monroe"));
    TurnoRequestDto nuevoTurnoDto = new TurnoRequestDto("2025-09-01T09:25:00", nuevoOdontologo.getCodigo(), DNI_PACIENTE);

    TurnoResponseDto actualizado = turnoService.updateTurno(turnoCreadoId, nuevoTurnoDto);
    assertNotNull(actualizado);
    assertEquals("OD99999", actualizado.getOdontologoResponseDto().getCodigo());
    assertEquals("2025-09-01T09:25", actualizado.getFechaConsulta());
  }

  @Test
  @Order(5)
  @DisplayName("Eliminar un turno")
  void testDeleteTurno() throws ResourceNotFoundException, IllegalArgException {
    turnoService.deleteTurno(turnoCreadoId);

    assertThrows(EntityNotFoundException.class, () -> {
      turnoService.getTurnoById(turnoCreadoId);
    });
  }

  @Test
  @Order(6)
  @DisplayName("Manejo de errores")
  void testErrorHandling() {
    // Buscar con ID nulo
    assertThrows(IllegalArgumentException.class, () -> {
      turnoService.getTurnoById(null);
    });

    // Buscar turno inexistente
    assertThrows(EntityNotFoundException.class, () -> {
      turnoService.getTurnoById(999999);
    });
  }
}
