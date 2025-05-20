package com.backend.clinica.model;

public class Odontologo {
  private Integer id;
  private String codigo;
  private String nombre;
  private String apellido;
  private boolean state;

  public Odontologo() {
  }

  public Odontologo(Integer id, String codigo, String nombre, String apellido) {
    this.id = id;
    this.codigo = codigo;
    this.nombre = nombre;
    this.apellido = apellido;
    state = true;
  }

  public Odontologo(String codigo, String nombre, String apellido) {
    this.codigo = codigo;
    this.nombre = nombre;
    this.apellido = apellido;
    state = true;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
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

  public boolean isState() {
    return state;
  }

  public void setState(boolean state) {
    this.state = state;
  }

  @Override
  public String toString() {
    return "Odontólogo id: " + id +
            " - state: " + state +
            " - código: " + codigo +
            " - nombre: " + nombre +
            " - apellido: " + apellido;
  }
}
