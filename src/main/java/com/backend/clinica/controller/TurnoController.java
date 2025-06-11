package com.backend.clinica.controller;

import com.backend.clinica.dto.request.TurnoRequestDto;
import com.backend.clinica.dto.response.TurnoResponseDto;
import com.backend.clinica.service.ITurnoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/turno")
public class TurnoController {
  private final ITurnoService<Integer, TurnoRequestDto, TurnoResponseDto> turnoService;

  public TurnoController(ITurnoService<Integer, TurnoRequestDto, TurnoResponseDto> turnoService) {
    this.turnoService = turnoService;
  }

  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

  @PostMapping
  public ResponseEntity<TurnoResponseDto> postTurno(@RequestBody TurnoRequestDto turno) {
    if (turno == null || turno.getFechaConsulta() == null || turno.getOdontologoCodigo() == null || turno.getPacienteDni() == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    TurnoResponseDto createTurno = turnoService.createTurno(turno);
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
    TurnoResponseDto turno = turnoService.getTurnoById(id);
    if (turno == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(turno);
  }

  @GetMapping
  public ResponseEntity<List<TurnoResponseDto>> getAllTurnos() {
    List<TurnoResponseDto> turnoList = turnoService.getAllTurnos();
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
    TurnoResponseDto updateTurno = turnoService.updateTurno(id, turno);
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
    if (!turnoService.deleteTurno(id)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  // ================== HQL methods ==================
  @GetMapping("/fecha")
  public ResponseEntity<List<TurnoResponseDto>> getTurnosEntreFechas(
      @RequestParam("firstDate") String firstDate,
      @RequestParam("endDate") String endDate
  ) {
    if (firstDate == null || endDate == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    LocalDateTime first = LocalDateTime.parse(firstDate, formatter);
    LocalDateTime end = LocalDateTime.parse(endDate, formatter);
    List<TurnoResponseDto> turnoList = turnoService.findByStartDateBetween(first, end);
    if (turnoList.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    return ResponseEntity.ok(turnoList);
  }

  @GetMapping("/odontologo/{codigo}")
  public ResponseEntity<List<TurnoResponseDto>> getTurnosByOdontologoCodigo(@PathVariable String codigo) {
    if (codigo == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    List<TurnoResponseDto> turnoList = turnoService.findByOdontologoCodigo(codigo);
    if (turnoList.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    return ResponseEntity.ok(turnoList);
  }

  @GetMapping("/paciente/{dni}")
  public ResponseEntity<List<TurnoResponseDto>> getTurnosByPacienteDni(@PathVariable String dni) {
    if (dni == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    List<TurnoResponseDto> turnoList = turnoService.findByPacienteDni(dni);
    if (turnoList.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    return ResponseEntity.ok(turnoList);
  }
}
