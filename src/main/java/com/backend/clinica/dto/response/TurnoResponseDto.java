package com.backend.clinica.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TurnoResponseDto {
  private Integer id;
  private String fechaConsulta;
  private OdontologoResponseDto odontologoResponseDto;
  private PacienteResponseDto pacienteResponseDto;
}
