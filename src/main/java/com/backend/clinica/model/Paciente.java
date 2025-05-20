package com.backend.clinica.model;

import java.time.LocalDate;

public class Paciente {
  private Integer id;
  private String nombre;
  private String apellido;
  private String dni;
  private LocalDate fechaRegistro;
  private Domicilio domicilio;
  private boolean state;

  public Paciente() {
  }

  public Paciente(Integer id, String nombre, String apellido, String dni, LocalDate fechaRegistro, Domicilio domicilio) {
    this.id = id;
    this.nombre = nombre;
    this.apellido = apellido;
    this.dni = dni;
    this.fechaRegistro = fechaRegistro;
    this.domicilio = domicilio;
    state = true;
  }

  public Paciente(String nombre, String apellido, String dni, LocalDate fechaRegistro, Domicilio domicilio) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.dni = dni;
    this.fechaRegistro = fechaRegistro;
    this.domicilio = domicilio;
    state = true;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getApellido() {
    return apellido;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  public String getDni() {
    return dni;
  }

  public void setDni(String dni) {
    this.dni = dni;
  }

  public Domicilio getDomicilio() {
    return domicilio;
  }

  public void setDomicilio(Domicilio domicilio) {
    this.domicilio = domicilio;
  }

  public LocalDate getFechaRegistro() {
    return fechaRegistro;
  }

  public void setFechaRegistro(LocalDate fechaRegistro) {
    this.fechaRegistro = fechaRegistro;
  }

  public boolean isState() {
    return state;
  }

  public void setState(boolean state) {
    this.state = state;
  }

  @Override
  public String toString() {
    return "Paciente id: " + id +
            " - state: " + state +
            " - nombre: " + nombre +
            " - apellido: " + apellido +
            " - dni: " + dni +
            " - fecha registro: " + fechaRegistro +
            ", " + domicilio;
  }
}
