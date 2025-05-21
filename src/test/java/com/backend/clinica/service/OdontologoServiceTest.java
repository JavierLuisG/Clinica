package com.backend.clinica.service;

import com.backend.clinica.dao.impl.OdontologoDaoH2;
import com.backend.clinica.db.H2Connection;
import com.backend.clinica.model.Odontologo;
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
    Odontologo odontologoCreado = new Odontologo("OD00129", "Camilo", "Perez");
    Odontologo resOdontologo = odontologoService.createOdontologo(odontologoCreado);
    assertNotNull(resOdontologo, "El odontólogo debería haberse creado");
    assertNotNull(resOdontologo.getId(), "El odontólogo debería tener un ID asignado");

    // Verificar que se puede recuperar por matricula
    Odontologo odontologoRecuperado = odontologoService.getOdontologoByCodigo("OD00129");
    assertNotNull(odontologoRecuperado, "Se debería poder recuperar el odontólogo por Código");
  }

  @Test
  @DisplayName("Buscar odontólogo por Código")
  void testGetPacienteByDni() {
    Odontologo odontologoObtenido = odontologoService.getOdontologoByCodigo("OD00127");
    assertNotNull(odontologoObtenido, "El odontólogo debería existir");
    assertEquals("María", odontologoObtenido.getNombre(), "El nombre debe coincidir");
    assertEquals("López", odontologoObtenido.getApellido(), "El apellido debe coincidir");
  }

  @Test
  @DisplayName("Listar todos los odontólogos")
  void obtenerOdontologos() {
    List<Odontologo> listaObtenida = odontologoService.getAllOdontologos();
    assertEquals(7, listaObtenida.size());

    assertNotNull(listaObtenida, "La lista no debería ser nula");
    assertTrue(listaObtenida.size() >= 3, "Debería haber al menos 3 pacientes");
  }

  @Test
  @DisplayName("Actualizar información de un odontólogo")
  void testUpdateOdontólogo() {
    // Obtener un odontólogo existente
    Odontologo odontologoOriginal = odontologoService.getOdontologoByCodigo("OD00125");
    assertNotNull(odontologoOriginal, "El odontólogo debería existir");

    // Cambiar algunos datos
    String nuevoNombre = "Homero nuevo";
    String nuevoApellido = "Simpson nuevo";

    // Crear odontólogo con datos actualizados
    Odontologo odontologoActualizado = new Odontologo(
            odontologoOriginal.getCodigo(),
            nuevoNombre,
            nuevoApellido
    );

    // Actualizar odontólogo
    Odontologo resultado = odontologoService.updateOdontologo("OD00125", odontologoActualizado);

    // Verificar actualización
    assertNotNull(resultado, "El resultado no debería ser nulo");
    assertEquals(nuevoNombre, resultado.getNombre(), "El nombre debería haberse actualizado");
    assertEquals(nuevoApellido, resultado.getApellido(), "El apellido debería haberse actualizado");
    assertEquals(odontologoOriginal.getCodigo(), resultado.getCodigo(), "El Código debería seguir siendo el mismo");

    // Verificar recuperando el paciente otra vez
    Odontologo verificacion = odontologoService.getOdontologoByCodigo("OD00125");
    assertEquals(nuevoNombre, verificacion.getNombre(), "El nombre debería seguir actualizado");
  }

  @Test
  @DisplayName("Eliminar un odontólogo")
  void testDeletePaciente() {
    // Verificar que existe
    Odontologo verificacionExistencia = odontologoService.getOdontologoByCodigo("OD00125");
    assertNotNull(verificacionExistencia, "El odontólogo debería existir antes de eliminarlo");

    // Eliminar odontólogo
    boolean resultado = odontologoService.deleteOdontologo("OD00125");
    assertTrue(resultado, "La eliminación debería ser exitosa");

    // Verificar que ya no se puede recuperar
    Odontologo verificacionEliminacion = odontologoService.getOdontologoByCodigo("OD00125");
    assertNull(verificacionEliminacion, "El odontólogo no debería existir después de eliminarlo");
  }

  @Test
  @DisplayName("Probar manejo de errores con entradas inválidas")
  void testErrorHandling() {
    // Buscar odontólogo con Matrícula null
    Odontologo odontologoNulo = odontologoService.getOdontologoByCodigo(null);
    assertNull(odontologoNulo, "Un Código nulo debería devolver null");

    // Buscar odontólogo con Matrícula inexistente
    Odontologo odontologoInexistente = odontologoService.getOdontologoByCodigo("00000000");
    assertNull(odontologoInexistente, "Un Código inexistente debería devolver null");
  }
}
