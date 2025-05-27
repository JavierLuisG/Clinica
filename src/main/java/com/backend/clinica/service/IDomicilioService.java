package com.backend.clinica.service;

import java.util.List;

public interface IDomicilioService<Type, Request, Response> {
  Response createDomicilio(Request d);

  Response getDomicilioById(Type id);

  List<Response> getAllDomicilios();

  Response updateDomicilio(Type id, Request d);

  boolean deleteDomicilio(Type id);
}
