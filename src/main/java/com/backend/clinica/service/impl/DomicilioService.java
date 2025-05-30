package com.backend.clinica.service.impl;

import com.backend.clinica.dto.request.DomicilioRequestDto;
import com.backend.clinica.dto.response.DomicilioResponseDto;
import com.backend.clinica.entity.Domicilio;
import com.backend.clinica.repository.IDomicilioRepository;
import com.backend.clinica.service.IDomicilioService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DomicilioService implements IDomicilioService<Integer, DomicilioRequestDto, DomicilioResponseDto> {
  Logger LOGGER = LoggerFactory.getLogger(OdontologoService.class);
  private final IDomicilioRepository domicilioRepository;

  public DomicilioService(IDomicilioRepository domicilioRepository) {
    this.domicilioRepository = domicilioRepository;
  }

  @Override
  public DomicilioResponseDto createDomicilio(DomicilioRequestDto domicilio) {
    if (domicilio == null) {
      throw new IllegalArgumentException("Datos incompletos.");
    }
    Domicilio createdDomicilio = new Domicilio(
            domicilio.getCalle(),
            domicilio.getNumero(),
            domicilio.getLocalidad(),
            domicilio.getCiudad());
    Domicilio savedDomicilio = domicilioRepository.save(createdDomicilio);
    LOGGER.info("Domicilio guardado: {}", savedDomicilio.getId());
    return mapToDto(savedDomicilio);
  }

  @Override
  public DomicilioResponseDto getDomicilioById(Integer id) {
    if (id == null) {
      throw new IllegalArgumentException("Ingrese id.");
    }
    Domicilio findDomicilio = domicilioRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Domicilio no encontrado: " + id));
    return mapToDto(findDomicilio);
  }

  @Override
  public List<DomicilioResponseDto> getAllDomicilios() {
    List<DomicilioResponseDto> domicilioResponseDtoList = new ArrayList<>();
    List<Domicilio> domicilioList = domicilioRepository.findAll();
    for (Domicilio domicilio : domicilioList) {
      domicilioResponseDtoList.add(mapToDto(domicilio));
    }
    return domicilioResponseDtoList;
  }

  @Override
  public DomicilioResponseDto updateDomicilio(Integer id, DomicilioRequestDto domicilio) {
    if (id == null || domicilio == null) {
      throw new IllegalArgumentException("Datos invalidos para actualizar.");
    }
    Domicilio findDomicilio = domicilioRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Domicilio no encontrado: " + id));

    findDomicilio.setCalle(domicilio.getCalle());
    findDomicilio.setNumero(domicilio.getNumero());
    findDomicilio.setLocalidad(domicilio.getLocalidad());
    findDomicilio.setCiudad(domicilio.getCiudad());

    Domicilio updatedDomicilio = domicilioRepository.save(findDomicilio);
    LOGGER.info("Domicilio actualizado: {}", id);
    return mapToDto(updatedDomicilio);
  }

  @Override
  public boolean deleteDomicilio(Integer id) {
    if (id == null) {
      throw new IllegalArgumentException("Ingrese id.");
    }
    return domicilioRepository.findById(id)
            .map(domicilio -> {
              domicilioRepository.delete(domicilio);
              LOGGER.info("Domicilio eliminado: {}", id);
              return true;
            }).orElse(false);
  }

  private DomicilioResponseDto mapToDto(Domicilio domicilio) {
    return new DomicilioResponseDto(
            domicilio.getId(),
            domicilio.getCalle(),
            domicilio.getNumero(),
            domicilio.getLocalidad(),
            domicilio.getCiudad());
  }
}
