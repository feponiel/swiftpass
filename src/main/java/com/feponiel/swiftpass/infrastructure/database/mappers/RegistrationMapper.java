package com.feponiel.swiftpass.infrastructure.database.mappers;

import org.springframework.stereotype.Component;

import com.feponiel.swiftpass.domain.business.entities.Registration;
import com.feponiel.swiftpass.infrastructure.database.entities.JPARegistration;

@Component
public class RegistrationMapper {
  public Registration toDomain(JPARegistration rawRegistration) {
    return Registration.builder()
      .id(rawRegistration.getId())
      .registrantId(rawRegistration.getRegistrantId())
      .ticketId(rawRegistration.getTicketId())
      .eventId(rawRegistration.getEventId())
      .holderName(rawRegistration.getHolderName())
      .paymentStatus(rawRegistration.getPaymentStatus())
      .checkoutUrl(rawRegistration.getCheckoutUrl())
      .stripeSessionId(rawRegistration.getStripeSessionId())
      .totalPaid(rawRegistration.getTotalPaid())
      .paidCurrency(rawRegistration.getPaidCurrency())
      .createdAt(rawRegistration.getCreatedAt())
      .updatedAt(rawRegistration.getUpdatedAt())
      .editedAt(rawRegistration.getEditedAt())
      .build();
  }

  public JPARegistration toJPA(Registration rawRegistration) {
    return new JPARegistration(
      rawRegistration.getId(),
      rawRegistration.getRegistrantId(),
      rawRegistration.getTicketId(),
      rawRegistration.getEventId(),
      rawRegistration.getHolderName(),
      rawRegistration.getPaymentStatus(),
      rawRegistration.getCheckoutUrl(),
      rawRegistration.getStripeSessionId(),
      rawRegistration.getTotalPaid(),
      rawRegistration.getPaidCurrency(),
      rawRegistration.getCreatedAt(),
      rawRegistration.getUpdatedAt(),
      rawRegistration.getEditedAt()
    );
  }
}
