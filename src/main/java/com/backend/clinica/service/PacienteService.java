package com.backend.clinica.service;

import com.backend.clinica.dao.IDao;
import com.backend.clinica.model.Domicilio;
import com.backend.clinica.model.Paciente;

import java.util.List;

public class PacienteService {

  private final IDao<String, Paciente> pacienteIDao;
  private final IDao<Integer, Domicilio> domicilioIDao;

  public PacienteService(IDao<String, Paciente> pacienteIDao, IDao<Integer, Domicilio> domicilioIDao) {
    this.pacienteIDao = pacienteIDao;
    this.domicilioIDao = domicilioIDao;
  }

  public Domicilio createDomicilio(Domicilio domicilio) {
    return domicilioIDao.create(domicilio);
  }

  public Domicilio getDomicilio(Integer id) {
    return domicilioIDao.readOne(id);
  }

  public List<Domicilio> getAllDomicilios() {
    return domicilioIDao.readAll();
  }

  public Paciente createPaciente(Paciente paciente) {
    return pacienteIDao.create(paciente);
  }

  public Paciente getPaciente(String dni) {
    return pacienteIDao.readOne(dni);
  }

  public List<Paciente> getAllPacientes() {
    return pacienteIDao.readAll();
  }
}
