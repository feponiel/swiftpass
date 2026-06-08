package com.feponiel.swiftpass.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

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

public class RefundRegistrationTest extends E2ETest {
  @Autowired private EventsRepository eventsRepository;
  @Autowired private TicketsRepository ticketsRepository;
  @Autowired private RegistrationsRepository registrationsRepository;
  @MockitoBean private StripeService stripeService;

  @Test
  void shouldRefundRegistration() throws Exception {
    Event event = EventFactory.make();
    eventsRepository.create(event);

    Ticket ticket = TicketFactory.make(t -> t.eventId(event.getId()));
    ticketsRepository.create(ticket);

    Registration registration = RegistrationFactory.make(r -> r
      .eventId(event.getId())
      .ticketId(ticket.getId())
      .paymentStatus(PaymentStatus.PAID));
    registrationsRepository.create(registration);

    doNothing().when(stripeService).processPartialRefund(any(), any());

    mockMvc.perform(post("/registrations/{registrationId}/refund", registration.getId())
      .with(oauth2Login().authorities(new SimpleGrantedAuthority("ROLE_ORGANIZER"))))
      .andExpect(status().isNoContent());

    Registration updated = registrationsRepository.findById(registration.getId()).get();
    assertThat(updated.getPaymentStatus()).isEqualTo(PaymentStatus.REFUNDED);
  }

  @Test
  void shouldReturnErrorWhenRegistrationIsNotPaid() throws Exception {
    Event event = EventFactory.make();
    eventsRepository.create(event);

    Ticket ticket = TicketFactory.make(t -> t.eventId(event.getId()));
    ticketsRepository.create(ticket);

    Registration registration = RegistrationFactory.make(r -> r
      .eventId(event.getId())
      .ticketId(ticket.getId())
      .paymentStatus(PaymentStatus.PENDING));
    registrationsRepository.create(registration);

    mockMvc.perform(post("/registrations/{registrationId}/refund", registration.getId())
      .with(oauth2Login().authorities(new SimpleGrantedAuthority("ROLE_ORGANIZER"))))
      .andExpect(status().isConflict());
  }

  @Test
  void shouldReturnNotFoundWhenRegistrationDoesNotExist() throws Exception {
    mockMvc.perform(post("/registrations/{registrationId}/refund", UUID.randomUUID())
      .with(oauth2Login().authorities(new SimpleGrantedAuthority("ROLE_ORGANIZER"))))
      .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnForbiddenWhenUserIsNotOrganizer() throws Exception {
    Event event = EventFactory.make();
    eventsRepository.create(event);

    Ticket ticket = TicketFactory.make(t -> t.eventId(event.getId()));
    ticketsRepository.create(ticket);

    Registration registration = RegistrationFactory.make(r -> r
      .eventId(event.getId())
      .ticketId(ticket.getId())
      .paymentStatus(PaymentStatus.PAID));
    registrationsRepository.create(registration);

    mockMvc.perform(post("/registrations/{registrationId}/refund", registration.getId())
      .with(oauth2Login().authorities(new SimpleGrantedAuthority("ROLE_ATTENDEE"))))
      .andExpect(status().isForbidden());

    Registration notUpdated = registrationsRepository.findById(registration.getId()).get();
    assertThat(notUpdated.getPaymentStatus()).isEqualTo(PaymentStatus.PAID);
  }

  @Test
  void shouldReturnUnauthorizedWhenUserIsNotAuthenticated() throws Exception {
    mockMvc.perform(post("/registrations/{registrationId}/refund", UUID.randomUUID()))
      .andExpect(status().isUnauthorized());
  }
}
