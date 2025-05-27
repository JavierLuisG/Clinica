package com.backend.clinica.service;

import java.util.List;

public interface IOdontologoService<Type, Request, Response> {
  Response createOdontologo(Request t);

  Response getOdontologoByCodigo(Type codigo);

  List<Response> getAllOdontologos();

  Response updateOdontologo(Type codigo, Request t);

  boolean deleteOdontologo(Type codigo);
}
