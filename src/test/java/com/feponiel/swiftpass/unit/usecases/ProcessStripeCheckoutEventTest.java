package com.feponiel.swiftpass.unit.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;

import com.feponiel.swiftpass.domain.application.boundaries.StripeCheckoutEventData;
import com.feponiel.swiftpass.domain.application.repositories.RegistrationsRepository;
import com.feponiel.swiftpass.domain.application.repositories.TicketsRepository;
import com.feponiel.swiftpass.domain.application.services.StripeService;
import com.feponiel.swiftpass.domain.application.usecases.ProcessStripeCheckoutEventUseCase;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.TicketNotFoundException;
import com.feponiel.swiftpass.domain.business.entities.Registration;
import com.feponiel.swiftpass.domain.business.entities.Ticket;
import com.feponiel.swiftpass.domain.business.events.TicketOverbookedEvent;
import com.feponiel.swiftpass.domain.business.valueobjects.PaymentStatus;
import com.feponiel.swiftpass.factories.RegistrationFactory;
import com.feponiel.swiftpass.factories.TicketFactory;
import com.feponiel.swiftpass.unit.UnitTest;

public class ProcessStripeCheckoutEventTest extends UnitTest {

  @Mock private RegistrationsRepository registrationsRepository;
  @Mock private TicketsRepository ticketsRepository;
  @Mock private StripeService stripeService;
  @Mock private ApplicationEventPublisher eventPublisher;

  @InjectMocks
  private ProcessStripeCheckoutEventUseCase processStripeCheckoutEventUseCase;

  // helpers ---------------------------------------------------------------------------------------------------------

  private void mockCompletedEvent(String sessionId, String paymentIntentId) {
    when(stripeService.parseWebhookAndGetSessionData(any(), any()))
      .thenReturn(new StripeCheckoutEventData("checkout.session.completed", sessionId, paymentIntentId));
  }

  private void mockExpiredEvent(String sessionId) {
    when(stripeService.parseWebhookAndGetSessionData(any(), any()))
      .thenReturn(new StripeCheckoutEventData("checkout.session.expired", sessionId, null));
  }

  // checkout.session.completed - happy path -------------------------------------------------------------------------

