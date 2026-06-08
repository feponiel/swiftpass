package com.feponiel.swiftpass.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.feponiel.swiftpass.domain.application.repositories.EventsRepository;
import com.feponiel.swiftpass.domain.application.repositories.RegistrationsRepository;
import com.feponiel.swiftpass.domain.application.services.StripeService;
import com.feponiel.swiftpass.domain.business.entities.Event;
import com.feponiel.swiftpass.domain.business.entities.Registration;
import com.feponiel.swiftpass.domain.business.valueobjects.EventStatus;
import com.feponiel.swiftpass.domain.business.valueobjects.PaymentStatus;
import com.feponiel.swiftpass.factories.EventFactory;
import com.feponiel.swiftpass.factories.RegistrationFactory;

public class CancelEventTest extends E2ETest {
  @Autowired private EventsRepository eventsRepository;
  @Autowired private RegistrationsRepository registrationsRepository;
  @MockitoBean private StripeService stripeService;

  @BeforeEach
  void setup() {
    when(stripeService.expireSessionsInBatch(any())).thenReturn(CompletableFuture.completedFuture(null));
    when(stripeService.processRefundsInBatch(any())).thenReturn(CompletableFuture.completedFuture(null));
  }

  @Test
  void shouldCancelEvent() throws Exception {
    Event event = EventFactory.make();
    eventsRepository.create(event);

    mockMvc.perform(post("/events/{eventId}/cancel", event.getId())
      .with(oauth2Login().authorities(new SimpleGrantedAuthority("ROLE_ORGANIZER"))))
      .andExpect(status().isOk());

    Event canceledEvent = eventsRepository.findById(event.getId()).get();
    assertThat(canceledEvent.getStatus()).isEqualTo(EventStatus.CANCELED);
    assertThat(canceledEvent.getSalesOpen()).isFalse();
  }

  @Test
  void shouldMarkPaidRegistrationsAsRefundedWhenEventIsCanceled() throws Exception {
    Event event = EventFactory.make();
    eventsRepository.create(event);

    Registration paid1 = RegistrationFactory.make(r -> r
      .eventId(event.getId())
      .paymentStatus(PaymentStatus.PAID));
    Registration paid2 = RegistrationFactory.make(r -> r
      .eventId(event.getId())
      .paymentStatus(PaymentStatus.PAID));

    registrationsRepository.create(paid1);
    registrationsRepository.create(paid2);

    mockMvc.perform(post("/events/{eventId}/cancel", event.getId())
      .with(oauth2Login().authorities(new SimpleGrantedAuthority("ROLE_ORGANIZER"))))
      .andExpect(status().isOk());

    Registration updatedPaid1 = registrationsRepository.findById(paid1.getId()).get();
    Registration updatedPaid2 = registrationsRepository.findById(paid2.getId()).get();

    assertThat(updatedPaid1.getPaymentStatus()).isEqualTo(PaymentStatus.REFUNDED);
    assertThat(updatedPaid2.getPaymentStatus()).isEqualTo(PaymentStatus.REFUNDED);
  }

  @Test
  void shouldReturnNotFoundWhenEventDoesNotExist() throws Exception {
    mockMvc.perform(post("/events/{eventId}/cancel", UUID.randomUUID())
      .with(oauth2Login().authorities(new SimpleGrantedAuthority("ROLE_ORGANIZER"))))
      .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnForbiddenWhenUserIsNotOrganizer() throws Exception {
    Event event = EventFactory.make();
    eventsRepository.create(event);

    mockMvc.perform(post("/events/{eventId}/cancel", event.getId())
      .with(oauth2Login().authorities(new SimpleGrantedAuthority("ROLE_ATTENDEE"))))
      .andExpect(status().isForbidden());

    Event notCanceledEvent = eventsRepository.findById(event.getId()).get();
    assertThat(notCanceledEvent.getStatus()).isNotEqualTo(EventStatus.CANCELED);
  }

  @Test
  void shouldReturnUnauthorizedWhenUserIsNotAuthenticated() throws Exception {
    Event event = EventFactory.make();
    eventsRepository.create(event);

    mockMvc.perform(post("/events/{eventId}/cancel", event.getId()))
      .andExpect(status().isUnauthorized());
  }
}
