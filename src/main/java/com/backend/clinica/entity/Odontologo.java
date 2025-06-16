package com.backend.clinica.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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
  @OneToMany(mappedBy = "odontologo", cascade = CascadeType.ALL)
  @JsonIgnore
  private Set<Turno> turnos = new HashSet<>();
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "odontologos_especialidades",
      joinColumns = @JoinColumn(name = "odontologo"),
      inverseJoinColumns = @JoinColumn(name = "especialidad")
  )
  private Set<Especialidad> especialidades = new HashSet<>();
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
