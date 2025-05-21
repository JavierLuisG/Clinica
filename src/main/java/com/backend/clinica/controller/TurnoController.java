package com.backend.clinica.controller;

import com.backend.clinica.model.Turno;
import com.backend.clinica.service.ITurnoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/turno")
public class TurnoController {
  private final ITurnoService<Integer, Turno> iTurnoService;

  public TurnoController(ITurnoService<Integer, Turno> iTurnoService) {
    this.iTurnoService = iTurnoService;
  }

  @PostMapping
  public ResponseEntity<Turno> postTurno(@RequestBody Turno turno) {
    if (turno == null || turno.getFechaConsulta() == null || turno.getOdontologo().getId() == null || turno.getPaciente().getId() == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    Turno createTurno = iTurnoService.createTurno(turno);
    if (createTurno == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(createTurno);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Turno> getTurnoById(@PathVariable Integer id) {
    if (id == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    Turno turno = iTurnoService.getTurnoById(id);
    if (turno == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(turno);
  }

  @GetMapping
  public ResponseEntity<List<Turno>> getAllTurnos() {
    List<Turno> turnoList = iTurnoService.getAllTurnos();
    if (turnoList.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    return ResponseEntity.ok(turnoList);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Turno> putTurno(@PathVariable Integer id, @RequestBody Turno turno) {
    if (id == null || turno == null || turno.getOdontologo().getId() == null || turno.getPaciente() == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    Turno updateTurno = iTurnoService.updateTurno(id, turno);
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
