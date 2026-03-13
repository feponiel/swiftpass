package com.feponiel.swiftpass.domain.business.events;

import java.util.UUID;

import com.feponiel.swiftpass.domain.business.entities.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserEditedEvent extends DomainEvent {
  private final User editedUser;
  private final UUID editorId;
}
