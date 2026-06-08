package com.feponiel.swiftpass.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.feponiel.swiftpass.domain.application.repositories.EventsRepository;
import com.feponiel.swiftpass.domain.application.repositories.TicketsRepository;
import com.feponiel.swiftpass.domain.business.entities.Event;
import com.feponiel.swiftpass.factories.EventFactory;

public class CreateTicketTest extends E2ETest {
  @Autowired private EventsRepository eventsRepository;
  @Autowired private TicketsRepository ticketsRepository;

  @Test
  void shouldCreateTicket() throws Exception {
    Event event = EventFactory.make();
    eventsRepository.create(event);

    mockMvc.perform(post("/events/{eventId}/tickets", event.getId())
      .with(oauth2Login().authorities(new SimpleGrantedAuthority("ROLE_ORGANIZER")))
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
        {
          "name": "VIP",
          "description": "VIP ticket",
          "price": 99.99,
          "currency": "BRL",
          "capacity": 100
        }
      """))
      .andExpect(status().isCreated());

    assertThat(ticketsRepository.listAllByEventId(event.getId())).hasSize(1);
  }

  @Test
  void shouldReturnNotFoundWhenEventDoesNotExist() throws Exception {
    mockMvc.perform(post("/events/{eventId}/tickets", UUID.randomUUID())
      .with(oauth2Login().authorities(new SimpleGrantedAuthority("ROLE_ORGANIZER")))
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
        {
          "name": "VIP",
          "description": "VIP ticket",
          "price": 99.99,
          "currency": "BRL",
          "capacity": 100
        }
      """))
      .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnErrorWhenCurrencyIsInvalid() throws Exception {
    Event event = EventFactory.make();
    eventsRepository.create(event);

    mockMvc.perform(post("/events/{eventId}/tickets", event.getId())
      .with(oauth2Login().authorities(new SimpleGrantedAuthority("ROLE_ORGANIZER")))
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
        {
          "name": "VIP",
          "description": "VIP ticket",
          "price": 99.99,
          "currency": "INVALID",
          "capacity": 100
        }
      """))
      .andExpect(status().is(422));
  }

  @Test
  void shouldReturnForbiddenWhenUserIsNotOrganizer() throws Exception {
    Event event = EventFactory.make();
    eventsRepository.create(event);

    mockMvc.perform(post("/events/{eventId}/tickets", event.getId())
      .with(oauth2Login().authorities(new SimpleGrantedAuthority("ROLE_ATTENDEE")))
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
        {
          "name": "VIP",
          "description": "VIP ticket",
          "price": 99.99,
          "currency": "BRL",
          "capacity": 100
        }
      """))
      .andExpect(status().isForbidden());

    assertThat(ticketsRepository.listAllByEventId(event.getId())).isEmpty();
  }

  @Test
  void shouldReturnUnauthorizedWhenUserIsNotAuthenticated() throws Exception {
    Event event = EventFactory.make();
    eventsRepository.create(event);

    mockMvc.perform(post("/events/{eventId}/tickets", event.getId())
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
        {
          "name": "VIP",
          "description": "VIP ticket",
          "price": 99.99,
          "currency": "BRL",
          "capacity": 100
        }
      """))
      .andExpect(status().isUnauthorized());
  }
}