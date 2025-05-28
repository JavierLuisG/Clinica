package com.backend.clinica.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "pacientes")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Paciente {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String nombre;
  private String apellido;
  private String dni;
  @Column(name = "fecha_registro")
  private LocalDateTime fechaRegistro;
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "id_domicilio", nullable = false)
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
