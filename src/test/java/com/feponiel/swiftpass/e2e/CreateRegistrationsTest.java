package com.feponiel.swiftpass.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.feponiel.swiftpass.domain.application.boundaries.CheckoutSessionData;
import com.feponiel.swiftpass.domain.application.repositories.EventsRepository;
import com.feponiel.swiftpass.domain.application.repositories.RegistrationsRepository;
import com.feponiel.swiftpass.domain.application.repositories.TicketsRepository;
import com.feponiel.swiftpass.domain.application.repositories.UsersRepository;
import com.feponiel.swiftpass.domain.application.services.StripeService;
import com.feponiel.swiftpass.domain.business.entities.Event;
import com.feponiel.swiftpass.domain.business.entities.Registration;
import com.feponiel.swiftpass.domain.business.entities.Ticket;
import com.feponiel.swiftpass.domain.business.entities.User;
import com.feponiel.swiftpass.domain.business.valueobjects.PaymentStatus;
import com.feponiel.swiftpass.factories.EventFactory;
import com.feponiel.swiftpass.factories.TicketFactory;
import com.feponiel.swiftpass.factories.UserFactory;

public class CreateRegistrationsTest extends E2ETest {
  @Autowired private UsersRepository usersRepository;
  @Autowired private EventsRepository eventsRepository;
  @Autowired private TicketsRepository ticketsRepository;
  @Autowired private RegistrationsRepository registrationsRepository;
  @MockitoBean private StripeService stripeService;

  private User authenticatedUser;

  @BeforeEach
  void setup() {
    authenticatedUser = UserFactory.make();
    usersRepository.create(authenticatedUser);

    when(stripeService.createCheckoutSession(any()))
      .thenReturn(new CheckoutSessionData(
        UUID.randomUUID().toString(),
        "https://checkout.stripe.com/test-session"
      ));
  }

  @Test
  void shouldCreateRegistrationsAndReturnCheckoutUrl() throws Exception {
    Event event = EventFactory.make();
    eventsRepository.create(event);

    Ticket ticket = TicketFactory.make(t -> t.eventId(event.getId()));
    ticketsRepository.create(ticket);

    mockMvc.perform(post("/registrations")
      .with(oauth2Login().attributes(attrs -> attrs.putAll(Map.of("id", authenticatedUser.getId()))))
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
        {
          "registrationItems": [
            {
              "ticketId": "%s",
              "holderName": "John Doe"
            }
          ]
        }
      """.formatted(ticket.getId())))
      .andExpect(status().isCreated());

    List<Registration> registrations = registrationsRepository.listAllByRegistrantId(authenticatedUser.getId());
    assertThat(registrations).hasSize(1);
    assertThat(registrations.get(0).getPaymentStatus()).isEqualTo(PaymentStatus.PENDING);
    assertThat(registrations.get(0).getCheckoutUrl()).isNotNull();
  }

  @Test
  void shouldReturnNotFoundWhenTicketDoesNotExist() throws Exception {
    mockMvc.perform(post("/registrations")
      .with(oauth2Login().attributes(attrs -> attrs.putAll(Map.of("id", authenticatedUser.getId()))))
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
        {
          "registrationItems": [
            {
              "ticketId": "%s",
              "holderName": "John Doe"
            }
          ]
        }
      """.formatted(UUID.randomUUID())))
      .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnErrorWhenEventSalesAreClosed() throws Exception {
    Event event = EventFactory.make(e -> e.salesOpen(false));
    eventsRepository.create(event);

    Ticket ticket = TicketFactory.make(t -> t.eventId(event.getId()));
    ticketsRepository.create(ticket);

    mockMvc.perform(post("/registrations")
      .with(oauth2Login().attributes(attrs -> attrs.putAll(Map.of("id", authenticatedUser.getId()))))
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
        {
          "registrationItems": [
            {
              "ticketId": "%s",
              "holderName": "John Doe"
            }
          ]
        }
      """.formatted(ticket.getId())))
      .andExpect(status().isConflict());
  }

  @Test
  void shouldReturnUnauthorizedWhenUserIsNotAuthenticated() throws Exception {
    mockMvc.perform(post("/registrations")
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
        {
          "registrationItems": [
            {
              "ticketId": "%s",
              "holderName": "John Doe"
            }
          ]
        }
      """.formatted(UUID.randomUUID())))
      .andExpect(status().isUnauthorized());
  }
}
