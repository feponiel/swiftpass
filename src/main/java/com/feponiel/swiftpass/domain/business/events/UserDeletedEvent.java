package com.feponiel.swiftpass.domain.business.events;

import java.util.UUID;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserDeletedEvent extends DomainEvent {
  private final UUID deletedUserId;
  private final UUID deleterId;
}
