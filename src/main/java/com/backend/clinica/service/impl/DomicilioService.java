package com.backend.clinica.service.impl;

import com.backend.clinica.dao.IDao;
import com.backend.clinica.model.Domicilio;
import com.backend.clinica.service.IDomicilioService;

import java.util.List;

public class DomicilioService implements IDomicilioService<Integer, Domicilio> {
  private final IDao<Integer, Domicilio> domicilioIDao;

  public DomicilioService(IDao<Integer, Domicilio> domicilioIDao) {
    this.domicilioIDao = domicilioIDao;
  }

  @Override
  public Domicilio createDomicilio(Domicilio domicilio) {
    return domicilioIDao.create(domicilio);
  }

  @Override
  public Domicilio getDomicilioById(Integer id) {
    if (id != null) {
      return domicilioIDao.readOne(id);
    }
    return null;
  }

  @Override
  public List<Domicilio> getAllDomicilios() {
    return domicilioIDao.readAll();
  }

  @Override
  public Domicilio updateDomicilio(Integer id, Domicilio domicilio) {
    if (id != null) {
      return domicilioIDao.update(id, domicilio);
    }
    return null;
  }

  @Override
  public boolean deleteDomicilio(Integer id) {
    if (id != null) {
      return domicilioIDao.delete(id);
    }
    return false;
  }
}
