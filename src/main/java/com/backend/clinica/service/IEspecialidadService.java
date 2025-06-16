package com.backend.clinica.service;

import com.backend.clinica.exception.IllegalArgException;

import java.util.List;

public interface IEspecialidadService<Type, Request, Response> {
  Response createEspecialidad(Request especialidad) throws IllegalArgException;

  Response getEspecialidadById(Type id);

  List<Response> getAllEspecialidades();

  Response updateEspecialidad(Type id, Request especialidad);

  void deleteEspecialidad(Type id);
}
