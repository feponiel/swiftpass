package com.feponiel.swiftpass.domain.application.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.feponiel.swiftpass.domain.business.entities.Registration;
import com.feponiel.swiftpass.domain.business.valueobjects.PaymentStatus;

public interface RegistrationsRepository {
  void create(Registration registration);
  Optional<Registration> findById(UUID id);
  List<Registration> listAllByRegistrantId(UUID registrantId);
  List<Registration> listAllByStripeSessionId(String stripeSessionId);
  List<Registration> listAllByEventIdAndPaymentStatus(UUID eventId, PaymentStatus paymentStatus);
  Integer countConfirmedByTicketId(UUID ticketId);
  void update(Registration registration);
  void deleteById(UUID id);
}
