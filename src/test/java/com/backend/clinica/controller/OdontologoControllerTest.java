package com.backend.clinica.controller;

import com.backend.clinica.dto.request.EspecialidadRequestDto;
import com.backend.clinica.dto.request.OdontologoRequestDto;
import com.backend.clinica.dto.response.EspecialidadResponseDto;
import com.backend.clinica.dto.response.OdontologoResponseDto;
import com.backend.clinica.service.IOdontologoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class OdontologoControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private IOdontologoService<String, OdontologoRequestDto, OdontologoResponseDto> odontologoService;

  @Test
  void mostrarDatasourceUrl(@Value("${spring.datasource.url}") String url) {
    System.out.println(">>> URL: " + url);
  }

  @Test
  @DisplayName("Test, guardar odontologo en DB")
  void postOdontologo() throws Exception {
    String odontologo = """
        {
            "codigo": "ODON001",
            "nombre": "Pepito",
            "apellido": "Perez"
        }
        """;
    mockMvc.perform(post("/odontologo")
            .contentType(MediaType.APPLICATION_JSON)
            .content(odontologo))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath(("$.codigo")).value("ODON001"))
        .andExpect(jsonPath(("$.nombre")).value("Pepito"))
        .andExpect(jsonPath(("$.apellido")).value("Perez"))
        .andExpect(jsonPath("$.especialidadesResponseDto").isArray())
        .andExpect(jsonPath("$.especialidadesResponseDto.length()").value(0));
  }

  @Test
  @DisplayName("Test, obtencion de odontologo despues de crearlo")
  void getOdontologo() throws Exception {
    String odontologo = """
        {
            "codigo": "ODON002",
            "nombre": "Ana",
            "apellido": "Gomez"
        }
        """;
    mockMvc.perform(post("/odontologo")
            .contentType(MediaType.APPLICATION_JSON)
            .content(odontologo))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/odontologo/ODON002")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nombre").value("Ana"));
  }

  @Test
  @DisplayName("Test, notFount de un odontologo")
  void getNotFountOdontologo() throws Exception {
    mockMvc.perform(get("/odontologo/ODON111")
            .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("Test, obtener lista de odontólogos")
  void getAllOdontologos() throws Exception {
    String odontologo = """
        {
            "codigo": "ODON003",
            "nombre": "Carlos",
            "apellido": "Ramirez"
        }
        """;
    mockMvc.perform(post("/odontologo")
            .contentType(MediaType.APPLICATION_JSON)
            .content(odontologo))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/odontologo")
            .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThan(0)));
  }

  @Test
  @DisplayName("Test, actualizar un odontólogo")
  void putOdontologo() throws Exception {
    String odontologo = """
        {
            "codigo": "ODON004",
            "nombre": "Lucía",
            "apellido": "Fernandez"
        }
        """;
    mockMvc.perform(post("/odontologo")
            .contentType(MediaType.APPLICATION_JSON)
            .content(odontologo))
        .andExpect(status().isCreated());

    String updatedOdontologo = """
        {
            "codigo": "ODON004",
            "nombre": "Lucía Modificada",
            "apellido": "Fernandez Modificada"
        }
        """;

    mockMvc.perform(put("/odontologo/ODON004")
            .contentType(MediaType.APPLICATION_JSON)
            .content(updatedOdontologo))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nombre").value("Lucía Modificada"))
        .andExpect(jsonPath("$.apellido").value("Fernandez Modificada"));
  }

  @Test
  @DisplayName("Test, eliminar odontólogo por código")
  void deleteOdontologo() throws Exception {
    String odontologo = """
        {
            "codigo": "ODON005",
            "nombre": "Mario",
            "apellido": "Suarez"
        }
        """;
    mockMvc.perform(post("/odontologo")
            .contentType(MediaType.APPLICATION_JSON)
            .content(odontologo))
        .andExpect(status().isCreated());

    mockMvc.perform(delete("/odontologo/ODON005"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Odontólogo ODON005 eliminado"));

    mockMvc.perform(get("/odontologo/ODON005"))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("Test, asignar especialidad a odontólogo")
  void assignEspecialidad() throws Exception {
    // Crear odontólogo
    String odontologo = """
        {
            "codigo": "ODON006",
            "nombre": "Valeria",
            "apellido": "Torres"
        }
        """;
    String responseBody = mockMvc.perform(post("/odontologo")
            .contentType(MediaType.APPLICATION_JSON)
            .content(odontologo))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();

    // Extraer id del odontólogo
    int odontologoId = JsonPath.read(responseBody, "$.id");

    EspecialidadRequestDto especialidadRequestDto = new EspecialidadRequestDto();
    especialidadRequestDto.setTipo("Ortodoncia");

    String response = mockMvc.perform(post("/especialidad")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(especialidadRequestDto)))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

    EspecialidadResponseDto especialidad = objectMapper.readValue(response, EspecialidadResponseDto.class);
    Integer especialidadId = especialidad.getId();

    mockMvc.perform(put("/odontologo/" + odontologoId + "/especialidad/" + especialidadId))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.especialidadesResponseDto.length()").value(1));
  }

  @Test
  @DisplayName("Test, eliminar especialidad de odontólogo")
  void removeEspecialidad() throws Exception {
    // Crear odontólogo
    String odontologo = """
        {
            "codigo": "ODON007",
            "nombre": "Juan",
            "apellido": "Lopez"
        }
        """;
    String responseBody = mockMvc.perform(post("/odontologo")
            .contentType(MediaType.APPLICATION_JSON)
            .content(odontologo))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();

    int odontologoId = JsonPath.read(responseBody, "$.id");

    EspecialidadRequestDto especialidadRequestDto = new EspecialidadRequestDto();
    especialidadRequestDto.setTipo("Clinica dermamaxidental");

    String response = mockMvc.perform(post("/especialidad")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(especialidadRequestDto)))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

    EspecialidadResponseDto especialidad = objectMapper.readValue(response, EspecialidadResponseDto.class);
    Integer especialidadId = especialidad.getId();

    mockMvc.perform(put("/odontologo/" + odontologoId + "/especialidad/" + especialidadId))
        .andExpect(status().isOk());

    // Eliminar especialidad
    mockMvc.perform(delete("/odontologo/" + odontologoId + "/especialidad/2"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.especialidadesResponseDto.length()").value(0));
  }
}