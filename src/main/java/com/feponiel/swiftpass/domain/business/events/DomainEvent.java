package com.feponiel.swiftpass.domain.business.events;

import java.time.Instant;

import lombok.Getter;

@Getter
public abstract class DomainEvent {
  private final Instant occuredAt = Instant.now();
}
