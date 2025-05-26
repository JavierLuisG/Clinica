package com.backend.clinica.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DomicilioResponseDto {
  private Integer id;
  private String calle;
  private String numero;
  private String localidad;
  private String ciudad;
}
