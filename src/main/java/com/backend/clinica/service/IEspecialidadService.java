package com.backend.clinica.service;

import com.backend.clinica.exception.IllegalArgException;
import com.backend.clinica.exception.ResourceNotFoundException;

import java.util.List;

public interface IEspecialidadService<Type, Request, Response> {
  Response createEspecialidad(Request especialidad) throws IllegalArgException;

  Response getEspecialidadById(Type id) throws IllegalArgException, ResourceNotFoundException;

  List<Response> getAllEspecialidades();

  Response updateEspecialidad(Type id, Request especialidad) throws IllegalArgException, ResourceNotFoundException;

  void deleteEspecialidad(Type id) throws IllegalArgException, ResourceNotFoundException;
}
