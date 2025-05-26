package com.backend.clinica.service;

import java.util.List;

public interface ITurnoService<Type, Request, Response> {
  Response createTurno(Request t);

  Response getTurnoById(Type id);

  List<Response> getAllTurnos();

  Response updateTurno(Type id, Request t);

  boolean deleteTurno(Type id);
}
