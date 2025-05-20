package com.backend.clinica.service.impl;

import com.backend.clinica.dao.IDao;
import com.backend.clinica.model.Odontologo;
import com.backend.clinica.service.IOdontologoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OdontologoService implements IOdontologoService<String, Odontologo> {
  private final IDao<String, Odontologo> odontologoIDao;

  public OdontologoService(IDao<String, Odontologo> odontologoIDao) {
    this.odontologoIDao = odontologoIDao;
  }

  @Override
  public Odontologo createOdontologo(Odontologo odontologo) {
    return odontologoIDao.create(odontologo);
  }

  @Override
  public Odontologo getOdontologoByRef(String codigo) {
    if (codigo != null) {
      return odontologoIDao.readOne(codigo);
    }
    return null;
  }

  @Override
  public List<Odontologo> getAllOdontologos() {
    return odontologoIDao.readAll();
  }

  @Override
  public Odontologo updateOdontologo(String codigo, Odontologo odontologo) {
    if (codigo != null) {
      return odontologoIDao.update(codigo, odontologo);
    }
    return null;
  }

  @Override
  public boolean deleteOdontologo(String codigo) {
    if (codigo != null) {
      return odontologoIDao.delete(codigo);
    }
    return false;
  }
}
