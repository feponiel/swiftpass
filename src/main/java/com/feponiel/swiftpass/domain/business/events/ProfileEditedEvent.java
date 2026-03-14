package com.feponiel.swiftpass.domain.business.events;

import com.feponiel.swiftpass.domain.business.entities.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProfileEditedEvent extends DomainEvent {
  private final User user;
}
