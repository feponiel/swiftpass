package com.feponiel.swiftpass.domain.application.usecases;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.feponiel.swiftpass.domain.application.repositories.UsersRepository;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.NoFieldProvidedToEditException;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.UserNotFoundException;
import com.feponiel.swiftpass.domain.business.entities.User;
import com.feponiel.swiftpass.domain.business.events.ProfileEditedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EditProfileUseCase {
  private final UsersRepository usersRepository;
  private final ApplicationEventPublisher eventPublisher;

  public void execute(UUID userId, String name, String pictureUrl) {
    if (name == null && pictureUrl == null) {
      throw new NoFieldProvidedToEditException();
    }

    User user = this.usersRepository.findById(userId)
      .orElseThrow(UserNotFoundException::new);

    user
      .changeName(name)
      .changePicture(pictureUrl);

    this.usersRepository.update(user);

    ProfileEditedEvent profileEditedEvent = new ProfileEditedEvent(user);

    this.eventPublisher.publishEvent(profileEditedEvent);
  }
}
