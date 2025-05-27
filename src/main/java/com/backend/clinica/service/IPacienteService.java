package com.backend.clinica.service;

import java.util.List;

public interface IPacienteService<Type, Request, Response> {

  Response createPaciente(Request t);

  Response getPacienteByDni(Type dni);

  List<Response> getAllPacientes();

  Response updatePaciente(Type dni, Request t);

  boolean deletePaciente(Type dni);
}
