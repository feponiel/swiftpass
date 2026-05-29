package com.feponiel.swiftpass.unit.usecases;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import com.feponiel.swiftpass.domain.application.repositories.UsersRepository;
import com.feponiel.swiftpass.domain.application.usecases.DeleteUserUseCase;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.UserNotFoundException;
import com.feponiel.swiftpass.domain.business.entities.User;
import com.feponiel.swiftpass.domain.business.events.UserDeletedEvent;
import com.feponiel.swiftpass.domain.business.valueobjects.Role;
import com.feponiel.swiftpass.factories.UserFactory;

public class DeleteUserTest {
  @Mock private UsersRepository usersRepository;
  @Mock private ApplicationEventPublisher eventPublisher;

  @InjectMocks
  private DeleteUserUseCase deleteUserUseCase;

  private AutoCloseable mocks;

  @BeforeEach
  void setup() {
    mocks = MockitoAnnotations.openMocks(this);
  }

  @AfterEach()
  void tearDown() throws Exception {
    mocks.close();
  }

  @Test
  void shouldDeleteUser() {
    User userToDelete = UserFactory.make();
    User deleter = UserFactory.make(d -> d.role(Role.ORGANIZER));

    when(usersRepository.findById(userToDelete.getId())).thenReturn(Optional.of(userToDelete));

    deleteUserUseCase.execute(userToDelete.getId(), deleter.getId());

    verify(usersRepository, times(1)).deleteById(userToDelete.getId());
    verify(eventPublisher, times(1)).publishEvent(any(UserDeletedEvent.class));
  }

  @Test
  void shouldNotDeleteUserWhenItDoesNotExist() {
    UUID randomId = UUID.randomUUID();
    User deleter = UserFactory.make(d -> d.role(Role.ORGANIZER));

    when(usersRepository.findById(randomId)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> deleteUserUseCase.execute(randomId, deleter.getId()));

    verify(usersRepository, never()).deleteById(randomId);
    verify(eventPublisher, never()).publishEvent(any(UserDeletedEvent.class));
  }
}
