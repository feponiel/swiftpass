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

    // PENDING registrations are not updated here intentionally.
    // When Stripe expires the sessions, it fires "checkout.session.expired" webhooks
    // which are handled by ProcessStripeCheckoutEventUseCase, updating them to EXPIRED.
    //
    // PAID registrations are updated synchronously here via updateFromPaidToRefundedByEventId
    // instead of relying on the "charge.refunded" webhook. This is because "charge.refunded"
    // is also fired for individual refunds (RefundRegistrationUseCase), and does not distinguish
    // between partial and full refunds, handling it in the webhook would require checking the
    // refunded amount against the total, and would still risk marking all registrations from the
    // same session as REFUNDED even when only one was partially refunded. To avoid these edge
    // cases, the bulk update is done synchronously here, and the webhook is not used for this.

    this.registrationsRepository.updateFromPaidToRefundedByEventId(eventId);
  }
}
