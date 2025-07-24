package com.backend.clinica.auth.service.impl;

import com.backend.clinica.auth.dto.request.LoginRequestDto;
import com.backend.clinica.auth.dto.request.RegisterRequestDto;
import com.backend.clinica.auth.dto.response.AuthResponseDto;
import com.backend.clinica.auth.service.IAuthService;
import com.backend.clinica.auth.config.JwtService;
import com.backend.clinica.entity.Role;
import com.backend.clinica.entity.User;
import com.backend.clinica.exception.IllegalArgException;
import com.backend.clinica.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService<RegisterRequestDto, LoginRequestDto, AuthResponseDto> {
  private final PasswordEncoder passwordEncoder;
  private final IUserRepository userRepository;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  @Override
  public AuthResponseDto register(RegisterRequestDto requestDto) {
    User created = User.builder()
        .firstname(requestDto.getFirstname())
        .lastname(requestDto.getLastname())
        .email(requestDto.getEmail())
        .password(passwordEncoder.encode(requestDto.getPassword()))
        .role(Role.USER)
        .build();
    userRepository.save(created);
    String jwt = jwtService.generateToken(created);
    return AuthResponseDto.builder().jwt(jwt).build();
  }

  @Override
  public AuthResponseDto login(LoginRequestDto requestDto) throws IllegalArgException {
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword())
    );
    User user = userRepository.findByEmail(requestDto.getEmail())
        .orElseThrow(() -> new IllegalArgException("Usuario no encontrado: " + requestDto.getEmail()));
    String jwt = jwtService.generateToken(user);
    return AuthResponseDto.builder().jwt(jwt).build();
  }
}
