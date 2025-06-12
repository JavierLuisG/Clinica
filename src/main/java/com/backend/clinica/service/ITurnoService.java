package com.backend.clinica.service;

import com.backend.clinica.exception.IllegalArgException;
import com.backend.clinica.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface ITurnoService<Type, Request, Response> {
  Response createTurno(Request t) throws ResourceNotFoundException, IllegalArgException;

  Response getTurnoById(Type id) throws ResourceNotFoundException, IllegalArgException;

  List<Response> getAllTurnos();

  Response updateTurno(Type id, Request t) throws IllegalArgException, ResourceNotFoundException;

  void deleteTurno(Type id) throws ResourceNotFoundException, IllegalArgException;

  // ================== HQL methods ==================
  List<Response> findByStartDateBetween(LocalDateTime firstDate, LocalDateTime endDate) throws IllegalArgException;

  List<Response> findByOdontologoCodigo(String codigo) throws IllegalArgException;

  List<Response> findByPacienteDni(String dni) throws IllegalArgException;
}
