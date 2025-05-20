package com.backend.clinica.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/prueba")
public class ControllerPrueba {
  @GetMapping
  public ResponseEntity<String> getPruebas() {
    return ResponseEntity.ok("Prueba de API Rest con Spring Boot.");
  }
}
