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
import org.springframework.context.ApplicationEventPublisher;

import com.feponiel.swiftpass.domain.application.repositories.UsersRepository;
import com.feponiel.swiftpass.domain.application.usecases.EditProfileUseCase;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.NoFieldProvidedToEditException;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.UserNotFoundException;
import com.feponiel.swiftpass.domain.business.entities.User;
import com.feponiel.swiftpass.domain.business.events.ProfileEditedEvent;
import com.feponiel.swiftpass.factories.UserFactory;

public class EditProfileTest {
  @Mock private UsersRepository usersRepository;
  @Mock private ApplicationEventPublisher eventPublisher;

  @InjectMocks
  private EditProfileUseCase editProfileUseCase;

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
  void shouldEditProfile() {
    User user = UserFactory.make();

    when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));

    editProfileUseCase.execute(user.getId(), "Brand New Name", "https://picsum.photos/500");

    ArgumentCaptor<User> captor = ArgumentCaptor.captor();
    verify(usersRepository, times(1)).update(captor.capture());

    assertThat(captor.getValue().getName()).isEqualTo("Brand New Name");
    assertThat(captor.getValue().getPictureUrl()).isEqualTo("https://picsum.photos/500");

    verify(eventPublisher, times(1)).publishEvent(any(ProfileEditedEvent.class));
  }

  @Test
  void shouldNotEditProfileWhenNoFieldsAreProvidedToEdit() {
    UUID randomId = UUID.randomUUID();

    assertThrows(NoFieldProvidedToEditException.class, () -> editProfileUseCase.execute(randomId, null, null));

    verify(usersRepository, never()).update(any());
    verify(eventPublisher, never()).publishEvent(any(ProfileEditedEvent.class));
  }

  @Test
  void shouldNotEditProfileWhenUserDoesNotExist() {
    UUID randomId = UUID.randomUUID();

    when(usersRepository.findById(randomId)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> editProfileUseCase.execute(randomId, "Brand New Name", "https://picsum.photos/300"));

    verify(usersRepository, never()).update(any());
    verify(eventPublisher, never()).publishEvent(any(ProfileEditedEvent.class));
  }
}
