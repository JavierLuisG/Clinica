package com.backend.clinica.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DomicilioRequestDto {
  private String calle;
  private String numero;
  private String localidad;
  private String ciudad;
}
