package com.backend.clinica.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RegisterRequestDto {
  @NotBlank(message = "El nombre es obligatorio")
  private String firstname;
  @NotBlank(message = "El apellido es obligatorio")
  private String lastname;
  @Email(message = "El formato del email es inválido")
  @NotBlank(message = "El email no puede estar vacío")
  private String email;
  @NotBlank(message = "La contraseña es requerida")
  private String password;
}
