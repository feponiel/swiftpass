package com.feponiel.swiftpass.domain.application.usecases;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.feponiel.swiftpass.domain.application.repositories.RegistrationsRepository;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.RegistrationNotFoundException;
import com.feponiel.swiftpass.domain.business.entities.Registration;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetRegistrationByIdUseCase {
  private final RegistrationsRepository registrationsRepository;

  public Registration execute(UUID registrationId) {
    return this.registrationsRepository.findById(registrationId)
      .orElseThrow(RegistrationNotFoundException::new);
  }
}
