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
public class Paciente {
  private Integer id;
  private String nombre;
  private String apellido;
  private String dni;
  private LocalDateTime fechaRegistro;
  private Domicilio domicilio;
  private boolean state = true;

  public Paciente(Integer id, String nombre, String apellido, String dni, LocalDateTime fechaRegistro, Domicilio domicilio) {
    this.id = id;
    this.nombre = nombre;
    this.apellido = apellido;
    this.dni = dni;
    this.fechaRegistro = fechaRegistro;
    this.domicilio = domicilio;
  }

  public Paciente(String nombre, String apellido, String dni, LocalDateTime fechaRegistro, Domicilio domicilio) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.dni = dni;
    this.fechaRegistro = fechaRegistro;
    this.domicilio = domicilio;
  }
}
