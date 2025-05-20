package com.backend.clinica.service;

import java.util.List;

public interface IPacienteService<TypeP, P> {

  P createPaciente(P p);

  P getPacienteByDni(TypeP dni);

  List<P> getAllPacientes();

  P updatePaciente(TypeP dni, P p);

  boolean deletePaciente(TypeP dni);
}
