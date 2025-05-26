package com.backend.clinica.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TurnoRequestDto {
  private String fechaConsulta;
  private String odontologoCodigo;
  private String pacienteDni;
}
