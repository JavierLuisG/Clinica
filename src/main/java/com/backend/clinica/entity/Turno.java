package com.backend.clinica.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Turno {
  private Integer id;
  private LocalDate fechaConsulta;
  private Odontologo odontologo;
  private Paciente paciente;
  private boolean state = true;

  public Turno(Integer id, LocalDate fechaConsulta, Odontologo odontologo, Paciente paciente) {
    this.id = id;
    this.fechaConsulta = fechaConsulta;
    this.odontologo = odontologo;
    this.paciente = paciente;
  }

  public Turno(LocalDate fechaConsulta, Odontologo odontologo, Paciente paciente) {
    this.fechaConsulta = fechaConsulta;
    this.odontologo = odontologo;
    this.paciente = paciente;
  }
}
