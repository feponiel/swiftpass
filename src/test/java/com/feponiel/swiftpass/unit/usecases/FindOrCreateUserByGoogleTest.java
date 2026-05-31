package com.feponiel.swiftpass.unit.usecases;

import static org.assertj.core.api.Assertions.assertThat;
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

import com.feponiel.swiftpass.domain.application.repositories.UsersRepository;
import com.feponiel.swiftpass.domain.application.usecases.FindOrCreateUserByGoogleUseCase;
import com.feponiel.swiftpass.domain.business.entities.User;
import com.feponiel.swiftpass.factories.UserFactory;

public class FindOrCreateUserByGoogleTest {
  @Mock private UsersRepository usersRepository;

  @InjectMocks
  private FindOrCreateUserByGoogleUseCase findOrCreateUserByGoogleUseCase;

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
  void shouldCreateUserWhenIdDoesNotExist() {
    String providerIdMock = UUID.randomUUID().toString();
    String nameMock = "John Doe";
    String emailMock = "johndoe@test.com";
    String pictureUrlMock = "https://picsum.photos/300";

    when(usersRepository.findByProviderId(providerIdMock)).thenReturn(Optional.empty());

    findOrCreateUserByGoogleUseCase.execute(providerIdMock, nameMock, emailMock, pictureUrlMock);

    verify(usersRepository, times(1)).create(any());
  }

  @Test
  void shouldFindUserWhenIdAlreadyExists() {
    String providerIdMock = UUID.randomUUID().toString();
    User userWithExistentProviderId = UserFactory.make(u -> u.providerId(providerIdMock));

    when(usersRepository.findByProviderId(providerIdMock)).thenReturn(Optional.of(userWithExistentProviderId));

    var user = findOrCreateUserByGoogleUseCase.execute(providerIdMock, null, null, null);

    verify(usersRepository, never()).create(any());

    assertThat(user).isEqualTo(userWithExistentProviderId);
  }
}
