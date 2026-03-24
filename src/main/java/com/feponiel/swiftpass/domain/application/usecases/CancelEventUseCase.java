package com.feponiel.swiftpass.domain.application.usecases;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.feponiel.swiftpass.domain.application.repositories.EventsRepository;
import com.feponiel.swiftpass.domain.application.repositories.RegistrationsRepository;
import com.feponiel.swiftpass.domain.application.services.StripeService;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.EventNotFoundException;
import com.feponiel.swiftpass.domain.business.entities.Event;
import com.feponiel.swiftpass.domain.business.entities.Registration;
import com.feponiel.swiftpass.domain.business.valueobjects.PaymentStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CancelEventUseCase {
  private final RegistrationsRepository registrationsRepository;
  private final EventsRepository eventsRepository;
  private final StripeService stripeService;

  public void execute(UUID eventId) {
    Event event = this.eventsRepository.findById(eventId)
      .orElseThrow(EventNotFoundException::new);

    event.closeSales();
    this.eventsRepository.update(event);

    List<Registration> pendingRegistrations = this.registrationsRepository
      .listAllByEventIdAndPaymentStatus(eventId, PaymentStatus.PENDING);

    List<Registration> paidRegistrations = this.registrationsRepository
      .listAllByEventIdAndPaymentStatus(eventId, PaymentStatus.PAID);

    List<String> pendingSessionIds = pendingRegistrations.stream()
      .map(Registration::getStripeSessionId)
      .distinct()
      .toList();

    List<String> paidSessionIds = paidRegistrations.stream()
      .map(Registration::getStripeSessionId)
      .distinct()
      .toList();

    CompletableFuture<Void> expireFuture = this.stripeService.expireSessionsInBatch(pendingSessionIds);
    CompletableFuture<Void> refundFuture = this.stripeService.processRefundsInBatch(paidSessionIds);

    CompletableFuture.allOf(expireFuture, refundFuture).join();

    this.registrationsRepository.updateFromPaidToRefundedByEventId(eventId);
  }
}
