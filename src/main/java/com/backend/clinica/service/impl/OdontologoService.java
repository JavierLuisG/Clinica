package com.backend.clinica.service.impl;

import com.backend.clinica.dto.request.OdontologoRequestDto;
import com.backend.clinica.dto.response.OdontologoResponseDto;
import com.backend.clinica.entity.Odontologo;
import com.backend.clinica.repository.IOdontologoRepository;
import com.backend.clinica.service.IOdontologoService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OdontologoService implements IOdontologoService<String, OdontologoRequestDto, OdontologoResponseDto> {
  Logger LOGGER = LoggerFactory.getLogger(OdontologoService.class);
  private final IOdontologoRepository odontologoRepository;

  public OdontologoService(IOdontologoRepository odontologoRepository) {
    this.odontologoRepository = odontologoRepository;
  }

  @Override
  public OdontologoResponseDto createOdontologo(OdontologoRequestDto odontologo) {
    if (odontologo == null) {
      throw new IllegalArgumentException("Datos incompletos.");
    }
    if (odontologoRepository.findByCodigo(odontologo.getCodigo()).isPresent()) {
      throw new IllegalArgumentException("Ya existe odontólogo con código: " + odontologo.getCodigo());
    }
    Odontologo created = new Odontologo(
            odontologo.getCodigo(),
            odontologo.getNombre(),
            odontologo.getApellido());
    Odontologo savedOdontologo = odontologoRepository.save(created);
    LOGGER.info("Odontólogo guardado: {}", savedOdontologo.getId());
    return mapToDto(savedOdontologo);
  }

  @Override
  public OdontologoResponseDto getOdontologoByCodigo(String codigo) {
    if (codigo == null) {
      throw new IllegalArgumentException("Ingrese código.");
    }
    Odontologo findOdontologo = odontologoRepository
            .findByCodigo(codigo)
            .orElseThrow(() -> new EntityNotFoundException("Odontólogo no encontrado: " + codigo));
    return mapToDto(findOdontologo);
  }

  @Override
  public List<OdontologoResponseDto> getAllOdontologos() {
    List<OdontologoResponseDto> odontologoResponseDtoList = new ArrayList<>();
    List<Odontologo> odontologoList = odontologoRepository.findAll();
    for (Odontologo odontologo : odontologoList) {
      odontologoResponseDtoList.add(mapToDto(odontologo));
    }
    return odontologoResponseDtoList;
  }

  @Override
  public OdontologoResponseDto updateOdontologo(String codigo, OdontologoRequestDto odontologo) {
    if (codigo == null || odontologo == null) {
      throw new IllegalArgumentException("Datos invalidos para actualizar.");
    }
    Odontologo findOdontologo = odontologoRepository
            .findByCodigo(codigo)
            .orElseThrow(() -> new EntityNotFoundException("Odontólogo no encontrado: " + codigo));

    findOdontologo.setNombre(odontologo.getNombre());
    findOdontologo.setApellido(odontologo.getApellido());

    Odontologo updatedOdontologo = odontologoRepository.save(findOdontologo);
    LOGGER.info("Odontólogo actualizado: {}", codigo);
    return mapToDto(updatedOdontologo);
  }

  @Override
  public boolean deleteOdontologo(String codigo) {
    if (codigo == null) {
      throw new IllegalArgumentException("Ingrese código.");
    }
    return odontologoRepository.findByCodigo(codigo)
            .map(odontologo -> {
              odontologoRepository.delete(odontologo);
              LOGGER.info("Odontólogo eliminado: {}", codigo);
              return true;
            }).orElse(false);
  }

  private OdontologoResponseDto mapToDto(Odontologo odontologo) {
    return new OdontologoResponseDto(
            odontologo.getId(),
            odontologo.getCodigo(),
            odontologo.getNombre(),
            odontologo.getApellido());
  }
}
