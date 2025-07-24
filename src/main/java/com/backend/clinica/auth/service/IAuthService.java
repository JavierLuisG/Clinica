package com.backend.clinica.auth.service;

import com.backend.clinica.exception.IllegalArgException;

public interface IAuthService<RegisterRequest, LoginRequest, AuthResponse> {
  AuthResponse register(RegisterRequest user);

  AuthResponse login(LoginRequest user) throws IllegalArgException;
}
