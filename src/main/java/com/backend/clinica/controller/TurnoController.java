package com.backend.clinica.controller;

import com.backend.clinica.dto.request.TurnoRequestDto;
import com.backend.clinica.dto.response.TurnoResponseDto;
import com.backend.clinica.service.ITurnoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/turno")
public class TurnoController {
  private final ITurnoService<Integer, TurnoRequestDto, TurnoResponseDto> iTurnoService;

  public TurnoController(ITurnoService<Integer, TurnoRequestDto, TurnoResponseDto> iTurnoService) {
    this.iTurnoService = iTurnoService;
  }

  @PostMapping
  public ResponseEntity<TurnoResponseDto> postTurno(@RequestBody TurnoRequestDto turno) {
    if (turno == null || turno.getFechaConsulta() == null || turno.getOdontologoCodigo() == null || turno.getPacienteDni() == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    TurnoResponseDto createTurno = iTurnoService.createTurno(turno);
    if (createTurno == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(createTurno);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TurnoResponseDto> getTurnoById(@PathVariable Integer id) {
    if (id == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    TurnoResponseDto turno = iTurnoService.getTurnoById(id);
    if (turno == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(turno);
  }

  @GetMapping
  public ResponseEntity<List<TurnoResponseDto>> getAllTurnos() {
    List<TurnoResponseDto> turnoList = iTurnoService.getAllTurnos();
    if (turnoList.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    return ResponseEntity.ok(turnoList);
  }

  @PutMapping("/{id}")
  public ResponseEntity<TurnoResponseDto> putTurno(@PathVariable Integer id, @RequestBody TurnoRequestDto turno) {
    if (id == null || turno == null || turno.getOdontologoCodigo() == null || turno.getPacienteDni() == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    TurnoResponseDto updateTurno = iTurnoService.updateTurno(id, turno);
    if (updateTurno == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(updateTurno);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTurno(@PathVariable Integer id) {
    if (id == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    if (!iTurnoService.deleteTurno(id)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
