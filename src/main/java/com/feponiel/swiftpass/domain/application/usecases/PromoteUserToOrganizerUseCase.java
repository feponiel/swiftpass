package com.feponiel.swiftpass.domain.application.usecases;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.feponiel.swiftpass.domain.application.repositories.UsersRepository;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.UserNotFoundException;
import com.feponiel.swiftpass.domain.business.entities.User;
import com.feponiel.swiftpass.domain.business.valueobjects.Role;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromoteUserToOrganizerUseCase {
  private UsersRepository usersRepository;

  public void execute(UUID userToPromoteId) {
    User userToPromote = this.usersRepository.findById(userToPromoteId)
      .orElseThrow(UserNotFoundException::new);

    userToPromote.updateRole(Role.ORGANIZER);

    this.usersRepository.update(userToPromote);
  }
}
