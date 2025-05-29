package com.backend.clinica.service;

import com.backend.clinica.dto.request.OdontologoRequestDto;
import com.backend.clinica.dto.response.OdontologoResponseDto;
import com.backend.clinica.service.impl.OdontologoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OdontologoServiceTest {
  @Autowired
  private OdontologoService odontologoService;

  @Test
  @Order(1)
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
  @Order(2)
  @DisplayName("Buscar odontólogo por Código")
  void testGetPacienteByDni() {
    OdontologoResponseDto odontologoObtenido = odontologoService.getOdontologoByCodigo("OD00129");
    assertNotNull(odontologoObtenido, "El odontólogo debería existir");
    assertEquals("Camilo", odontologoObtenido.getNombre(), "El nombre debe coincidir");
    assertEquals("Perez", odontologoObtenido.getApellido(), "El apellido debe coincidir");
  }

  @Test
  @Order(3)
  @DisplayName("Listar todos los odontólogos")
  void obtenerOdontologos() {
    List<OdontologoResponseDto> listaObtenida = odontologoService.getAllOdontologos();

    assertNotNull(listaObtenida, "La lista no debería ser nula");
    assertTrue(listaObtenida.size() >= 1, "Debería haber al menos 1 paciente");
  }

  @Test
  @Order(4)
  @DisplayName("Actualizar información de un odontólogo")
  void testUpdateOdontólogo() {
    // Obtener un odontólogo existente
    OdontologoResponseDto odontologoOriginal = odontologoService.getOdontologoByCodigo("OD00129");
    assertNotNull(odontologoOriginal, "El odontólogo debería existir");

    // Cambiar algunos datos
    String nuevoNombre = "Camilo nuevo";
    String nuevoApellido = "Perez nuevo";

    // Crear odontólogo con datos actualizados
    OdontologoRequestDto odontologoActualizado = new OdontologoRequestDto(
            odontologoOriginal.getCodigo(),
            nuevoNombre,
            nuevoApellido);

    // Actualizar odontólogo
    OdontologoResponseDto resultado = odontologoService.updateOdontologo("OD00129", odontologoActualizado);

    // Verificar actualización
    assertNotNull(resultado, "El resultado no debería ser nulo");
    assertEquals(nuevoNombre, resultado.getNombre(), "El nombre debería haberse actualizado");
    assertEquals(nuevoApellido, resultado.getApellido(), "El apellido debería haberse actualizado");
    assertEquals(odontologoOriginal.getCodigo(), resultado.getCodigo(), "El Código debería seguir siendo el mismo");

    // Verificar recuperando el paciente otra vez
    OdontologoResponseDto verificacion = odontologoService.getOdontologoByCodigo("OD00129");
    assertEquals(nuevoNombre, verificacion.getNombre(), "El nombre debería seguir actualizado");
  }

  @Test
  @Order(5)
  @DisplayName("Eliminar un odontólogo")
  void testDeletePaciente() {
    // Verificar que existe
    OdontologoResponseDto verificacionExistencia = odontologoService.getOdontologoByCodigo("OD00129");
    assertNotNull(verificacionExistencia, "El odontólogo debería existir antes de eliminarlo");

    // Eliminar odontólogo
    boolean resultado = odontologoService.deleteOdontologo("OD00129");
    assertTrue(resultado, "La eliminación debería ser exitosa");

    // Verificar que ya no se puede recuperar
    // OdontologoResponseDto verificacionEliminacion = odontologoService.getOdontologoByCodigo("OD00129");
    // assertNull(verificacionEliminacion, "El odontólogo no debería existir después de eliminarlo");
  }

  @Test
  @Order(6)
  @DisplayName("Probar manejo de errores con entradas inválidas")
  void testErrorHandling() {
    // Buscar odontólogo con codigo null
    assertThrows(IllegalArgumentException.class, () -> {
      odontologoService.getOdontologoByCodigo(null);
    }, "Se esperaba una IllegalArgumentException por código nulo");

    // Buscar odontólogo con Matrícula inexistente
    assertThrows(EntityNotFoundException.class, () -> {
      odontologoService.getOdontologoByCodigo("00000000");
    }, "Se esperaba una EntityNotFoundException por código inexistente");
  }
}
