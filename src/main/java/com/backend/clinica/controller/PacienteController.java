package com.backend.clinica.controller;

import com.backend.clinica.dto.MessageResponse;
import com.backend.clinica.dto.request.PacienteRequestDto;
import com.backend.clinica.dto.response.PacienteResponseDto;
import com.backend.clinica.exception.IllegalArgException;
import com.backend.clinica.exception.ResourceNotFoundException;
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
  public ResponseEntity<PacienteResponseDto> postPaciente(@RequestBody PacienteRequestDto paciente) throws IllegalArgException {
    PacienteResponseDto createPaciente = iPacienteService.createPaciente(paciente);
    return ResponseEntity.status(HttpStatus.CREATED).body(createPaciente);
  }

  @GetMapping("/{dni}")
  public ResponseEntity<PacienteResponseDto> getPaciente(@PathVariable String dni) throws ResourceNotFoundException, IllegalArgException {
    PacienteResponseDto paciente = iPacienteService.getPacienteByDni(dni);
    return ResponseEntity.ok(paciente);
  }

  @GetMapping
  public ResponseEntity<List<PacienteResponseDto>> getAllPacientes() {
    List<PacienteResponseDto> pacienteList = iPacienteService.getAllPacientes();
    return ResponseEntity.ok(pacienteList);
  }

  @PutMapping("/{dni}")
  public ResponseEntity<PacienteResponseDto> putPaciente(@PathVariable String dni, @RequestBody PacienteRequestDto paciente) throws ResourceNotFoundException, IllegalArgException {
    PacienteResponseDto updatePaciente = iPacienteService.updatePaciente(dni, paciente);
    return ResponseEntity.ok(updatePaciente);
  }

  @DeleteMapping("/{dni}")
  public ResponseEntity<MessageResponse> deletePaciente(@PathVariable String dni) throws IllegalArgException, ResourceNotFoundException {
    iPacienteService.deletePaciente(dni);
    return ResponseEntity.ok(new MessageResponse("Paciente " + dni + " eliminado"));
  }
}
