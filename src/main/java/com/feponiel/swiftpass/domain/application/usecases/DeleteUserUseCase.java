package com.feponiel.swiftpass.domain.application.usecases;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.feponiel.swiftpass.domain.application.repositories.UsersRepository;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.UserNotFoundException;
import com.feponiel.swiftpass.domain.business.events.UserDeletedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteUserUseCase {
  private final UsersRepository usersRepository;
  private final ApplicationEventPublisher eventPublisher;

  public void execute(UUID userToDeleteId, UUID deleterId) {
    this.usersRepository.findById(userToDeleteId)
      .orElseThrow(UserNotFoundException::new);

    this.usersRepository.deleteById(userToDeleteId);

    UserDeletedEvent userDeletedEvent = new UserDeletedEvent(userToDeleteId, deleterId);

    this.eventPublisher.publishEvent(userDeletedEvent);
  }
}
