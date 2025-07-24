package com.backend.clinica.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final AuthenticationProvider authProvider;
  private final JwtAuthFilterConfig authFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(auth -> {
          // endpoints sin autenticacion
          auth.requestMatchers("/api/auth/**").permitAll();
          auth.requestMatchers(HttpMethod.GET, "/odontologo/**").permitAll();

          // endpoints para SWAGGER
          auth.requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll();
          auth.requestMatchers(HttpMethod.GET, "/swagger-ui/index.html").permitAll();
          auth.requestMatchers(HttpMethod.GET, "/v2/**").permitAll();
          auth.requestMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll();
          auth.requestMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll();

          // endpoints con roles especificos
          auth.requestMatchers(HttpMethod.POST, "/odontologo/**").hasRole("ADMIN");
          auth.requestMatchers(HttpMethod.PUT, "/odontologo/**").hasRole("ADMIN");
          auth.requestMatchers(HttpMethod.DELETE, "/odontologo/**").hasRole("ADMIN");

          auth.requestMatchers(HttpMethod.POST, "/paciente/**").hasRole("ADMIN");
          auth.requestMatchers(HttpMethod.PUT, "/paciente/**").hasRole("ADMIN");
          auth.requestMatchers(HttpMethod.DELETE, "/paciente/**").hasRole("ADMIN");

          // endpoints que requieren autenticacion
          auth.requestMatchers(("/turno/**")).authenticated();
          auth.requestMatchers(HttpMethod.GET, "/paciente/**").authenticated();

          // cualquier otra solicitud debe estar autenticada
          auth.anyRequest().authenticated();
        })
        .csrf(config -> config.disable())
        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
        .authenticationProvider(authProvider)
        .build();
  }
}
