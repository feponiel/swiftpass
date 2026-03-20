package com.feponiel.swiftpass.domain.business.entities;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class Ticket extends Entity {
  private UUID eventId;
  private String name;
  private String description;
  private BigDecimal price;
  private String currency;
  private Integer capacity;
  private Instant createdAt;
  private Instant updatedAt;
  private Instant editedAt;

  @Builder
  private Ticket(
    @NonNull UUID id,
    @NonNull UUID eventId,
    @NonNull String name,
    String description,
    @NonNull BigDecimal price,
    @NonNull String currency,
    @NonNull Integer capacity,
    Instant createdAt,
    Instant updatedAt,
    Instant editedAt
  ) {
    super(id);

    this.eventId = eventId;
    this.name = name;
    this.description = description;
    this.price = price;
    this.currency = currency;
    this.capacity = capacity;
    this.createdAt = createdAt != null ? createdAt : Instant.now();
    this.updatedAt = updatedAt;
    this.editedAt = editedAt;
  }

  protected void touch() {
    this.updatedAt = Instant.now();
  }

  protected void markEdited() {
    this.touch();

    this.editedAt = Instant.now();
  }

  public Ticket changeName(String newName) {
    if (newName == null)
      return this;

    this.name = newName;

    this.markEdited();

    return this;
  }

  public Ticket changeDescription(String newDescription) {
    if (newDescription == null)
      return this;

    this.description = newDescription;

    this.markEdited();

    return this;
  }

  public Ticket changePrice(BigDecimal newPrice) {
    if (newPrice == null)
      return this;

    this.price = newPrice;

    this.markEdited();

    return this;
  }

  public Ticket changeCurrency(String newCurrency) {
    if (newCurrency == null)
      return this;

    this.currency = newCurrency;

    this.markEdited();

    return this;
  }

  public Ticket changeCapacity(Integer newCapacity) {
    if (newCapacity == null)
      return this;

    this.capacity = newCapacity;

    this.markEdited();

    return this;
  }
}
