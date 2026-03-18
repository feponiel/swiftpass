package com.feponiel.swiftpass.infrastructure.http.presenters.dtos;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.feponiel.swiftpass.domain.business.valueobjects.PaymentStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RegistrationListHTTPResponseModel {
  @Getter
  @RequiredArgsConstructor
  public static class Registration {
    private final UUID id;
    private final UUID registrantId;
    private final UUID ticketId;
    private final UUID eventId;
    private final String holderName;
    private final PaymentStatus paymentStatus;
    private final String checkoutUrl;
    private final BigDecimal totalPaid;
    private final String paidCurrency;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Instant editedAt;
  }

  private final List<Registration> registrations;
}
