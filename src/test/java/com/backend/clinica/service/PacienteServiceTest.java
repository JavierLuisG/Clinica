package com.backend.clinica.service;

import com.backend.clinica.dao.impl.DomicilioDaoH2;
import com.backend.clinica.dao.impl.PacienteDaoH2;
import com.backend.clinica.db.H2Connection;
import com.backend.clinica.dto.request.DomicilioRequestDto;
import com.backend.clinica.dto.request.PacienteRequestDto;
import com.backend.clinica.dto.response.DomicilioResponseDto;
import com.backend.clinica.dto.response.PacienteResponseDto;
import com.backend.clinica.service.impl.DomicilioService;
import com.backend.clinica.service.impl.PacienteService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PacienteServiceTest {
  private static PacienteService pacienteService;
  private static DomicilioService domicilioService;

  @BeforeAll
  static void setUp() {
    H2Connection.createTable();
    pacienteService = new PacienteService(new PacienteDaoH2(), new DomicilioDaoH2());
    domicilioService = new DomicilioService(new DomicilioDaoH2());
  }

  @Test
  @DisplayName("Agregar un paciente con domicilio nuevo y buscarlo en la db")
  void testInsertPacienteWithNewDomicilio() {
    // Crear un nuevo domicilio
    DomicilioRequestDto domicilioCreado = new DomicilioRequestDto("Cl. Siempre Viva", "247", "Spring-field", "Texas");

    // Crear paciente con ese domicilio
    PacienteRequestDto pacienteCreado = new PacienteRequestDto("Bart", "Simpson", "22123378", LocalDate.now().toString(), domicilioCreado);
    PacienteResponseDto resPaciente = pacienteService.createPaciente(pacienteCreado);

    assertNotNull(resPaciente.getDomicilioResponseDto(), "El domicilio debería haberse creado");
    assertNotNull(resPaciente, "El paciente debería haberse creado");
    assertNotNull(resPaciente.getDomicilioResponseDto().getId(), "El domicilio debería tener un ID asignado");
    assertEquals("Bart", resPaciente.getNombre(), "El nombre del paciente debe coincidir");
    assertEquals("Simpson", resPaciente.getApellido(), "El apellido del paciente debe coincidir");
    assertEquals("22123378", resPaciente.getDni(), "El DNI del paciente debe coincidir");

    // Verificar que se puede recuperar por DNI
    PacienteResponseDto pacienteRecuperado = pacienteService.getPacienteByDni("22123378");
    assertNotNull(pacienteRecuperado, "Se debería poder recuperar el paciente por DNI");
    assertEquals(resPaciente.getId(), pacienteRecuperado.getId(), "Los IDs deben coincidir");
  }

  @Test
  @DisplayName("Buscar paciente por DNI")
  void testGetPacienteByDni() {
    PacienteResponseDto pacienteObtenido = pacienteService.getPacienteByDni("12345678");
    assertNotNull(pacienteObtenido, "El paciente debería existir");
//    assertEquals("Bruce", pacienteObtenido.getNombre(), "El nombre debe coincidir");
//    assertEquals("Wayne", pacienteObtenido.getApellido(), "El apellido debe coincidir");
//    assertEquals("87654321", pacienteObtenido.getDni(), "El DNI debe coincidir");
//    assertNotNull(pacienteObtenido.getDomicilioResponseDto(), "El domicilio no debería ser nulo");
//    assertEquals("Calle Falsa", pacienteObtenido.getDomicilioResponseDto().getCalle(), "La calle debe coincidir");assertEquals("Bruce", pacienteObtenido.getNombre(), "El nombre debe coincidir");

    assertEquals("Simpson nuevo", pacienteObtenido.getApellido(), "El apellido debe coincidir");
    assertEquals("12345678", pacienteObtenido.getDni(), "El DNI debe coincidir");
    assertNotNull(pacienteObtenido.getDomicilioResponseDto(), "El domicilio no debería ser nulo");
    assertEquals("Av. Siempre Viva", pacienteObtenido.getDomicilioResponseDto().getCalle(), "La calle debe coincidir");
  }

  @Test
  @DisplayName("Buscar todos los pacientes con sus domicilios")
  void testGetAllPacientes() {
    List<PacienteResponseDto> listaPacientes = pacienteService.getAllPacientes();
    assertNotNull(listaPacientes, "La lista no debería ser nula");
    assertTrue(listaPacientes.size() >= 3, "Debería haber al menos 3 pacientes");

    // Verificar que todos los pacientes tienen un domicilio asignado
    for (PacienteResponseDto paciente : listaPacientes) {
      assertNotNull(paciente.getDomicilioResponseDto(), "Todos los pacientes deberían tener un domicilio");
    }
  }

  @Test
  @DisplayName("Listar todos los domicilios")
  void testGetAllDomicilios() {
    List<DomicilioResponseDto> listaDomicilios = domicilioService.getAllDomicilios();
    assertNotNull(listaDomicilios, "La lista no debería ser nula");
    assertTrue(listaDomicilios.size() >= 3, "Debería haber al menos 3 domicilios");
  }

  @Test
  @DisplayName("Actualizar información de un paciente")
  void testUpdatePaciente() {
    // Obtener un paciente existente
    PacienteResponseDto pacienteOriginal = pacienteService.getPacienteByDni("12345678");
    assertNotNull(pacienteOriginal, "El paciente debería existir");

    // Cambiar algunos datos
    String nuevoNombre = "Homer nuevo";
    String nuevoApellido = "Simpson nuevo";

    DomicilioRequestDto domicilioRequestDto = new DomicilioRequestDto(
            pacienteOriginal.getDomicilioResponseDto().getCalle(),
            pacienteOriginal.getDomicilioResponseDto().getNumero(),
            pacienteOriginal.getDomicilioResponseDto().getLocalidad(),
            pacienteOriginal.getDomicilioResponseDto().getCiudad());
    // Crear paciente con datos actualizados pero manteniendo el mismo domicilio
    PacienteRequestDto pacienteActualizado = new PacienteRequestDto(
            nuevoNombre,
            nuevoApellido,
            pacienteOriginal.getDni(),
            pacienteOriginal.getFechaRegistro(),
            domicilioRequestDto);

    // Actualizar paciente
    PacienteResponseDto resultado = pacienteService.updatePaciente("12345678", pacienteActualizado);

    // Verificar actualización
    assertNotNull(resultado, "El resultado no debería ser nulo");
    assertEquals(nuevoNombre, resultado.getNombre(), "El nombre debería haberse actualizado");
    assertEquals(nuevoApellido, resultado.getApellido(), "El apellido debería haberse actualizado");
    assertEquals(pacienteOriginal.getDni(), resultado.getDni(), "El DNI debería seguir siendo el mismo");
    assertEquals(pacienteOriginal.getDomicilioResponseDto().getId(), resultado.getDomicilioResponseDto().getId(), "El ID del domicilio debería seguir siendo el mismo");

    // Verificar recuperando el paciente otra vez
    PacienteResponseDto verificacion = pacienteService.getPacienteByDni("12345678");
    assertEquals(nuevoNombre, verificacion.getNombre(), "El nombre debería seguir actualizado");
  }

  @Test
  @DisplayName("Actualizar información de un domicilio")
  void testUpdateDomicilio() {
    //Obtener paciente
    PacienteResponseDto pacienteObtenido = pacienteService.getPacienteByDni("56781234");

    // Obtener domicilio del paciente
    DomicilioResponseDto domicilioPaciente = pacienteObtenido.getDomicilioResponseDto();
    assertNotNull(domicilioPaciente.getId(), "El domicilio debería existir y con Id");

    // Cambiar algunos datos
    String nuevaCalle = "Evergreen Terrace";
    String nuevaLocalidad = "Springfield";

    DomicilioRequestDto domicilioRequestDto = new DomicilioRequestDto(
            nuevaCalle,
            domicilioPaciente.getNumero(),
            nuevaLocalidad,
            domicilioPaciente.getCiudad());
    PacienteRequestDto pacienteRequestDto = new PacienteRequestDto(
            pacienteObtenido.getNombre(),
            pacienteObtenido.getApellido(),
            pacienteObtenido.getDni(),
            pacienteObtenido.getFechaRegistro(),
            domicilioRequestDto);

    // Actualizar domicilio en paciente
    PacienteResponseDto pacienteActualizado = pacienteService.updatePaciente(pacienteObtenido.getDni(), pacienteRequestDto);

    // Verificar actualización
    assertNotNull(pacienteActualizado, "El resultado no debería ser nulo");
    assertEquals(nuevaCalle, pacienteActualizado.getDomicilioResponseDto().getCalle(), "La calle debería haberse actualizado");
    assertEquals(nuevaLocalidad, pacienteActualizado.getDomicilioResponseDto().getLocalidad(), "La localidad debería haberse actualizado");
    assertEquals(domicilioPaciente.getNumero(), pacienteActualizado.getDomicilioResponseDto().getNumero(), "El número debería seguir siendo el mismo");

    // Verificar recuperando el domicilio otra vez
    PacienteResponseDto verificacion = pacienteService.getPacienteByDni("56781234");
    assertEquals(nuevaCalle, verificacion.getDomicilioResponseDto().getCalle(), "La calle debería seguir actualizada");
  }

  @Test
  @DisplayName("Eliminar un paciente")
  void testDeletePaciente() {
    // Crear un paciente que luego se eliminará
    DomicilioRequestDto domicilioCreado = new DomicilioRequestDto("de prueba", "de prueba", "de prueba", "de prueba");
    PacienteRequestDto pacienteParaEliminar = new PacienteRequestDto("Temporal", "temporal", "99887768", LocalDate.now().toString(), domicilioCreado);
    PacienteResponseDto pacienteCreado = pacienteService.createPaciente(pacienteParaEliminar);
    assertNotNull(pacienteCreado, "El paciente debería haberse creado");

    // Verificar que existe
    PacienteResponseDto verificacionExistencia = pacienteService.getPacienteByDni("99887768");
    assertNotNull(verificacionExistencia, "El paciente debería existir antes de eliminarlo");

    // Eliminar paciente
    boolean resultado = pacienteService.deletePaciente("99887768");
    assertTrue(resultado, "La eliminación debería ser exitosa");

    // Verificar que ya no se puede recuperar
    PacienteResponseDto verificacionEliminacion = pacienteService.getPacienteByDni("99887768");
    assertNull(verificacionEliminacion, "El paciente no debería existir después de eliminarlo");
  }

  @Test
  @DisplayName("Probar manejo de errores con entradas inválidas")
  void testErrorHandling() {
    // Buscar paciente con DNI nulo
    PacienteResponseDto pacienteNulo = pacienteService.getPacienteByDni(null);
    assertNull(pacienteNulo, "Un DNI nulo debería devolver null");

    // Buscar paciente con DNI inexistente
    PacienteResponseDto pacienteInexistente = pacienteService.getPacienteByDni("00000000");
    assertNull(pacienteInexistente, "Un DNI inexistente debería devolver null");

    // Buscar domicilio con ID nulo
    DomicilioResponseDto domicilioNulo = domicilioService.getDomicilioById(null);
    assertNull(domicilioNulo, "Un ID nulo debería devolver null");

    // Buscar domicilio con ID inexistente
    DomicilioResponseDto domicilioInexistente = domicilioService.getDomicilioById(9999);
    assertNull(domicilioInexistente, "Un ID inexistente debería devolver null");
  }
}