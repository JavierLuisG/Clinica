package com.backend.clinica.service;

import java.util.List;

public interface ITurnoService<TypeT, T> {
  T createTurno(T t);

  T getTurnoById(TypeT id);

  List<T> getAllTurnos();

  T updateTurno(TypeT id, T t);

  boolean deleteTurno(TypeT id);
}
