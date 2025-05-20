package com.backend.clinica.model;

public class Domicilio {
  private Integer id;
  private String calle;
  private String numero;
  private String localidad;
  private String ciudad;
  private boolean state;

  public Domicilio() {
  }

  public Domicilio(Integer id, String calle, String numero, String localidad, String ciudad) {
    this.id = id;
    this.calle = calle;
    this.numero = numero;
    this.localidad = localidad;
    this.ciudad = ciudad;
    state = true;
  }

  public Domicilio(String calle, String numero, String localidad, String ciudad) {
    this.calle = calle;
    this.numero = numero;
    this.localidad = localidad;
    this.ciudad = ciudad;
    state = true;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCalle() {
    return calle;
  }

  public void setCalle(String calle) {
    this.calle = calle;
  }

  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public String getLocalidad() {
    return localidad;
  }

  public void setLocalidad(String localidad) {
    this.localidad = localidad;
  }

  public String getCiudad() {
    return ciudad;
  }

  public void setCiudad(String ciudad) {
    this.ciudad = ciudad;
  }

  public boolean isState() {
    return state;
  }

  public void setState(boolean state) {
    this.state = state;
  }

  @Override
  public String toString() {
    return "Domicilio id: " + id +
            " - state: " + state +
            " - calle: " + calle +
            " - numero: " + numero +
            " - ciudad: " + ciudad +
            " - localidad: " + localidad;
  }
}
