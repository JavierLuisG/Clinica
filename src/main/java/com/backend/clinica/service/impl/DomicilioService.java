package com.backend.clinica.service.impl;

import com.backend.clinica.dto.request.DomicilioRequestDto;
import com.backend.clinica.dto.response.DomicilioResponseDto;
import com.backend.clinica.entity.Domicilio;
import com.backend.clinica.exception.IllegalArgException;
import com.backend.clinica.exception.ResourceNotFoundException;
import com.backend.clinica.repository.IDomicilioRepository;
import com.backend.clinica.service.IDomicilioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DomicilioService implements IDomicilioService<Integer, DomicilioRequestDto, DomicilioResponseDto> {
  private final Logger LOGGER = LoggerFactory.getLogger(DomicilioService.class);
  private final IDomicilioRepository domicilioRepository;

  public DomicilioService(IDomicilioRepository domicilioRepository) {
    this.domicilioRepository = domicilioRepository;
  }

  @Override
  public DomicilioResponseDto createDomicilio(DomicilioRequestDto domicilio) throws IllegalArgException {
    if (domicilio == null) {
      throw new IllegalArgException("Ingrese correctamente los datos del Domicilio");
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
  public DomicilioResponseDto getDomicilioById(Integer id) throws IllegalArgException, ResourceNotFoundException {
    if (id == null || id <= 0) {
      throw new IllegalArgException("Ingrese correctamente el id de Domicilio");
    }
    Domicilio findDomicilio = getDomicilioOrThrow(id);
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
  public DomicilioResponseDto updateDomicilio(Integer id, DomicilioRequestDto domicilio) throws ResourceNotFoundException, IllegalArgException {
    if (id == null || id <= 0 || domicilio == null) {
      throw new IllegalArgException("Datos invalidos para actualizar Domicilio: " + id);
    }
    Domicilio findDomicilio = getDomicilioOrThrow(id);

    findDomicilio.setCalle(domicilio.getCalle());
    findDomicilio.setNumero(domicilio.getNumero());
    findDomicilio.setLocalidad(domicilio.getLocalidad());
    findDomicilio.setCiudad(domicilio.getCiudad());

    Domicilio updatedDomicilio = domicilioRepository.save(findDomicilio);
    LOGGER.info("Domicilio actualizado: {}", id);
    return mapToDto(updatedDomicilio);
  }

  @Override
  public void deleteDomicilio(Integer id) throws IllegalArgException, ResourceNotFoundException {
    if (id == null) {
      throw new IllegalArgException("Ingrese correctamente el id del Domicilio");
    }
    Domicilio domicilio = getDomicilioOrThrow(id);
    LOGGER.info("Domicilio eliminado: {}", id);
    domicilioRepository.delete(domicilio);
  }

  private Domicilio getDomicilioOrThrow(Integer id) throws ResourceNotFoundException {
    return domicilioRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Domicilio " + id + " no encontrado"));
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
