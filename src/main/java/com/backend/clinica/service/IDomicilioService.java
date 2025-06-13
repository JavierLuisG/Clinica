package com.backend.clinica.service;

import com.backend.clinica.exception.IllegalArgException;
import com.backend.clinica.exception.ResourceNotFoundException;

import java.util.List;

public interface IDomicilioService<Type, Request, Response> {
  Response createDomicilio(Request t) throws IllegalArgException;

  Response getDomicilioById(Type id) throws IllegalArgException, ResourceNotFoundException;

  List<Response> getAllDomicilios();

  Response updateDomicilio(Type id, Request t) throws ResourceNotFoundException, IllegalArgException;

  void deleteDomicilio(Type id) throws IllegalArgException, ResourceNotFoundException;
}
