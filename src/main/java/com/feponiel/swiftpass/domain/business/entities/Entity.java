package com.feponiel.swiftpass.domain.business.entities;

import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public abstract class Entity {
  private final UUID id;

  protected Entity(UUID id) {
    this.id = id;
  }
}
