package com.backend.clinica.service.impl;

import com.backend.clinica.dto.request.EspecialidadRequestDto;
import com.backend.clinica.dto.response.EspecialidadResponseDto;
import com.backend.clinica.entity.Especialidad;
import com.backend.clinica.entity.Odontologo;
import com.backend.clinica.exception.IllegalArgException;
import com.backend.clinica.exception.ResourceNotFoundException;
import com.backend.clinica.repository.IEspecialidadRepository;
import com.backend.clinica.service.IEspecialidadService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EspecialidadService implements IEspecialidadService<Integer, EspecialidadRequestDto, EspecialidadResponseDto> {
  private final Logger LOGGER = LoggerFactory.getLogger(EspecialidadService.class);
  private final IEspecialidadRepository especialidadRepository;

  public EspecialidadService(IEspecialidadRepository especialidadRepository) {
    this.especialidadRepository = especialidadRepository;
  }

  @Override
  public EspecialidadResponseDto createEspecialidad(EspecialidadRequestDto especialidad) throws IllegalArgException {
    if (especialidad == null) {
      throw new IllegalArgException("Ingrese correctamente los datos de la Especialidad");
    }
    if (especialidadRepository.findByTipo(especialidad.getTipo()).isPresent()) {
      throw new IllegalArgException("Ya existe una especialidad con tipo: " + especialidad.getTipo());
    }
    Especialidad created = new Especialidad();
    created.setTipo(especialidad.getTipo());

    Especialidad saved = especialidadRepository.save(created);
    LOGGER.info("Especialidad guardada: {}", saved.getTipo());
    return mapToDto(saved);
  }

  @Override
  public EspecialidadResponseDto getEspecialidadById(Integer id) throws IllegalArgException, ResourceNotFoundException {
    if (id == null || id <= 0) {
      throw new IllegalArgException("Ingrese correctamente el id de la Especialidad");
    }
    Especialidad find = getEspecialidadOrThrow(id);
    return mapToDto(find);
  }

  @Override
  public List<EspecialidadResponseDto> getAllEspecialidades() {
    List<EspecialidadResponseDto> especialidadResponseDtoList = new ArrayList<>();
    List<Especialidad> especialidadList = especialidadRepository.findAll();
    for (Especialidad especialidad : especialidadList) {
      especialidadResponseDtoList.add(mapToDto(especialidad));
    }
    return especialidadResponseDtoList;
  }

  @Override
  public EspecialidadResponseDto updateEspecialidad(Integer id, EspecialidadRequestDto especialidad) throws IllegalArgException, ResourceNotFoundException {
    if (id == null || id <= 0 || especialidad.getTipo() == null) {
      throw new IllegalArgException("Ingrese correctamente el id de la Especialidad");
    }
    Especialidad find = getEspecialidadOrThrow(id);
    find.setTipo(especialidad.getTipo());

    Especialidad saved = especialidadRepository.save(find);
    LOGGER.info("Especialidad actualizado: {}", id);
    return mapToDto(saved);
  }

  @Transactional
  @Override
  public void deleteEspecialidad(Integer id) throws IllegalArgException, ResourceNotFoundException {
    if (id == null || id <= 0) {
      throw new IllegalArgException("Ingrese correctamente el id de la Especialidad");
    }
    Especialidad find = getEspecialidadOrThrow(id);
    for (Odontologo odontologo : find.getOdontologos()) {
      odontologo.getEspecialidades().remove(find);
    }
    LOGGER.info("Especialidad eliminada: {}", id);
    especialidadRepository.delete(find);
  }

  private Especialidad getEspecialidadOrThrow(Integer id) throws ResourceNotFoundException {
    return especialidadRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Especialidad " + id + " no encontrada"));
  }

  private EspecialidadResponseDto mapToDto(Especialidad especialidad) {
    return new EspecialidadResponseDto(especialidad.getId(), especialidad.getTipo());
  }
}
