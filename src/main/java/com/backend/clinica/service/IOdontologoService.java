package com.backend.clinica.service;

import java.util.List;

public interface IOdontologoService<TypeO, O> {
  O createOdontologo(O o);

  O getOdontologoByCodigo(TypeO codigo);

  List<O> getAllOdontologos();

  O updateOdontologo(TypeO codigo, O o);

  boolean deleteOdontologo(TypeO codigo);
}
