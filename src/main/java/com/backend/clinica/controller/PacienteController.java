package com.backend.clinica.controller;

import com.backend.clinica.dto.request.PacienteRequestDto;
import com.backend.clinica.dto.response.PacienteResponseDto;
import com.backend.clinica.service.IPacienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paciente")
public class PacienteController {
  private final IPacienteService<String, PacienteRequestDto, PacienteResponseDto> iPacienteService;

  public PacienteController(IPacienteService<String, PacienteRequestDto, PacienteResponseDto> iPacienteService) {
    this.iPacienteService = iPacienteService;
  }

  @PostMapping
  public ResponseEntity<PacienteResponseDto> postPaciente(@RequestBody PacienteRequestDto paciente) {
    if (paciente == null || paciente.getDomicilio() == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    PacienteResponseDto createPaciente = iPacienteService.createPaciente(paciente);
    if (createPaciente == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(createPaciente);
  }

  @GetMapping("/{dni}")
  public ResponseEntity<PacienteResponseDto> getPaciente(@PathVariable String dni) {
    if (dni == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    PacienteResponseDto paciente = iPacienteService.getPacienteByDni(dni);
    if (paciente == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(paciente);
  }

  @GetMapping
  public ResponseEntity<List<PacienteResponseDto>> getAllPacientes() {
    List<PacienteResponseDto> pacienteList = iPacienteService.getAllPacientes();
    if (pacienteList.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(pacienteList);
  }

  @PutMapping("/{dni}")
  public ResponseEntity<PacienteResponseDto> putPaciente(@PathVariable String dni, @RequestBody PacienteRequestDto paciente) {
    if (dni == null || paciente == null || paciente.getDomicilio() == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    PacienteResponseDto updatePaciente = iPacienteService.updatePaciente(dni, paciente);
    if (updatePaciente == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(updatePaciente);
  }

  @DeleteMapping("/{dni}")
  public ResponseEntity<Void> deletePaciente(@PathVariable String dni) {
    if (dni == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    if (!iPacienteService.deletePaciente(dni)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
