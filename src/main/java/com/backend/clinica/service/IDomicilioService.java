package com.backend.clinica.service;

import java.util.List;

public interface IDomicilioService<TypeD, D> {
  D createDomicilio(D d);

  D getDomicilioById(TypeD id);

  List<D> getAllDomicilios();

  D updateDomicilio(TypeD id, D d);

  boolean deleteDomicilio(TypeD id);
}
