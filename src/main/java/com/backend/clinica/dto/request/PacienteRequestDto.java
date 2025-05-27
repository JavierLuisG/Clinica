package com.backend.clinica.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PacienteRequestDto {
  private String nombre;
  private String apellido;
  private String dni;
  private String fechaRegistro;
  private DomicilioRequestDto domicilio;
}
