package com.backend.clinica.controller;

import com.backend.clinica.dto.MessageResponse;
import com.backend.clinica.dto.request.EspecialidadRequestDto;
import com.backend.clinica.dto.response.EspecialidadResponseDto;
import com.backend.clinica.exception.IllegalArgException;
import com.backend.clinica.exception.ResourceNotFoundException;
import com.backend.clinica.service.IEspecialidadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/especialidad")
public class EspecialidadController {
  private final IEspecialidadService<Integer, EspecialidadRequestDto, EspecialidadResponseDto> especialidadService;

  public EspecialidadController(IEspecialidadService<Integer, EspecialidadRequestDto, EspecialidadResponseDto> especialidadService) {
    this.especialidadService = especialidadService;
  }

  @PostMapping
  public ResponseEntity<EspecialidadResponseDto> postEspecialidad(@RequestBody EspecialidadRequestDto especialidad) throws IllegalArgException {
    EspecialidadResponseDto created = especialidadService.createEspecialidad(especialidad);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EspecialidadResponseDto> getEspecialidad(@PathVariable Integer id) throws ResourceNotFoundException, IllegalArgException {
    EspecialidadResponseDto especialidad = especialidadService.getEspecialidadById(id);
    return ResponseEntity.ok(especialidad);
  }

  @GetMapping
  public ResponseEntity<List<EspecialidadResponseDto>> getAllEspecialidades() {
    List<EspecialidadResponseDto> especialidades = especialidadService.getAllEspecialidades();
    return ResponseEntity.ok(especialidades);
  }

  @PutMapping("/{id}")
  public ResponseEntity<EspecialidadResponseDto> putEspecialidad(@PathVariable Integer id, @RequestBody EspecialidadRequestDto especialidad) throws ResourceNotFoundException, IllegalArgException {
    EspecialidadResponseDto updated = especialidadService.updateEspecialidad(id, especialidad);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<MessageResponse> deleteEspecialidad(@PathVariable Integer id) throws ResourceNotFoundException, IllegalArgException {
    especialidadService.deleteEspecialidad(id);
    return ResponseEntity.ok(new MessageResponse("Especialidad " + id + " eliminada"));
  }
}
