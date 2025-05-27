package com.backend.clinica.service.impl;

import com.backend.clinica.dao.IDao;
import com.backend.clinica.dto.request.DomicilioRequestDto;
import com.backend.clinica.dto.response.DomicilioResponseDto;
import com.backend.clinica.model.Domicilio;
import com.backend.clinica.service.IDomicilioService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DomicilioService implements IDomicilioService<Integer, DomicilioRequestDto, DomicilioResponseDto> {
  private final IDao<Integer, Domicilio> domicilioIDao;

  public DomicilioService(IDao<Integer, Domicilio> domicilioIDao) {
    this.domicilioIDao = domicilioIDao;
  }

  @Override
  public DomicilioResponseDto createDomicilio(DomicilioRequestDto domicilio) {
    if (domicilio == null) {
      return null;
    }
    Domicilio created = new Domicilio(
            domicilio.getCalle(),
            domicilio.getNumero(),
            domicilio.getLocalidad(),
            domicilio.getCiudad());
    Domicilio saved = domicilioIDao.create(created);
    if (saved == null) {
      return null;
    }
    DomicilioResponseDto domicilioResponseDto = new DomicilioResponseDto(
            saved.getId(),
            saved.getCalle(),
            saved.getNumero(),
            saved.getLocalidad(),
            saved.getCiudad()
    );
    return domicilioResponseDto;
  }

  @Override
  public DomicilioResponseDto getDomicilioById(Integer id) {
    if (id == null) {
      return null;
    }
    Domicilio domicilio = domicilioIDao.readOne(id);
    if (domicilio == null) {
      return null;
    }
    DomicilioResponseDto domicilioResponseDto = new DomicilioResponseDto(
            domicilio.getId(),
            domicilio.getCalle(),
            domicilio.getNumero(),
            domicilio.getLocalidad(),
            domicilio.getCiudad()
    );
    return domicilioResponseDto;
  }

  @Override
  public List<DomicilioResponseDto> getAllDomicilios() {
    List<DomicilioResponseDto> domicilioResponseDtoList = new ArrayList<>();
    List<Domicilio> domicilioList = domicilioIDao.readAll();
    if (domicilioList.isEmpty()) {
      return null;
    }
    for (Domicilio domicilio : domicilioList) {
      domicilioResponseDtoList.add(new DomicilioResponseDto(
              domicilio.getId(),
              domicilio.getCalle(),
              domicilio.getNumero(),
              domicilio.getLocalidad(),
              domicilio.getCiudad()));
    }
    return domicilioResponseDtoList;
  }

  @Override
  public DomicilioResponseDto updateDomicilio(Integer id, DomicilioRequestDto domicilio) {
    if (id == null || domicilio == null) {
      return null;
    }
    Domicilio getDomicilio = domicilioIDao.readOne(id);
    if (getDomicilio == null) {
      return null;
    }
    Domicilio created = new Domicilio(
            getDomicilio.getId(),
            domicilio.getCalle(),
            domicilio.getNumero(),
            domicilio.getLocalidad(),
            domicilio.getCiudad());
    Domicilio updated = domicilioIDao.update(id, created);
    DomicilioResponseDto domicilioResponseDto = new DomicilioResponseDto(
            updated.getId(),
            updated.getCalle(),
            updated.getNumero(),
            updated.getLocalidad(),
            updated.getCiudad());
    return domicilioResponseDto;
  }

  @Override
  public boolean deleteDomicilio(Integer id) {
    if (id == null) {
      return false;
    }
    return domicilioIDao.delete(id);
  }
}
