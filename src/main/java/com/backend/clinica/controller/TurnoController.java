package com.backend.clinica.controller;

import com.backend.clinica.dto.MessageResponse;
import com.backend.clinica.dto.request.TurnoRequestDto;
import com.backend.clinica.dto.response.TurnoResponseDto;
import com.backend.clinica.exception.IllegalArgException;
import com.backend.clinica.exception.ResourceNotFoundException;
import com.backend.clinica.service.ITurnoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/turno")
public class TurnoController {
  private final ITurnoService<Integer, TurnoRequestDto, TurnoResponseDto> turnoService;

  public TurnoController(ITurnoService<Integer, TurnoRequestDto, TurnoResponseDto> turnoService) {
    this.turnoService = turnoService;
  }

  @PostMapping
  public ResponseEntity<TurnoResponseDto> postTurno(@RequestBody TurnoRequestDto turno) throws ResourceNotFoundException, IllegalArgException {
    TurnoResponseDto createTurno = turnoService.createTurno(turno);
    return ResponseEntity.status(HttpStatus.CREATED).body(createTurno);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TurnoResponseDto> getTurnoById(@PathVariable Integer id) throws ResourceNotFoundException, IllegalArgException {
    TurnoResponseDto turno = turnoService.getTurnoById(id);
    return ResponseEntity.ok(turno);
  }

  @GetMapping
  public ResponseEntity<List<TurnoResponseDto>> getAllTurnos() {
    List<TurnoResponseDto> turnoList = turnoService.getAllTurnos();
    return ResponseEntity.ok(turnoList);
  }

  @PutMapping("/{id}")
  public ResponseEntity<TurnoResponseDto> putTurno(@PathVariable Integer id, @RequestBody TurnoRequestDto turno) throws IllegalArgException, ResourceNotFoundException {
    TurnoResponseDto updateTurno = turnoService.updateTurno(id, turno);
    return ResponseEntity.ok(updateTurno);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<MessageResponse> deleteTurno(@PathVariable Integer id) throws ResourceNotFoundException, IllegalArgException {
    turnoService.deleteTurno(id);
    return ResponseEntity.ok(new MessageResponse("Turno " + id + " eliminado"));
  }

  // ================== HQL methods ==================
  @GetMapping("/fecha")
  public ResponseEntity<List<TurnoResponseDto>> getTurnosEntreFechas(
      @RequestParam("firstDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime firstDate,
      @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime endDate
  ) throws IllegalArgException {
    List<TurnoResponseDto> turnoList = turnoService.findByStartDateBetween(firstDate, endDate);
    return ResponseEntity.ok(turnoList);
  }

  @GetMapping("/odontologo/{codigo}")
  public ResponseEntity<List<TurnoResponseDto>> getTurnosByOdontologoCodigo(@PathVariable String codigo) throws IllegalArgException {
    List<TurnoResponseDto> turnoList = turnoService.findByOdontologoCodigo(codigo);
    return ResponseEntity.ok(turnoList);
  }

  @GetMapping("/paciente/{dni}")
  public ResponseEntity<List<TurnoResponseDto>> getTurnosByPacienteDni(@PathVariable String dni) throws IllegalArgException {
    List<TurnoResponseDto> turnoList = turnoService.findByPacienteDni(dni);
    return ResponseEntity.ok(turnoList);
  }
}
