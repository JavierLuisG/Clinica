package com.backend.clinica.service.impl;

import com.backend.clinica.dto.request.EspecialidadRequestDto;
import com.backend.clinica.dto.response.EspecialidadResponseDto;
import com.backend.clinica.entity.Especialidad;
import com.backend.clinica.exception.IllegalArgException;
import com.backend.clinica.exception.ResourceNotFoundException;
import com.backend.clinica.repository.IEspecialidadRepository;
import com.backend.clinica.service.IEspecialidadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
  public EspecialidadResponseDto getEspecialidadById(Integer id) {
    return null;
  }

  @Override
  public List<EspecialidadResponseDto> getAllEspecialidades() {
    return null;
  }

  @Override
  public EspecialidadResponseDto updateEspecialidad(Integer id, EspecialidadRequestDto especialidad) {
    return null;
  }

  @Override
  public void deleteEspecialidad(Integer id) {

  }

  private Especialidad getEspecialidadOrThrow(Integer id) throws ResourceNotFoundException {
    return especialidadRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Especialidad " + id + " no encontrada"));
  }

  private EspecialidadResponseDto mapToDto(Especialidad especialidad) {
    return new EspecialidadResponseDto(especialidad.getId(), especialidad.getTipo());
  }
}
