package com.backend.clinica.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "turnos")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Turno {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(name = "fecha_consulta")
  private LocalDateTime fechaConsulta;
  @ManyToOne
  @JoinColumn(name = "id_odontologo", nullable = false)
  private Odontologo odontologo;
  @ManyToOne
  @JoinColumn(name = "id_paciente", nullable = false)
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
