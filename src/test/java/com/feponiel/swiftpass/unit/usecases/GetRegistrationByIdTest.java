package com.feponiel.swiftpass.unit.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.feponiel.swiftpass.domain.application.repositories.RegistrationsRepository;
import com.feponiel.swiftpass.domain.application.usecases.GetRegistrationByIdUseCase;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.RegistrationNotFoundException;
import com.feponiel.swiftpass.domain.business.entities.Registration;
import com.feponiel.swiftpass.factories.RegistrationFactory;
import com.feponiel.swiftpass.unit.UnitTest;

public class GetRegistrationByIdTest extends UnitTest {
  @Mock private RegistrationsRepository registrationsRepository;

  @InjectMocks
  private GetRegistrationByIdUseCase getRegistrationByIdUseCase;

  @Test
  void shouldGetRegistrationById() {
    Registration registration = RegistrationFactory.make();

    when(registrationsRepository.findById(registration.getId())).thenReturn(Optional.of(registration));

    Registration result = getRegistrationByIdUseCase.execute(registration.getId());

    assertThat(result).isEqualTo(registration);
  }

  @Test
  void shouldNotGetRegistrationWhenIdDoesNotExist() {
    UUID randomId = UUID.randomUUID();

    when(registrationsRepository.findById(randomId)).thenReturn(Optional.empty());

    assertThrows(RegistrationNotFoundException.class, () -> getRegistrationByIdUseCase.execute(randomId));
  }
}
