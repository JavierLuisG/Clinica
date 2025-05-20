package com.backend.clinica.service.impl;

import java.util.List;

public interface IPacienteService<TypeD, D, TypeP, P> {
  D createDomicilio(D d);

  D getDomicilioById(TypeD id);

  List<D> getAllDomicilios();

  P createPaciente(P p);

  P getPacienteByDni(TypeP dni);

  List<P> getAllPacientes();
}
