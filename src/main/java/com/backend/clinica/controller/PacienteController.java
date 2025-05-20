package com.backend.clinica.controller;

import com.backend.clinica.model.Paciente;
import com.backend.clinica.service.IPacienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paciente")
public class PacienteController {
  private final IPacienteService<String, Paciente> iPacienteService;

  public PacienteController(IPacienteService<String, Paciente> iPacienteService) {
    this.iPacienteService = iPacienteService;
  }

  @PostMapping
  public ResponseEntity<Paciente> postPaciente(@RequestBody Paciente paciente) {
    if (paciente == null || paciente.getDomicilio() == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    Paciente createPaciente = iPacienteService.createPaciente(paciente);
    if (createPaciente == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(createPaciente);
  }

  @GetMapping("/{dni}")
  public ResponseEntity<Paciente> getPaciente(@PathVariable String dni) {
    if (dni == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    Paciente paciente = iPacienteService.getPacienteByDni(dni);
    if (paciente == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(paciente);
  }

  @GetMapping
  public ResponseEntity<List<Paciente>> getAllPacientes() {
    List<Paciente> pacienteList = iPacienteService.getAllPacientes();
    if (pacienteList.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(pacienteList);
  }

  @PutMapping("/{dni}")
  public ResponseEntity<Paciente> putPaciente(@PathVariable String dni, @RequestBody Paciente paciente) {
    if (dni == null || paciente == null || paciente.getDomicilio() == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    Paciente updatePaciente = iPacienteService.updatePaciente(dni, paciente);
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
    } else {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
  }
}
