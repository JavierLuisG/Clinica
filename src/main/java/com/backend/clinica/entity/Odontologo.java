package com.backend.clinica.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "odontologos")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Odontologo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(unique = true)
  private String codigo;
  private String nombre;
  private String apellido;
  private boolean state = true;

  public Odontologo(Integer id, String codigo, String nombre, String apellido) {
    this.id = id;
    this.codigo = codigo;
    this.nombre = nombre;
    this.apellido = apellido;
  }

  public Odontologo(String codigo, String nombre, String apellido) {
    this.codigo = codigo;
    this.nombre = nombre;
    this.apellido = apellido;
  }
}
