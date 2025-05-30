package com.backend.clinica.service;

import com.backend.clinica.dto.request.DomicilioRequestDto;
import com.backend.clinica.dto.request.PacienteRequestDto;
import com.backend.clinica.dto.response.PacienteResponseDto;
import com.backend.clinica.service.impl.DomicilioService;
import com.backend.clinica.service.impl.PacienteService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PacienteServiceTest {
  @Autowired
  private PacienteService pacienteService;
  @Autowired
  private DomicilioService domicilioService;
  private static final String DNI_BART = "22123378";
  private static final String DNI_TEMP = "99887768";

  @Test
  @Order(1)
  @DisplayName("Agregar un paciente con domicilio nuevo y buscarlo en la db")
  void testInsertPacienteWithNewDomicilio() {
    DomicilioRequestDto domicilioDto = new DomicilioRequestDto("Cl. Siempre Viva", "247", "Spring-field", "Texas");
    PacienteRequestDto pacienteDto = new PacienteRequestDto("Bart", "Simpson", DNI_BART, LocalDateTime.now().toString(), domicilioDto);

    PacienteResponseDto creado = pacienteService.createPaciente(pacienteDto);

    assertNotNull(creado, "El paciente debería haberse creado");
    assertNotNull(creado.getDomicilioResponseDto(), "El domicilio debería haberse creado");
    assertNotNull(creado.getDomicilioResponseDto().getId(), "El domicilio debería tener ID");
    assertEquals("Bart", creado.getNombre());
    assertEquals("Simpson", creado.getApellido());
    assertEquals(DNI_BART, creado.getDni());
  }

  @Test
  @Order(2)
  @DisplayName("Buscar paciente por DNI")
  void testGetPacienteByDni() {
    PacienteResponseDto encontrado = pacienteService.getPacienteByDni(DNI_BART);
    assertNotNull(encontrado, "Debería encontrarse el paciente");
    assertEquals("Bart", encontrado.getNombre());
    assertEquals("Simpson", encontrado.getApellido());
    assertEquals(DNI_BART, encontrado.getDni());
    assertNotNull(encontrado.getDomicilioResponseDto());
    assertEquals("Cl. Siempre Viva", encontrado.getDomicilioResponseDto().getCalle());
  }

  @Test
  @Order(3)
  @DisplayName("Buscar todos los pacientes")
  void testGetAllPacientes() {
    List<PacienteResponseDto> lista = pacienteService.getAllPacientes();
    assertNotNull(lista);
    assertTrue(lista.size() >= 1, "Debería haber al menos un paciente");
    for (PacienteResponseDto paciente : lista) {
      assertNotNull(paciente.getDomicilioResponseDto(), "Todos deberían tener domicilio");
    }
  }

  @Test
  @Order(4)
  @DisplayName("Actualizar información de un paciente")
  void testUpdatePaciente() {
    PacienteResponseDto original = pacienteService.getPacienteByDni(DNI_BART);
    assertNotNull(original);

    DomicilioRequestDto domicilioDto = new DomicilioRequestDto(
            original.getDomicilioResponseDto().getCalle(),
            original.getDomicilioResponseDto().getNumero(),
            original.getDomicilioResponseDto().getLocalidad(),
            original.getDomicilioResponseDto().getCiudad()
    );

    PacienteRequestDto actualizadoDto = new PacienteRequestDto(
            "Bart actualizado",
            "Simpson actualizado",
            original.getDni(),
            original.getFechaRegistro(),
            domicilioDto
    );

    PacienteResponseDto actualizado = pacienteService.updatePaciente(DNI_BART, actualizadoDto);

    assertNotNull(actualizado);
    assertEquals("Bart actualizado", actualizado.getNombre());
    assertEquals("Simpson actualizado", actualizado.getApellido());
    assertEquals(original.getDni(), actualizado.getDni());
    assertEquals(original.getDomicilioResponseDto().getId(), actualizado.getDomicilioResponseDto().getId());

    PacienteResponseDto verificado = pacienteService.getPacienteByDni(DNI_BART);
    assertEquals("Bart actualizado", verificado.getNombre());
  }

  @Test
  @Order(5)
  @DisplayName("Crear y eliminar un paciente")
  void testDeletePaciente() {
    DomicilioRequestDto domicilioDto = new DomicilioRequestDto("Calle Temporal", "123", "Ciudad X", "Provincia Y");
    PacienteRequestDto pacienteDto = new PacienteRequestDto("Temporal", "Paciente", DNI_TEMP, LocalDateTime.now().toString(), domicilioDto);

    PacienteResponseDto creado = pacienteService.createPaciente(pacienteDto);
    assertNotNull(creado, "El paciente debería haberse creado");

    PacienteResponseDto verificado = pacienteService.getPacienteByDni(DNI_TEMP);
    assertNotNull(verificado, "El paciente debería existir antes de eliminarlo");

    boolean eliminado = pacienteService.deletePaciente(DNI_TEMP);
    assertTrue(eliminado, "La eliminación debería ser exitosa");

    assertThrows(EntityNotFoundException.class, () -> {
      pacienteService.getPacienteByDni(DNI_TEMP);
    }, "Se esperaba una EntityNotFoundException por DNI inexistente");
  }

  @Test
  @Order(6)
  @DisplayName("Probar manejo de errores con entradas inválidas")
  void testErrorHandling() {
    // Buscar paciente con DNI null
    assertThrows(IllegalArgumentException.class, () -> {
      pacienteService.getPacienteByDni(null);
    }, "Se esperaba una IllegalArgumentException por DNI nulo");

    // Buscar paciente con DNI inexistente
    assertThrows(EntityNotFoundException.class, () -> {
      pacienteService.getPacienteByDni("00000000");
    }, "Se esperaba una EntityNotFoundException por DNI inexistente");

    // Buscar domicilio con ID null
    assertThrows(IllegalArgumentException.class, () -> {
      domicilioService.getDomicilioById(null);
    }, "Se esperaba una IllegalArgumentException por ID nulo");

    // Buscar domicilio con ID inexistente
    assertThrows(EntityNotFoundException.class, () -> {
      domicilioService.getDomicilioById(9999);
    }, "Se esperaba una EntityNotFoundException por ID inexistente");
  }
}