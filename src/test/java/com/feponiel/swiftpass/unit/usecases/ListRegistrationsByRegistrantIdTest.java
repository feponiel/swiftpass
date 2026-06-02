package com.feponiel.swiftpass.unit.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.feponiel.swiftpass.domain.application.repositories.RegistrationsRepository;
import com.feponiel.swiftpass.domain.application.repositories.UsersRepository;
import com.feponiel.swiftpass.domain.application.usecases.ListRegistrationsByRegistrantIdUseCase;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.UserNotFoundException;
import com.feponiel.swiftpass.domain.business.entities.Registration;
import com.feponiel.swiftpass.domain.business.entities.User;
import com.feponiel.swiftpass.factories.RegistrationFactory;
import com.feponiel.swiftpass.factories.UserFactory;

public class ListRegistrationsByRegistrantIdTest {
  @Mock private RegistrationsRepository registrationsRepository;
  @Mock private UsersRepository usersRepository;

  @InjectMocks
  private ListRegistrationsByRegistrantIdUseCase listRegistrationsByRegistrantIdUseCase;

  private AutoCloseable mocks;

  @BeforeEach
  void setup() {
    mocks = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }

  @Test
  void shouldListRegistrationsByRegistrantId() {
    User registrant = UserFactory.make();
    Registration registration1 = RegistrationFactory.make(r -> r.registrantId(registrant.getId()));
    Registration registration2 = RegistrationFactory.make(r -> r.registrantId(registrant.getId()));

    when(usersRepository.findById(registrant.getId())).thenReturn(Optional.of(registrant));
    when(registrationsRepository.listAllByRegistrantId(registrant.getId())).thenReturn(List.of(registration1, registration2));

    var result = listRegistrationsByRegistrantIdUseCase.execute(registrant.getId());

    verify(registrationsRepository, times(1)).listAllByRegistrantId(registrant.getId());

    assertThat(result).containsExactlyInAnyOrder(registration1, registration2);
  }

  @Test
  void shouldReturnEmptyListWhenThereIsNoRegistrations() {
    User registrant = UserFactory.make();

    when(usersRepository.findById(registrant.getId())).thenReturn(Optional.of(registrant));
    when(registrationsRepository.listAllByRegistrantId(registrant.getId())).thenReturn(List.of());

    var result = listRegistrationsByRegistrantIdUseCase.execute(registrant.getId());

    verify(registrationsRepository, times(1)).listAllByRegistrantId(registrant.getId());

    assertThat(result).isEmpty();
  }

  @Test
  void shouldNotListRegistrationsWhenRegistrantDoesNotExist() {
    UUID randomId = UUID.randomUUID();

    when(usersRepository.findById(randomId)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> listRegistrationsByRegistrantIdUseCase.execute(randomId));

    verify(registrationsRepository, never()).listAllByRegistrantId(randomId);
  }
}
