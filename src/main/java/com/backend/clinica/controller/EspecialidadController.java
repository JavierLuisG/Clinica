package com.backend.clinica.controller;

import com.backend.clinica.dto.request.EspecialidadRequestDto;
import com.backend.clinica.dto.response.EspecialidadResponseDto;
import com.backend.clinica.exception.IllegalArgException;
import com.backend.clinica.service.IEspecialidadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/especialidad")
public class EspecialidadController {
  private final IEspecialidadService<Integer, EspecialidadRequestDto, EspecialidadResponseDto> especialidadService;

  public EspecialidadController(IEspecialidadService<Integer, EspecialidadRequestDto, EspecialidadResponseDto> especialidadService) {
    this.especialidadService = especialidadService;
  }

  @PostMapping
  public ResponseEntity<EspecialidadResponseDto> postEspecialidad(@RequestBody EspecialidadRequestDto especialidad) throws IllegalArgException {
    EspecialidadResponseDto createEspecialidad = especialidadService.createEspecialidad(especialidad);
    return ResponseEntity.status(HttpStatus.CREATED).body(createEspecialidad);
  }
}
