package com.feponiel.swiftpass.domain.application.usecases;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.feponiel.swiftpass.domain.application.repositories.RegistrationsRepository;
import com.feponiel.swiftpass.domain.application.repositories.UsersRepository;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.UserNotFoundException;
import com.feponiel.swiftpass.domain.business.entities.Registration;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListRegistrationsByRegistrantIdUseCase {
  private final RegistrationsRepository registrationsRepository;
  private final UsersRepository usersRepository;

  public List<Registration> execute(UUID registrantId) {
    this.usersRepository.findById(registrantId)
      .orElseThrow(UserNotFoundException::new);

    return this.registrationsRepository.listAllByRegistrantId(registrantId);
  }
}
