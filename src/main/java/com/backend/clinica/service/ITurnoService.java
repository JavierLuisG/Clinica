package com.backend.clinica.service;

import com.backend.clinica.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface ITurnoService<Type, Request, Response> {
  Response createTurno(Request t);

  Response getTurnoById(Type id);

  List<Response> getAllTurnos();

  Response updateTurno(Type id, Request t);

  void deleteTurno(Type id) throws ResourceNotFoundException;

  // ================== HQL methods ==================
  List<Response> findByStartDateBetween(LocalDateTime firstDate, LocalDateTime endDate);

  List<Response> findByOdontologoCodigo(String codigo);

  List<Response> findByPacienteDni(String dni);
}
