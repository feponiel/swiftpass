package com.feponiel.swiftpass.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.feponiel.swiftpass.domain.application.boundaries.StripeCheckoutEventData;
import com.feponiel.swiftpass.domain.application.repositories.EventsRepository;
import com.feponiel.swiftpass.domain.application.repositories.RegistrationsRepository;
import com.feponiel.swiftpass.domain.application.repositories.TicketsRepository;
import com.feponiel.swiftpass.domain.application.services.StripeService;
import com.feponiel.swiftpass.domain.business.entities.Event;
import com.feponiel.swiftpass.domain.business.entities.Registration;
import com.feponiel.swiftpass.domain.business.entities.Ticket;
import com.feponiel.swiftpass.domain.business.valueobjects.PaymentStatus;
import com.feponiel.swiftpass.factories.EventFactory;
import com.feponiel.swiftpass.factories.RegistrationFactory;
import com.feponiel.swiftpass.factories.TicketFactory;

public class ProcessStripeCheckoutEventTest extends E2ETest {
  @Autowired private EventsRepository eventsRepository;
  @Autowired private TicketsRepository ticketsRepository;
  @Autowired private RegistrationsRepository registrationsRepository;
  @MockitoBean private StripeService stripeService;

  @BeforeEach
  void setup() {
    when(stripeService.parseWebhookAndGetSessionData(any(), any()))
      .thenReturn(new StripeCheckoutEventData(null, null, null));
  }

  // checkout.session.completed - happy path -------------------------------------------------------------------------

  @Test
  void shouldMarkRegistrationsAsPaidWhenCheckoutCompletes() throws Exception {
    String sessionId = UUID.randomUUID().toString();
    String paymentIntentId = UUID.randomUUID().toString();

    Event event = EventFactory.make();
    eventsRepository.create(event);

    Ticket ticket = TicketFactory.make(t -> t.eventId(event.getId()).capacity(10));
    ticketsRepository.create(ticket);

    Registration registration1 = RegistrationFactory.make(r -> r
      .eventId(event.getId())
      .ticketId(ticket.getId())
      .stripeSessionId(sessionId));
    Registration registration2 = RegistrationFactory.make(r -> r
      .eventId(event.getId())
      .ticketId(ticket.getId())
      .stripeSessionId(sessionId));

    registrationsRepository.create(registration1);
    registrationsRepository.create(registration2);

    when(stripeService.parseWebhookAndGetSessionData(any(), any()))
      .thenReturn(new StripeCheckoutEventData("checkout.session.completed", sessionId, paymentIntentId));

    mockMvc.perform(post("/webhooks/stripe")
      .contentType(MediaType.APPLICATION_JSON)
      .header("Stripe-Signature", "test-signature")
      .content("{}"))
      .andExpect(status().isOk());

    Registration updated1 = registrationsRepository.findById(registration1.getId()).get();
    Registration updated2 = registrationsRepository.findById(registration2.getId()).get();

    assertThat(updated1.getPaymentStatus()).isEqualTo(PaymentStatus.PAID);
    assertThat(updated2.getPaymentStatus()).isEqualTo(PaymentStatus.PAID);
  }

  // checkout.session.completed - overbooked ------------------------------------------------------------------------

  @Test
  void shouldMarkRegistrationsAsRefundedWhenTicketIsOverbooked() throws Exception {
    String sessionId = UUID.randomUUID().toString();
    String paymentIntentId = UUID.randomUUID().toString();

    Event event = EventFactory.make();
    eventsRepository.create(event);

    // capacity = 1, already confirmed = 1 → remaining = 0 → overbooked
    Ticket ticket = TicketFactory.make(t -> t.eventId(event.getId()).capacity(1));
    ticketsRepository.create(ticket);

    Registration existingPaid = RegistrationFactory.make(r -> r
      .eventId(event.getId())
      .ticketId(ticket.getId())
      .paymentStatus(PaymentStatus.PAID));
    registrationsRepository.create(existingPaid);

    Registration registration = RegistrationFactory.make(r -> r
      .eventId(event.getId())
      .ticketId(ticket.getId())
      .stripeSessionId(sessionId));
    registrationsRepository.create(registration);

    when(stripeService.parseWebhookAndGetSessionData(any(), any()))
      .thenReturn(new StripeCheckoutEventData("checkout.session.completed", sessionId, paymentIntentId));

    mockMvc.perform(post("/webhooks/stripe")
      .contentType(MediaType.APPLICATION_JSON)
      .header("Stripe-Signature", "test-signature")
      .content("{}"))
      .andExpect(status().isOk());

    Registration updated = registrationsRepository.findById(registration.getId()).get();
    assertThat(updated.getPaymentStatus()).isEqualTo(PaymentStatus.REFUNDED);
  }

  // checkout.session.expired ---------------------------------------------------------------------------------------

  @Test
  void shouldMarkRegistrationsAsExpiredWhenCheckoutExpires() throws Exception {
    String sessionId = UUID.randomUUID().toString();

    Event event = EventFactory.make();
    eventsRepository.create(event);

    Ticket ticket = TicketFactory.make(t -> t.eventId(event.getId()));
    ticketsRepository.create(ticket);

    Registration registration1 = RegistrationFactory.make(r -> r
      .eventId(event.getId())
      .ticketId(ticket.getId())
      .stripeSessionId(sessionId));
    Registration registration2 = RegistrationFactory.make(r -> r
      .eventId(event.getId())
      .ticketId(ticket.getId())
      .stripeSessionId(sessionId));

    registrationsRepository.create(registration1);
    registrationsRepository.create(registration2);

    when(stripeService.parseWebhookAndGetSessionData(any(), any()))
      .thenReturn(new StripeCheckoutEventData("checkout.session.expired", sessionId, null));

    mockMvc.perform(post("/webhooks/stripe")
      .contentType(MediaType.APPLICATION_JSON)
      .header("Stripe-Signature", "test-signature")
      .content("{}"))
      .andExpect(status().isOk());

    Registration updated1 = registrationsRepository.findById(registration1.getId()).get();
    Registration updated2 = registrationsRepository.findById(registration2.getId()).get();

    assertThat(updated1.getPaymentStatus()).isEqualTo(PaymentStatus.EXPIRED);
    assertThat(updated2.getPaymentStatus()).isEqualTo(PaymentStatus.EXPIRED);
  }

  // eventType null - early return ----------------------------------------------------------------------------------

  @Test
  void shouldDoNothingWhenEventTypeIsNull() throws Exception {
    when(stripeService.parseWebhookAndGetSessionData(any(), any()))
      .thenReturn(new StripeCheckoutEventData(null, null, null));

    mockMvc.perform(post("/webhooks/stripe")
      .contentType(MediaType.APPLICATION_JSON)
      .header("Stripe-Signature", "test-signature")
      .content("{}"))
      .andExpect(status().isOk());
  }
}
