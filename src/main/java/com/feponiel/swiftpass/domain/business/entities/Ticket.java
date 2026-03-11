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
  private Integer amountAvailable;
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
    @NonNull Integer amountAvailable,
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
    this.amountAvailable = amountAvailable;
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

  public void changeName(String newName) {
    this.name = newName;

    this.markEdited();
  }

  public void changeDescription(String newDescription) {
    this.description = newDescription;

    this.markEdited();
  }

  public void changePrice(BigDecimal newPrice) {
    this.price = newPrice;

    this.markEdited();
  }

  public void changeCurrency(String newCurrency) {
    this.currency = newCurrency;

    this.markEdited();
  }

  public void changeAmountAvailable(Integer newAmountAvailable) {
    this.amountAvailable = newAmountAvailable;

    this.markEdited();
  }

  public Boolean isSoldOut() {
    return this.amountAvailable <= 0;
  }
}
