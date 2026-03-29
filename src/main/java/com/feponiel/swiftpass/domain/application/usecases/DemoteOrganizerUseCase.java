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
public class DemoteOrganizerUseCase {
  private UsersRepository usersRepository;

  public void execute(UUID organizerToDemoteId) {
    User organizerToDemote = this.usersRepository.findById(organizerToDemoteId)
      .orElseThrow(UserNotFoundException::new);

    organizerToDemote.updateRole(Role.DEFAULT);
    
    this.usersRepository.update(organizerToDemote);
  }
}
