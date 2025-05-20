package com.backend.clinica.controller;

import com.backend.clinica.model.Odontologo;
import com.backend.clinica.service.IOdontologoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/odontologo")
public class OdontologoController {
  private final IOdontologoService<String, Odontologo> iOdontologoService;

  public OdontologoController(IOdontologoService<String, Odontologo> iOdontologoService) {
    this.iOdontologoService = iOdontologoService;
  }

  @PostMapping
  public ResponseEntity<Odontologo> postOdontologo(@RequestBody Odontologo odontologo) {
    if (odontologo == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    Odontologo createOdontologo = iOdontologoService.createOdontologo(odontologo);
    if (createOdontologo == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(createOdontologo);
  }

  @GetMapping("/{codigo}")
  public ResponseEntity<Odontologo> getOdontologoByCodigo(@PathVariable String codigo) {
    if (codigo == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    Odontologo odontologo = iOdontologoService.getOdontologoByCodigo(codigo);
    if (odontologo == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(odontologo);
  }

  @GetMapping
  public ResponseEntity<List<Odontologo>> getAllOdontologos() {
    List<Odontologo> odontologoList = iOdontologoService.getAllOdontologos();
    if (odontologoList.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    return ResponseEntity.ok(odontologoList);
  }

  @PutMapping("/{codigo}")
  public ResponseEntity<Odontologo> putOdontologo(@PathVariable String codigo, @RequestBody Odontologo odontologo) {
    if (codigo == null || codigo.isEmpty() || odontologo == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    Odontologo updateOdontologo = iOdontologoService.updateOdontologo(codigo, odontologo);
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
