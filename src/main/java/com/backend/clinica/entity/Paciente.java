package com.backend.clinica.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
  @Column(unique = true)
  private String dni;
  @Column(name = "fecha_registro")
  private LocalDateTime fechaRegistro;
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(
          name = "id_domicilio",
          nullable = false,
          foreignKey = @ForeignKey(name = "fk_paciente_domicilio")
  )
  private Domicilio domicilio;
  @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
  @JsonIgnore
  private Set<Turno> turnos = new HashSet<>();
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
