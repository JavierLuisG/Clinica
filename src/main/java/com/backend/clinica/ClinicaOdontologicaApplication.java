package com.backend.clinica;

import com.backend.clinica.db.H2Connection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClinicaOdontologicaApplication {

  public static void main(String[] args) {
    SpringApplication.run(ClinicaOdontologicaApplication.class, args);
    H2Connection.createTable();
  }
}
