package com.backend.clinica.controller;

import com.backend.clinica.dto.MessageResponse;
import com.backend.clinica.dto.request.OdontologoRequestDto;
import com.backend.clinica.dto.response.OdontologoResponseDto;
import com.backend.clinica.exception.IllegalArgException;
import com.backend.clinica.exception.ResourceNotFoundException;
import com.backend.clinica.service.IOdontologoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/odontologo")
public class OdontologoController {
  private final IOdontologoService<String, OdontologoRequestDto, OdontologoResponseDto> odontologoService;

  public OdontologoController(IOdontologoService<String, OdontologoRequestDto, OdontologoResponseDto> odontologoService) {
    this.odontologoService = odontologoService;
  }

  @PostMapping
  public ResponseEntity<OdontologoResponseDto> postOdontologo(@RequestBody OdontologoRequestDto odontologo) throws IllegalArgException {
    OdontologoResponseDto createOdontologo = odontologoService.createOdontologo(odontologo);
    return ResponseEntity.status(HttpStatus.CREATED).body(createOdontologo);
  }

  @GetMapping("/{codigo}")
  public ResponseEntity<OdontologoResponseDto> getOdontologoByCodigo(@PathVariable String codigo) throws ResourceNotFoundException, IllegalArgException {
    OdontologoResponseDto odontologo = odontologoService.getOdontologoByCodigo(codigo);
    return ResponseEntity.ok(odontologo);
  }

  @GetMapping
  public ResponseEntity<List<OdontologoResponseDto>> getAllOdontologos() {
    List<OdontologoResponseDto> odontologoList = odontologoService.getAllOdontologos();
    return ResponseEntity.ok(odontologoList);
  }

  @PutMapping("/{codigo}")
  public ResponseEntity<OdontologoResponseDto> putOdontologo(@PathVariable String codigo, @RequestBody OdontologoRequestDto odontologo) throws ResourceNotFoundException, IllegalArgException {
    OdontologoResponseDto updateOdontologo = odontologoService.updateOdontologo(codigo, odontologo);
    return ResponseEntity.ok(updateOdontologo);
  }

  @DeleteMapping("/{codigo}")
  public ResponseEntity<MessageResponse> deleteOdontologo(@PathVariable String codigo) throws ResourceNotFoundException, IllegalArgException {
    odontologoService.deleteOdontologo(codigo);
    return ResponseEntity.ok(new MessageResponse("Odontólogo " + codigo + " eliminado"));
  }

  @PutMapping("/{id_odontologo}/especialidad/{id_especialidad}")
  public ResponseEntity<OdontologoResponseDto> assignEspecialidad(@PathVariable Integer id_odontologo, @PathVariable Integer id_especialidad) throws ResourceNotFoundException, IllegalArgException {
    OdontologoResponseDto odontologo = odontologoService.assignEspecialidad(id_odontologo, id_especialidad);
    return ResponseEntity.ok(odontologo);
  }

  @DeleteMapping("/{id_odontologo}/especialidad/{id_especialidad}")
  public ResponseEntity<OdontologoResponseDto> removeEspecialidad(@PathVariable Integer id_odontologo, @PathVariable Integer id_especialidad) throws ResourceNotFoundException, IllegalArgException {
    OdontologoResponseDto odontologo = odontologoService.removeEspecialidad(id_odontologo, id_especialidad);
    return ResponseEntity.ok(odontologo);
  }
}
