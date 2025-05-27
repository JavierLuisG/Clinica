package com.backend.clinica.service;

import com.backend.clinica.dao.impl.OdontologoDaoH2;
import com.backend.clinica.db.H2Connection;
import com.backend.clinica.dto.request.OdontologoRequestDto;
import com.backend.clinica.dto.response.OdontologoResponseDto;
import com.backend.clinica.service.impl.OdontologoService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OdontologoServiceTest {
  private static OdontologoService odontologoService;

  @BeforeAll
  static void setUp() {
    H2Connection.createTable();
    odontologoService = new OdontologoService(new OdontologoDaoH2());
  }

  @Test
  @DisplayName("Agregar un odontólogo")
  void insertarOdontologo() {
    // Crear un nuevo odontologo
    OdontologoRequestDto odontologoCreado = new OdontologoRequestDto("OD00129", "Camilo", "Perez");
    OdontologoResponseDto resOdontologo = odontologoService.createOdontologo(odontologoCreado);
    assertNotNull(resOdontologo, "El odontólogo debería haberse creado");
    assertNotNull(resOdontologo.getId(), "El odontólogo debería tener un ID asignado");

    // Verificar que se puede recuperar por matricula
    OdontologoResponseDto odontologoRecuperado = odontologoService.getOdontologoByCodigo("OD00129");
    assertNotNull(odontologoRecuperado, "Se debería poder recuperar el odontólogo por Código");
  }

  @Test
  @DisplayName("Buscar odontólogo por Código")
  void testGetPacienteByDni() {
    OdontologoResponseDto odontologoObtenido = odontologoService.getOdontologoByCodigo("OD00127");
    assertNotNull(odontologoObtenido, "El odontólogo debería existir");
    assertEquals("María", odontologoObtenido.getNombre(), "El nombre debe coincidir");
    assertEquals("López", odontologoObtenido.getApellido(), "El apellido debe coincidir");
  }

  @Test
  @DisplayName("Listar todos los odontólogos")
  void obtenerOdontologos() {
    List<OdontologoResponseDto> listaObtenida = odontologoService.getAllOdontologos();
    assertEquals(7, listaObtenida.size());

    assertNotNull(listaObtenida, "La lista no debería ser nula");
    assertTrue(listaObtenida.size() >= 3, "Debería haber al menos 3 pacientes");
  }

  @Test
  @DisplayName("Actualizar información de un odontólogo")
  void testUpdateOdontólogo() {
    // Obtener un odontólogo existente
    OdontologoResponseDto odontologoOriginal = odontologoService.getOdontologoByCodigo("OD00125");
    assertNotNull(odontologoOriginal, "El odontólogo debería existir");

    // Cambiar algunos datos
    String nuevoNombre = "Homero nuevo";
    String nuevoApellido = "Simpson nuevo";

    // Crear odontólogo con datos actualizados
    OdontologoRequestDto odontologoActualizado = new OdontologoRequestDto(
            odontologoOriginal.getCodigo(),
            nuevoNombre,
            nuevoApellido);

    // Actualizar odontólogo
    OdontologoResponseDto resultado = odontologoService.updateOdontologo("OD00125", odontologoActualizado);

    // Verificar actualización
    assertNotNull(resultado, "El resultado no debería ser nulo");
    assertEquals(nuevoNombre, resultado.getNombre(), "El nombre debería haberse actualizado");
    assertEquals(nuevoApellido, resultado.getApellido(), "El apellido debería haberse actualizado");
    assertEquals(odontologoOriginal.getCodigo(), resultado.getCodigo(), "El Código debería seguir siendo el mismo");

    // Verificar recuperando el paciente otra vez
    OdontologoResponseDto verificacion = odontologoService.getOdontologoByCodigo("OD00125");
    assertEquals(nuevoNombre, verificacion.getNombre(), "El nombre debería seguir actualizado");
  }

  @Test
  @DisplayName("Eliminar un odontólogo")
  void testDeletePaciente() {
    // Verificar que existe
    OdontologoResponseDto verificacionExistencia = odontologoService.getOdontologoByCodigo("OD00125");
    assertNotNull(verificacionExistencia, "El odontólogo debería existir antes de eliminarlo");

    // Eliminar odontólogo
    boolean resultado = odontologoService.deleteOdontologo("OD00125");
    assertTrue(resultado, "La eliminación debería ser exitosa");

    // Verificar que ya no se puede recuperar
    OdontologoResponseDto verificacionEliminacion = odontologoService.getOdontologoByCodigo("OD00125");
    assertNull(verificacionEliminacion, "El odontólogo no debería existir después de eliminarlo");
  }

  @Test
  @DisplayName("Probar manejo de errores con entradas inválidas")
  void testErrorHandling() {
    // Buscar odontólogo con Matrícula null
    OdontologoResponseDto odontologoNulo = odontologoService.getOdontologoByCodigo(null);
    assertNull(odontologoNulo, "Un Código nulo debería devolver null");

    // Buscar odontólogo con Matrícula inexistente
    OdontologoResponseDto odontologoInexistente = odontologoService.getOdontologoByCodigo("00000000");
    assertNull(odontologoInexistente, "Un Código inexistente debería devolver null");
  }
}
