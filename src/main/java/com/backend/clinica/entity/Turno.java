package com.backend.clinica.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Turno {
  private Integer id;
  private LocalDateTime fechaConsulta;
  private Odontologo odontologo;
  private Paciente paciente;
  private boolean state = true;

  public Turno(Integer id, LocalDateTime fechaConsulta, Odontologo odontologo, Paciente paciente) {
    this.id = id;
    this.fechaConsulta = fechaConsulta;
    this.odontologo = odontologo;
    this.paciente = paciente;
  }

  public Turno(LocalDateTime fechaConsulta, Odontologo odontologo, Paciente paciente) {
    this.fechaConsulta = fechaConsulta;
    this.odontologo = odontologo;
    this.paciente = paciente;
  }
}
