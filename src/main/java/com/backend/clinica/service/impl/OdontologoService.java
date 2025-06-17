package com.backend.clinica.service.impl;

import com.backend.clinica.dto.request.OdontologoRequestDto;
import com.backend.clinica.dto.response.EspecialidadResponseDto;
import com.backend.clinica.dto.response.OdontologoResponseDto;
import com.backend.clinica.entity.Especialidad;
import com.backend.clinica.entity.Odontologo;
import com.backend.clinica.exception.IllegalArgException;
import com.backend.clinica.exception.ResourceNotFoundException;
import com.backend.clinica.repository.IOdontologoRepository;
import com.backend.clinica.service.IOdontologoService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OdontologoService implements IOdontologoService<String, OdontologoRequestDto, OdontologoResponseDto> {
  private final Logger LOGGER = LoggerFactory.getLogger(OdontologoService.class);
  private final IOdontologoRepository odontologoRepository;

  public OdontologoService(IOdontologoRepository odontologoRepository) {
    this.odontologoRepository = odontologoRepository;
  }

  @Override
  public OdontologoResponseDto createOdontologo(OdontologoRequestDto odontologo) throws IllegalArgException {
    if (odontologo == null) {
      throw new IllegalArgException("Ingrese correctamente los datos del Odontólogo");
    }
    if (odontologoRepository.findByCodigo(odontologo.getCodigo()).isPresent()) {
      throw new IllegalArgException("Ya existe Odontólogo con Código: " + odontologo.getCodigo());
    }
    Odontologo created = new Odontologo(
        odontologo.getCodigo(),
        odontologo.getNombre(),
        odontologo.getApellido());
    Odontologo savedOdontologo = odontologoRepository.save(created);
    LOGGER.info("Odontólogo guardado: {}", savedOdontologo.getId());
    return mapToDto(savedOdontologo);
  }

  @Transactional
  @Override
  public OdontologoResponseDto getOdontologoByCodigo(String codigo) throws ResourceNotFoundException, IllegalArgException {
    if (codigo == null) {
      throw new IllegalArgException("Ingrese correctamente el Código del Odontólogo");
    }
    Odontologo findOdontologo = getOdontologoOrThrow(codigo);
    return mapToDto(findOdontologo);
  }

  @Transactional
  @Override
  public List<OdontologoResponseDto> getAllOdontologos() {
    List<OdontologoResponseDto> odontologoResponseDtoList = new ArrayList<>();
    List<Odontologo> odontologoList = odontologoRepository.findAll();
    for (Odontologo odontologo : odontologoList) {
      odontologoResponseDtoList.add(mapToDto(odontologo));
    }
    return odontologoResponseDtoList;
  }

  @Transactional
  @Override
  public OdontologoResponseDto updateOdontologo(String codigo, OdontologoRequestDto odontologo) throws IllegalArgException, ResourceNotFoundException {
    if (codigo == null || odontologo == null) {
      throw new IllegalArgException("Datos invalidos para actualizar Odontólogo: " + codigo);
    }
    Odontologo findOdontologo = getOdontologoOrThrow(codigo);

    findOdontologo.setNombre(odontologo.getNombre());
    findOdontologo.setApellido(odontologo.getApellido());

    Odontologo updatedOdontologo = odontologoRepository.save(findOdontologo);
    LOGGER.info("Odontólogo actualizado: {}", codigo);
    return mapToDto(updatedOdontologo);
  }

  @Override
  public void deleteOdontologo(String codigo) throws IllegalArgException, ResourceNotFoundException {
    if (codigo == null) {
      throw new IllegalArgException("Ingrese correctamente el Código del Odontólogo");
    }
    Odontologo odontologo = getOdontologoOrThrow(codigo);
    LOGGER.info("Odontólogo eliminado: {}", codigo);
    odontologoRepository.delete(odontologo);
  }

  private Odontologo getOdontologoOrThrow(String codigo) throws ResourceNotFoundException {
    return odontologoRepository.findByCodigo(codigo)
        .orElseThrow(() -> new ResourceNotFoundException("Odontólogo " + codigo + " no encontrado"));
  }

  private Set<EspecialidadResponseDto> mapToDto(Set<Especialidad> especialidades) {
    Set<EspecialidadResponseDto> especialidadResponseDtos = new HashSet<>();
    for (Especialidad especialidad : especialidades) {
      especialidadResponseDtos.add(new EspecialidadResponseDto(
          especialidad.getId(), especialidad.getTipo()
      ));
    }
    return especialidadResponseDtos;
  }

  private OdontologoResponseDto mapToDto(Odontologo odontologo) {
    return new OdontologoResponseDto(
        odontologo.getId(),
        odontologo.getCodigo(),
        odontologo.getNombre(),
        odontologo.getApellido(),
        mapToDto(odontologo.getEspecialidades())
    );
  }
}
