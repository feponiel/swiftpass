package com.feponiel.swiftpass.domain.application.usecases;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.feponiel.swiftpass.domain.application.repositories.UsersRepository;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.NoFieldProvidedToEditException;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.UserNotFoundException;
import com.feponiel.swiftpass.domain.business.entities.User;
import com.feponiel.swiftpass.domain.business.events.UserEditedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EditUserUseCase {
  private final UsersRepository usersRepository;
  private final ApplicationEventPublisher eventPublisher;

  public void execute(UUID userToEditId, UUID editorId, String name, String pictureUrl) {
    if (name == null && pictureUrl == null) {
      throw new NoFieldProvidedToEditException();
    }

    User user = this.usersRepository.findById(userToEditId)
      .orElseThrow(UserNotFoundException::new);

    user
      .changeName(name)
      .changePicture(pictureUrl);

    this.usersRepository.update(user);

    UserEditedEvent userEditedEvent = new UserEditedEvent(user, editorId);

    this.eventPublisher.publishEvent(userEditedEvent);
  }
}
