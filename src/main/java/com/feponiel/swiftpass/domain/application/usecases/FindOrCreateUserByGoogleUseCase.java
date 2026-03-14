package com.feponiel.swiftpass.domain.application.usecases;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.feponiel.swiftpass.domain.application.repositories.UsersRepository;
import com.feponiel.swiftpass.domain.business.entities.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FindOrCreateUserByGoogleUseCase {
  private final UsersRepository usersRepository;

  public User execute(String providerId, String name, String email, String pictureUrl) {
    return usersRepository.findByProviderId(providerId)
      .orElseGet(() -> {
        User user = User.builder()
          .id(UUID.randomUUID())
          .providerId(providerId)
          .name(name)
          .email(email)
          .pictureUrl(pictureUrl)
          .build();

        this.usersRepository.create(user);

        return user;
      });
  }
}
