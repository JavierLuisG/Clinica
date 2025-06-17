package com.backend.clinica.dto.response;

import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OdontologoResponseDto {
  private Integer id;
  private String codigo;
  private String nombre;
  private String apellido;
  private Set<EspecialidadResponseDto> especialidadesResponseDto;
}
