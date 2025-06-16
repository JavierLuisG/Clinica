package com.backend.clinica.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "especialidad")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Especialidad {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(unique = true)
  private String tipo;
  @ManyToMany(mappedBy = "especialidades", fetch = FetchType.LAZY)
  @JsonIgnore
  private Set<Odontologo> odontologos = new HashSet<>();
}
