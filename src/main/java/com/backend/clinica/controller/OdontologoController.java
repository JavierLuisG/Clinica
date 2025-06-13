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
  private final IOdontologoService<String, OdontologoRequestDto, OdontologoResponseDto> iOdontologoService;

  public OdontologoController(IOdontologoService<String, OdontologoRequestDto, OdontologoResponseDto> iOdontologoService) {
    this.iOdontologoService = iOdontologoService;
  }

  @PostMapping
  public ResponseEntity<OdontologoResponseDto> postOdontologo(@RequestBody OdontologoRequestDto odontologo) throws IllegalArgException {
    OdontologoResponseDto createOdontologo = iOdontologoService.createOdontologo(odontologo);
    return ResponseEntity.status(HttpStatus.CREATED).body(createOdontologo);
  }

  @GetMapping("/{codigo}")
  public ResponseEntity<OdontologoResponseDto> getOdontologoByCodigo(@PathVariable String codigo) throws ResourceNotFoundException, IllegalArgException {
    OdontologoResponseDto odontologo = iOdontologoService.getOdontologoByCodigo(codigo);
    return ResponseEntity.ok(odontologo);
  }

  @GetMapping
  public ResponseEntity<List<OdontologoResponseDto>> getAllOdontologos() {
    List<OdontologoResponseDto> odontologoList = iOdontologoService.getAllOdontologos();
    return ResponseEntity.ok(odontologoList);
  }

  @PutMapping("/{codigo}")
  public ResponseEntity<OdontologoResponseDto> putOdontologo(@PathVariable String codigo, @RequestBody OdontologoRequestDto odontologo) throws ResourceNotFoundException, IllegalArgException {
    OdontologoResponseDto updateOdontologo = iOdontologoService.updateOdontologo(codigo, odontologo);
    return ResponseEntity.ok(updateOdontologo);
  }

  @DeleteMapping("/{codigo}")
  public ResponseEntity<MessageResponse> deleteOdontologo(@PathVariable String codigo) throws ResourceNotFoundException, IllegalArgException {
    iOdontologoService.deleteOdontologo(codigo);
    return ResponseEntity.ok(new MessageResponse("Odontólogo " + codigo + " eliminado"));
  }
}
