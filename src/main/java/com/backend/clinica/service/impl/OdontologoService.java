package com.backend.clinica.service.impl;

import com.backend.clinica.dao.IDao;
import com.backend.clinica.dto.request.OdontologoRequestDto;
import com.backend.clinica.dto.response.OdontologoResponseDto;
import com.backend.clinica.entity.Odontologo;
import com.backend.clinica.service.IOdontologoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OdontologoService implements IOdontologoService<String, OdontologoRequestDto, OdontologoResponseDto> {
  private final IDao<String, Odontologo> odontologoIDao;

  public OdontologoService(IDao<String, Odontologo> odontologoIDao) {
    this.odontologoIDao = odontologoIDao;
  }

  @Override
  public OdontologoResponseDto createOdontologo(OdontologoRequestDto odontologo) {
    if (odontologo == null) {
      return null;
    }
    Odontologo created = new Odontologo(
            odontologo.getCodigo(),
            odontologo.getNombre(),
            odontologo.getApellido());
    Odontologo saved = odontologoIDao.create(created);
    if (saved == null) {
      return null;
    }
    Odontologo getSaveOdontologo = odontologoIDao.readOne(saved.getCodigo());
    if (getSaveOdontologo == null) {
      return null;
    }
    return mapToDto(getSaveOdontologo);
  }

  @Override
  public OdontologoResponseDto getOdontologoByCodigo(String codigo) {
    if (codigo == null) {
      return null;
    }
    Odontologo getOdontologo = odontologoIDao.readOne(codigo);
    if (getOdontologo == null) {
      return null;
    }
    return mapToDto(getOdontologo);
  }

  @Override
  public List<OdontologoResponseDto> getAllOdontologos() {
    List<OdontologoResponseDto> odontologoResponseDtoList = new ArrayList<>();
    List<Odontologo> odontologoList = odontologoIDao.readAll();
    if (odontologoList.isEmpty()) {
      return null;
    }
    for (Odontologo odontologo : odontologoList) {
      odontologoResponseDtoList.add(mapToDto(odontologo));
    }
    return odontologoResponseDtoList;
  }

  @Override
  public OdontologoResponseDto updateOdontologo(String codigo, OdontologoRequestDto odontologo) {
    if (codigo == null || odontologo == null) {
      return null;
    }
    Odontologo getOdontologo = odontologoIDao.readOne(codigo);
    if (getOdontologo == null) {
      return null;
    }
    Odontologo created = new Odontologo(
            getOdontologo.getId(),
            odontologo.getCodigo(),
            odontologo.getNombre(),
            odontologo.getApellido());
    Odontologo updated = odontologoIDao.update(codigo, created);
    if (updated == null) {
      return null;
    }
    Odontologo getUpdatedOdontologo = odontologoIDao.readOne(updated.getCodigo());
    if (getUpdatedOdontologo == null) {
      return null;
    }
    return mapToDto(getUpdatedOdontologo);
  }

  @Override
  public boolean deleteOdontologo(String codigo) {
    if (codigo == null) {
      return false;
    }
    return odontologoIDao.delete(codigo);
  }

  private OdontologoResponseDto mapToDto(Odontologo odontologo) {
    return new OdontologoResponseDto(
            odontologo.getId(),
            odontologo.getCodigo(),
            odontologo.getNombre(),
            odontologo.getApellido());
  }
}
