package com.backend.clinica.service.impl;

import com.backend.clinica.dao.IDao;
import com.backend.clinica.model.Turno;
import com.backend.clinica.service.ITurnoService;

import java.util.List;

public class TurnoService implements ITurnoService<Integer, Turno> {
  private final IDao<Integer, Turno> turnoIDao;

  public TurnoService(IDao<Integer, Turno> turnoIDao) {
    this.turnoIDao = turnoIDao;
  }

  @Override
  public Turno createTurno(Turno turno) {
    if (turno == null || turno.getOdontologo().getId() == null || turno.getPaciente().getId() == null) {
      return null;
    }
    return turnoIDao.create(turno);
  }

  @Override
  public Turno getTurnoById(Integer id) {
    if (id == null) {
      return null;
    }
    return turnoIDao.readOne(id);
  }

  @Override
  public List<Turno> getAllTurnos() {
    return turnoIDao.readAll();
  }

  @Override
  public Turno updateTurno(Integer id, Turno turno) {
    if (id == null || turno == null || turno.getOdontologo() == null || turno.getPaciente() == null) {
      return null;
    }
    return turnoIDao.update(id, turno);
  }

  @Override
  public boolean deleteTurno(Integer id) {
    if (id == null) {
      return false;
    }
    return turnoIDao.delete(id);
  }
}
