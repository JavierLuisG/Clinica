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
}
