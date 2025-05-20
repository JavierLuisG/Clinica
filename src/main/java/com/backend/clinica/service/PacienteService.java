package com.backend.clinica.service;

import com.backend.clinica.dao.IDao;
import com.backend.clinica.model.Domicilio;
import com.backend.clinica.model.Paciente;
import com.backend.clinica.service.impl.IPacienteService;

import java.util.List;

public class PacienteService implements IPacienteService<Integer, Domicilio, String, Paciente> {

  private final IDao<String, Paciente> pacienteIDao;
  private final IDao<Integer, Domicilio> domicilioIDao;

  public PacienteService(IDao<String, Paciente> pacienteIDao, IDao<Integer, Domicilio> domicilioIDao) {
    this.pacienteIDao = pacienteIDao;
    this.domicilioIDao = domicilioIDao;
  }

  @Override
  public Domicilio createDomicilio(Domicilio domicilio) {
    return domicilioIDao.create(domicilio);
  }

  @Override
  public Domicilio getDomicilioById(Integer id) {
    return domicilioIDao.readOne(id);
  }

  @Override
  public List<Domicilio> getAllDomicilios() {
    return domicilioIDao.readAll();
  }

  @Override
  public Paciente createPaciente(Paciente paciente) {
    return pacienteIDao.create(paciente);
  }

  @Override
  public Paciente getPacienteByDni(String dni) {
    return pacienteIDao.readOne(dni);
  }

  @Override
  public List<Paciente> getAllPacientes() {
    return pacienteIDao.readAll();
  }
}
