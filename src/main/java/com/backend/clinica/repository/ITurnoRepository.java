package com.backend.clinica.repository;

import com.backend.clinica.entity.Turno;
import jakarta.websocket.server.PathParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ITurnoRepository extends JpaRepository<Turno, Integer> {
  @Query("select t from Turno t where t.fechaConsulta between :firstDate and :endDate")
  List<Turno> findByStartDateBetween(@PathParam("firstDate") LocalDateTime firstDate, @PathParam("endDate") LocalDateTime endDate);
}
