package com.backend.clinica.auth.controller;

import com.backend.clinica.auth.dto.request.LoginRequestDto;
import com.backend.clinica.auth.dto.request.RegisterRequestDto;
import com.backend.clinica.auth.dto.response.AuthResponseDto;
import com.backend.clinica.auth.service.IAuthService;
import com.backend.clinica.exception.IllegalArgException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final IAuthService<RegisterRequestDto, LoginRequestDto, AuthResponseDto> authService;

  @PostMapping("/register")
  public ResponseEntity<AuthResponseDto> register(@RequestBody @Valid RegisterRequestDto user) {
    return ResponseEntity.ok(authService.register(user));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid LoginRequestDto user) throws IllegalArgException {
    return ResponseEntity.ok(authService.login(user));
  }
}
