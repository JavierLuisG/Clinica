package com.backend.clinica.controller;

import com.backend.clinica.dto.request.OdontologoRequestDto;
import com.backend.clinica.dto.response.OdontologoResponseDto;
import com.backend.clinica.service.IOdontologoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/odontologo")
public class OdontologoController {
  private final IOdontologoService<String, OdontologoRequestDto, OdontologoResponseDto> iOdontologoService;

  public OdontologoController(IOdontologoService<String, OdontologoRequestDto, OdontologoResponseDto> iOdontologoService) {
    this.iOdontologoService = iOdontologoService;
  }

  @PostMapping
  public ResponseEntity<OdontologoResponseDto> postOdontologo(@RequestBody OdontologoRequestDto odontologo) {
    if (odontologo == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    OdontologoResponseDto createOdontologo = iOdontologoService.createOdontologo(odontologo);
    if (createOdontologo == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(createOdontologo);
  }

  @GetMapping("/{codigo}")
  public ResponseEntity<OdontologoResponseDto> getOdontologoByCodigo(@PathVariable String codigo) {
    if (codigo == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    OdontologoResponseDto odontologo = iOdontologoService.getOdontologoByCodigo(codigo);
    if (odontologo == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(odontologo);
  }

  @GetMapping
  public ResponseEntity<List<OdontologoResponseDto>> getAllOdontologos() {
    List<OdontologoResponseDto> odontologoList = iOdontologoService.getAllOdontologos();
    if (odontologoList.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    return ResponseEntity.ok(odontologoList);
  }

  @PutMapping("/{codigo}")
  public ResponseEntity<OdontologoResponseDto> putOdontologo(@PathVariable String codigo, @RequestBody OdontologoRequestDto odontologo) {
    if (codigo == null || codigo.isEmpty() || odontologo == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    OdontologoResponseDto updateOdontologo = iOdontologoService.updateOdontologo(codigo, odontologo);
    if (updateOdontologo == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(updateOdontologo);
  }

  @DeleteMapping("/{codigo}")
  public ResponseEntity<Void> deleteOdontologo(@PathVariable String codigo) {
    if (codigo == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    if (!iOdontologoService.deleteOdontologo(codigo)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
