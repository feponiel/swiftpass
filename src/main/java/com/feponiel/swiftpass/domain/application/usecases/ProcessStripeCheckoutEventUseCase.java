package com.feponiel.swiftpass.domain.application.usecases;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.feponiel.swiftpass.domain.application.boundaries.StripeCheckoutEventData;
import com.feponiel.swiftpass.domain.application.repositories.RegistrationsRepository;
import com.feponiel.swiftpass.domain.application.repositories.TicketsRepository;
import com.feponiel.swiftpass.domain.application.services.StripeService;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.TicketNotFoundException;
import com.feponiel.swiftpass.domain.business.entities.Registration;
import com.feponiel.swiftpass.domain.business.entities.Ticket;
import com.feponiel.swiftpass.domain.business.events.DomainEvent;
import com.feponiel.swiftpass.domain.business.events.TicketOverbookedEvent;
import com.feponiel.swiftpass.domain.business.valueobjects.PaymentStatus;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProcessStripeCheckoutEventUseCase {
  private final RegistrationsRepository registrationsRepository;
  private final TicketsRepository ticketsRepository;
  private final StripeService stripeService;
  private final ApplicationEventPublisher eventPublisher;

  public void execute(String rawEvent, String stripeSignature) {
    StripeCheckoutEventData checkoutEventData =
      this.stripeService.parseWebhookAndGetSessionData(rawEvent, stripeSignature);

    if (checkoutEventData.eventType() == null) return;

    switch (checkoutEventData.eventType()) {
      case "checkout.session.completed" -> handleCheckoutCompleted(checkoutEventData.sessionId(), checkoutEventData.paymentIntentId());
      case "checkout.session.expired" -> handleCheckoutExpired(checkoutEventData.sessionId());
    }
  }

  private void handleCheckoutCompleted(String sessionId, String paymentIntentId) {
    List<Registration> registrations = this.registrationsRepository.listAllByStripeSessionId(sessionId);

    // Two separate loops are needed here: the first populates paidCurrency and totalPaid for all registrations,
    // so that if an overbooked scenario is detected in the second loop, markRegistrationsAsRefunded() will persist
    // complete data for every registration, not just for the ones processed before the overbooked one was found.

    for (Registration registration : registrations) {
      Ticket ticket = this.ticketsRepository.findById(registration.getTicketId())
        .orElseThrow(TicketNotFoundException::new);

      registration
        .updatePaidCurrency(ticket.getCurrency())
        .updateTotalPaid(ticket.getPrice());
    }

    for (Registration registration : registrations) {
      Ticket ticket = this.ticketsRepository.findByIdWithLock(registration.getTicketId())
        .orElseThrow(TicketNotFoundException::new);

      Integer confirmedCount = this.registrationsRepository.countConfirmedByTicketId(ticket.getId());
      Integer remaining = ticket.getCapacity() - confirmedCount;

      if (remaining <= 0) {
        DomainEvent ticketOverbookedEvent = new TicketOverbookedEvent(paymentIntentId);
        
        this.eventPublisher.publishEvent(ticketOverbookedEvent);

        markRegistrationsAsRefunded(registrations);

        return;
      }

      registration.updatePaymentStatus(PaymentStatus.PAID);
      this.registrationsRepository.update(registration);
    }
  }

  private void markRegistrationsAsRefunded(List<Registration> registrations) {
    for (Registration registration : registrations) {
      registration.updatePaymentStatus(PaymentStatus.REFUNDED);
      this.registrationsRepository.update(registration);
    }
  }

  private void handleCheckoutExpired(String sessionId) {
    List<Registration> registrations = this.registrationsRepository.listAllByStripeSessionId(sessionId);

    for (Registration registration : registrations) {
      registration.updatePaymentStatus(PaymentStatus.EXPIRED);
      this.registrationsRepository.update(registration);
    }
  }
}