package com.feponiel.swiftpass.unit.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.feponiel.swiftpass.domain.application.repositories.UsersRepository;
import com.feponiel.swiftpass.domain.application.usecases.DemoteOrganizerUseCase;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.UserNotFoundException;
import com.feponiel.swiftpass.domain.business.entities.User;
import com.feponiel.swiftpass.domain.business.valueobjects.Role;
import com.feponiel.swiftpass.factories.UserFactory;

public class DemoteOrganizerTest {
  @Mock private UsersRepository usersRepository;

  @InjectMocks
  private DemoteOrganizerUseCase demoteOrganizerUseCase;

  private AutoCloseable mocks;

  @BeforeEach()
  void setup() {
    mocks = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }

  @Test
  void shouldDemoteOrganizer() {
    User organizerToDemote = UserFactory.make(o -> o.role(Role.ORGANIZER));

    when(usersRepository.findById(organizerToDemote.getId())).thenReturn(Optional.of(organizerToDemote));

    demoteOrganizerUseCase.execute(organizerToDemote.getId());

    ArgumentCaptor<User> captor = ArgumentCaptor.captor();
    verify(usersRepository, times(1)).update(captor.capture());

    assertThat(captor.getValue().getRole()).isEqualTo(Role.DEFAULT);
  }

  @Test
  void shouldNotDemoteOrganizerWhenItDoesNotExist() {
    UUID randomId = UUID.randomUUID();

    when(usersRepository.findById(randomId)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> demoteOrganizerUseCase.execute(randomId));

    verify(usersRepository, never()).update(any(User.class));
  }
}
