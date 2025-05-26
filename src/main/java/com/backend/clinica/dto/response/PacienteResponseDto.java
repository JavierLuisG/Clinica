package com.backend.clinica.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PacienteResponseDto {
  private Integer id;
  private String nombre;
  private String apellido;
  private String dni;
  private DomicilioResponseDto domicilioResponseDto;
}
