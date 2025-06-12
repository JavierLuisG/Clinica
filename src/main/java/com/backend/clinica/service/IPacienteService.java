package com.backend.clinica.service;

import com.backend.clinica.exception.IllegalArgException;
import com.backend.clinica.exception.ResourceNotFoundException;

import java.util.List;

public interface IPacienteService<Type, Request, Response> {

  Response createPaciente(Request t) throws IllegalArgException;

  Response getPacienteByDni(Type dni) throws IllegalArgException, ResourceNotFoundException;

  List<Response> getAllPacientes();

  Response updatePaciente(Type dni, Request t) throws ResourceNotFoundException, IllegalArgException;

  void deletePaciente(Type dni) throws IllegalArgException, ResourceNotFoundException;
}
