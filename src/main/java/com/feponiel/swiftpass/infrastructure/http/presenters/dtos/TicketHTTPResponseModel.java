package com.feponiel.swiftpass.infrastructure.http.presenters.dtos;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TicketHTTPResponseModel {
  private final UUID id;
  private final UUID eventId;
  private final String name;
  private final String description;
  private final BigDecimal price;
  private final String currency;
  private final Integer amountAvailable;
  private final Boolean isSoldOut;
  private final Instant createdAt;
  private final Instant updatedAt;
  private final Instant editedAt;
}