  @Test
  void shouldMarkRegistrationsAsPaidWhenCheckoutCompletes() {
    String sessionId = UUID.randomUUID().toString();
    String paymentIntentId = UUID.randomUUID().toString();
    UUID eventId = UUID.randomUUID();

    Ticket ticket = TicketFactory.make(t -> t.eventId(eventId).capacity(10));

    Registration registration1 = RegistrationFactory.make(r -> r.stripeSessionId(sessionId).eventId(eventId).ticketId(ticket.getId()));
    Registration registration2 = RegistrationFactory.make(r -> r.stripeSessionId(sessionId).eventId(eventId).ticketId(ticket.getId()));

    mockCompletedEvent(sessionId, paymentIntentId);
    when(registrationsRepository.listAllByStripeSessionId(sessionId)).thenReturn(List.of(registration1, registration2));
    when(ticketsRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
    when(ticketsRepository.findByIdWithLock(ticket.getId())).thenReturn(Optional.of(ticket));
    when(registrationsRepository.countConfirmedByTicketId(ticket.getId())).thenReturn(0);

    processStripeCheckoutEventUseCase.execute("raw", "sig");

    ArgumentCaptor<Registration> captor = ArgumentCaptor.captor();
    verify(registrationsRepository, times(2)).update(captor.capture());

    assertThat(captor.getAllValues()).allMatch(r -> r.getPaymentStatus() == PaymentStatus.PAID);
  }

  @Test
  void shouldPopulatePaidCurrencyAndTotalPaidOnCompletion() {
    String sessionId = UUID.randomUUID().toString();
    UUID eventId = UUID.randomUUID();

    Ticket ticket = TicketFactory.make(t -> t.eventId(eventId).capacity(10));

    Registration registration = RegistrationFactory.make(r -> r.stripeSessionId(sessionId).eventId(eventId).ticketId(ticket.getId()));

    mockCompletedEvent(sessionId, UUID.randomUUID().toString());
    when(registrationsRepository.listAllByStripeSessionId(sessionId)).thenReturn(List.of(registration));
    when(ticketsRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
    when(ticketsRepository.findByIdWithLock(ticket.getId())).thenReturn(Optional.of(ticket));
    when(registrationsRepository.countConfirmedByTicketId(ticket.getId())).thenReturn(0);

    processStripeCheckoutEventUseCase.execute("raw", "sig");

    assertThat(registration.getPaidCurrency()).isEqualTo(ticket.getCurrency());
    assertThat(registration.getTotalPaid()).isEqualTo(ticket.getPrice());
  }

  // checkout.session.completed - overbooked ------------------------------------------------------------------------

  @Test
  void shouldMarkAllRegistrationsAsRefundedWhenTicketIsOverbooked() {
    String sessionId = UUID.randomUUID().toString();
    String paymentIntentId = UUID.randomUUID().toString();
    UUID eventId = UUID.randomUUID();

    Ticket ticket = TicketFactory.make(t -> t.eventId(eventId).capacity(1));

    Registration registration1 = RegistrationFactory.make(r -> r.stripeSessionId(sessionId).eventId(eventId).ticketId(ticket.getId()));
    Registration registration2 = RegistrationFactory.make(r -> r.stripeSessionId(sessionId).eventId(eventId).ticketId(ticket.getId()));

    mockCompletedEvent(sessionId, paymentIntentId);
    when(registrationsRepository.listAllByStripeSessionId(sessionId)).thenReturn(List.of(registration1, registration2));
    when(ticketsRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
    when(ticketsRepository.findByIdWithLock(ticket.getId())).thenReturn(Optional.of(ticket));
    // capacity = 1, already confirmed = 1 → remaining = 0 → overbooked
    when(registrationsRepository.countConfirmedByTicketId(ticket.getId())).thenReturn(1);

    processStripeCheckoutEventUseCase.execute("raw", "sig");

    ArgumentCaptor<Registration> captor = ArgumentCaptor.captor();
    verify(registrationsRepository, times(2)).update(captor.capture());

    assertThat(captor.getAllValues()).allMatch(r -> r.getPaymentStatus() == PaymentStatus.REFUNDED);
  }

  @Test
  void shouldPublishTicketOverbookedEventWhenOverbookingDetected() {
    String sessionId = UUID.randomUUID().toString();
    String paymentIntentId = UUID.randomUUID().toString();
    UUID eventId = UUID.randomUUID();

    Ticket ticket = TicketFactory.make(t -> t.eventId(eventId).capacity(1));

    Registration registration = RegistrationFactory.make(r -> r.stripeSessionId(sessionId).eventId(eventId).ticketId(ticket.getId()));

    mockCompletedEvent(sessionId, paymentIntentId);
    when(registrationsRepository.listAllByStripeSessionId(sessionId)).thenReturn(List.of(registration));
    when(ticketsRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
    when(ticketsRepository.findByIdWithLock(ticket.getId())).thenReturn(Optional.of(ticket));
    when(registrationsRepository.countConfirmedByTicketId(ticket.getId())).thenReturn(1);

    processStripeCheckoutEventUseCase.execute("raw", "sig");

    ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.captor();
    verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());

    assertThat(eventCaptor.getValue()).isInstanceOf(TicketOverbookedEvent.class);
    assertThat(((TicketOverbookedEvent) eventCaptor.getValue()).getPaymentIntentId()).isEqualTo(paymentIntentId);
  }

  @Test
  void shouldPopulatePaidCurrencyAndTotalPaidEvenWhenOverbookedSoRefundDataIsComplete() {
    String sessionId = UUID.randomUUID().toString();
    UUID eventId = UUID.randomUUID();

    Ticket ticket = TicketFactory.make(t -> t.eventId(eventId).capacity(1));

    Registration registration1 = RegistrationFactory.make(r -> r .stripeSessionId(sessionId).eventId(eventId).ticketId(ticket.getId()));
    Registration registration2 = RegistrationFactory.make(r -> r.stripeSessionId(sessionId).eventId(eventId).ticketId(ticket.getId()));

    mockCompletedEvent(sessionId, UUID.randomUUID().toString());
    when(registrationsRepository.listAllByStripeSessionId(sessionId)).thenReturn(List.of(registration1, registration2));
    when(ticketsRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
    when(ticketsRepository.findByIdWithLock(ticket.getId())).thenReturn(Optional.of(ticket));
    when(registrationsRepository.countConfirmedByTicketId(ticket.getId())).thenReturn(1);

    processStripeCheckoutEventUseCase.execute("raw", "sig");

    // Both must have currency and price populated before the refund
    assertThat(registration1.getPaidCurrency()).isEqualTo(ticket.getCurrency());
    assertThat(registration1.getTotalPaid()).isEqualTo(ticket.getPrice());
    assertThat(registration2.getPaidCurrency()).isEqualTo(ticket.getCurrency());
    assertThat(registration2.getTotalPaid()).isEqualTo(ticket.getPrice());
  }

  // checkout.session.expired ---------------------------------------------------------------------------------------

  @Test
  void shouldMarkRegistrationsAsExpiredWhenCheckoutExpires() {
    String sessionId = UUID.randomUUID().toString();

    Registration registration1 = RegistrationFactory.make(r -> r.stripeSessionId(sessionId));
    Registration registration2 = RegistrationFactory.make(r -> r.stripeSessionId(sessionId));

    mockExpiredEvent(sessionId);
    when(registrationsRepository.listAllByStripeSessionId(sessionId)).thenReturn(List.of(registration1, registration2));

    processStripeCheckoutEventUseCase.execute("raw", "sig");

    ArgumentCaptor<Registration> captor = ArgumentCaptor.captor();
    verify(registrationsRepository, times(2)).update(captor.capture());

    assertThat(captor.getAllValues()).allMatch(r -> r.getPaymentStatus() == PaymentStatus.EXPIRED);
  }

  // eventType null - early return ----------------------------------------------------------------------------------

  @Test
  void shouldDoNothingWhenEventTypeIsNull() {
    when(stripeService.parseWebhookAndGetSessionData(any(), any())).thenReturn(new StripeCheckoutEventData(null, null, null));

    processStripeCheckoutEventUseCase.execute("raw", "sig");

    verify(registrationsRepository, never()).listAllByStripeSessionId(any());
    verify(registrationsRepository, never()).update(any());
    verify(eventPublisher, never()).publishEvent(any());
  }

  // TicketNotFoundException ----------------------------------------------------------------------------------------

  @Test
  void shouldThrowWhenTicketNotFoundInFirstLoop() {
    String sessionId = UUID.randomUUID().toString();
    UUID ticketId = UUID.randomUUID();

    Registration registration = RegistrationFactory.make(r -> r.stripeSessionId(sessionId).ticketId(ticketId));

    mockCompletedEvent(sessionId, UUID.randomUUID().toString());
    when(registrationsRepository.listAllByStripeSessionId(sessionId)).thenReturn(List.of(registration));
    when(ticketsRepository.findById(ticketId)).thenReturn(Optional.empty());

    assertThrows(TicketNotFoundException.class, () -> processStripeCheckoutEventUseCase.execute("raw", "sig"));

    verify(registrationsRepository, never()).update(any());
  }

  @Test
  void shouldThrowWhenTicketNotFoundInSecondLoop() {
    String sessionId = UUID.randomUUID().toString();
    UUID eventId = UUID.randomUUID();

    Ticket ticket = TicketFactory.make(t -> t.eventId(eventId).capacity(10));

    Registration registration = RegistrationFactory.make(r -> r.stripeSessionId(sessionId).eventId(eventId).ticketId(ticket.getId()));

    mockCompletedEvent(sessionId, UUID.randomUUID().toString());
    when(registrationsRepository.listAllByStripeSessionId(sessionId)).thenReturn(List.of(registration));
    when(ticketsRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));

    // findByIdWithLock returns empty to simulate the ticket being deleted between loops
    when(ticketsRepository.findByIdWithLock(ticket.getId())).thenReturn(Optional.empty());

    assertThrows(TicketNotFoundException.class, () -> processStripeCheckoutEventUseCase.execute("raw", "sig"));

    verify(registrationsRepository, never()).update(any());
  }
}
