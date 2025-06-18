package com.backend.clinica.service;

import com.backend.clinica.exception.IllegalArgException;
import com.backend.clinica.exception.ResourceNotFoundException;

import java.util.List;

public interface IOdontologoService<Type, Request, Response> {
  Response createOdontologo(Request t) throws IllegalArgException;

  Response getOdontologoByCodigo(Type codigo) throws ResourceNotFoundException, IllegalArgException;

  List<Response> getAllOdontologos();

  Response updateOdontologo(Type codigo, Request t) throws IllegalArgException, ResourceNotFoundException;

  void deleteOdontologo(Type codigo) throws IllegalArgException, ResourceNotFoundException;

  Response assignEspecialidad(Integer id_odontologo, Integer id_especialidad) throws IllegalArgException, ResourceNotFoundException;

  Response removeEspecialidad(Integer id_odontologo, Integer id_especialidad) throws IllegalArgException, ResourceNotFoundException;
}
