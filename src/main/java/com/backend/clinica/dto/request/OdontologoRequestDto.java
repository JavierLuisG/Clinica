package com.backend.clinica.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OdontologoRequestDto {
  private String codigo;
  private String nombre;
  private String apellido;
}
