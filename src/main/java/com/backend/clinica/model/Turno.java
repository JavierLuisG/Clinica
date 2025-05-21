package com.backend.clinica.model;

import java.time.LocalDate;

public class Turno {
  private Integer id;
  private LocalDate fechaConsulta;
  private Odontologo odontologo;
  private Paciente paciente;
  private boolean state;

  public Turno() {
  }

  public Turno(Integer id, LocalDate fechaConsulta, Odontologo odontologo, Paciente paciente) {
    this.id = id;
    this.fechaConsulta = fechaConsulta;
    this.odontologo = odontologo;
    this.paciente = paciente;
    state = true;
  }

  public Turno(LocalDate fechaConsulta, Odontologo odontologo, Paciente paciente) {
    this.fechaConsulta = fechaConsulta;
    this.odontologo = odontologo;
    this.paciente = paciente;
    state = true;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public LocalDate getFechaConsulta() {
    return fechaConsulta;
  }

  public void setFechaConsulta(LocalDate fechaConsulta) {
    this.fechaConsulta = fechaConsulta;
  }

  public Odontologo getOdontologo() {
    return odontologo;
  }

  public void setOdontologo(Odontologo odontologo) {
    this.odontologo = odontologo;
  }

  public Paciente getPaciente() {
    return paciente;
  }

  public void setPaciente(Paciente paciente) {
    this.paciente = paciente;
  }

  public boolean isState() {
    return state;
  }

  public void setState(boolean state) {
    this.state = state;
  }

  @Override
  public String toString() {
    return "Turno id: " + id +
            " - state: " + state +
            " - fechaConsulta: " + fechaConsulta +
            ", " + odontologo +
            ", " + paciente;
  }
}
