package com.backend.clinica.service;

import java.util.List;

public interface IDomicilioService<Type, Request, Response> {
  Response createDomicilio(Request t);

  Response getDomicilioById(Type id);

  List<Response> getAllDomicilios();

  Response updateDomicilio(Type id, Request t);

  boolean deleteDomicilio(Type id);
}
