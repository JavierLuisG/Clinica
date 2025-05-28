package com.backend.clinica.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "domicilios")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Domicilio {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String calle;
  private String numero;
  private String localidad;
  private String ciudad;
  private boolean state = true;

  public Domicilio(Integer id, String calle, String numero, String localidad, String ciudad) {
    this.id = id;
    this.calle = calle;
    this.numero = numero;
    this.localidad = localidad;
    this.ciudad = ciudad;
  }

  public Domicilio(String calle, String numero, String localidad, String ciudad) {
    this.calle = calle;
    this.numero = numero;
    this.localidad = localidad;
    this.ciudad = ciudad;
  }
}
