package com.backend.clinica.service;

import java.util.List;

public interface IPacienteService<Type, Request, Response> {

  Response createPaciente(Request p);

  Response getPacienteByDni(Type dni);

  List<Response> getAllPacientes();

  Response updatePaciente(Type dni, Request p);

  boolean deletePaciente(Type dni);
}
