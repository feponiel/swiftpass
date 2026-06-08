package com.feponiel.swiftpass.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.feponiel.swiftpass.domain.application.repositories.EventsRepository;
import com.feponiel.swiftpass.domain.application.repositories.UsersRepository;
import com.feponiel.swiftpass.domain.business.entities.User;
import com.feponiel.swiftpass.factories.UserFactory;

public class CreateEventTest extends E2ETest {
  @Autowired private EventsRepository eventsRepository;
  @Autowired private UsersRepository usersRepository;

  private User authenticatedUser;

  @BeforeEach
  void setup() {
    authenticatedUser = UserFactory.make();
    usersRepository.create(authenticatedUser);
  }

  @Test
  void shouldCreateEvent() throws Exception {
    mockMvc.perform(post("/events")
      .with(oauth2Login()
        .attributes(attrs -> attrs.putAll(Map.of("id", authenticatedUser.getId())))
        .authorities(new SimpleGrantedAuthority("ROLE_ORGANIZER")))
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
        {
          "name": "Rock in Rio",
          "description": "The biggest music festival in the world happening in Brazil",
          "ageRange": 18,
          "salesOpen": true,
          "address": {
            "postalCode": "22430-010",
            "country": "Brazil",
            "state": "Rio de Janeiro",
            "city": "Rio de Janeiro",
            "addressLine1": "Av. Embaixador Abelardo Bueno, 3401"
          },
          "startDate": "2030-01-01T00:00:00Z",
          "endDate": "2030-01-10T00:00:00Z"
        }
      """))
      .andExpect(status().isCreated());

    assertThat(eventsRepository.listAll()).hasSize(1);
  }

  @Test
  void shouldReturnValidationErrorWhenRequiredFieldsAreMissing() throws Exception {
    mockMvc.perform(post("/events")
      .with(oauth2Login()
        .attributes(attrs -> attrs.putAll(Map.of("id", authenticatedUser.getId())))
        .authorities(new SimpleGrantedAuthority("ROLE_ORGANIZER")))
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
        {
          "description": "Missing required fields"
        }
      """))
      .andExpect(status().is(422));
  }

  @Test
  void shouldReturnForbiddenWhenUserIsNotOrganizer() throws Exception {
    mockMvc.perform(post("/events")
      .with(oauth2Login()
        .attributes(attrs -> attrs.putAll(Map.of("id", authenticatedUser.getId())))
        .authorities(new SimpleGrantedAuthority("ROLE_ATTENDEE")))
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
        {
          "name": "Rock in Rio",
          "description": "The biggest music festival in the world happening in Brazil",
          "ageRange": 18,
          "salesOpen": true,
          "address": {
            "postalCode": "22430-010",
            "country": "Brazil",
            "state": "Rio de Janeiro",
            "city": "Rio de Janeiro",
            "addressLine1": "Av. Embaixador Abelardo Bueno, 3401"
          },
          "startDate": "2030-01-01T00:00:00Z",
          "endDate": "2030-01-10T00:00:00Z"
        }
      """))
      .andExpect(status().isForbidden());

    assertThat(eventsRepository.listAll()).isEmpty();
  }

  @Test
  void shouldReturnUnauthorizedWhenUserIsNotAuthenticated() throws Exception {
    mockMvc.perform(post("/events")
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
        {
          "name": "Rock in Rio",
          "description": "The biggest music festival in the world happening in Brazil",
          "ageRange": 18,
          "salesOpen": true,
          "address": {
            "postalCode": "22430-010",
            "country": "Brazil",
            "state": "Rio de Janeiro",
            "city": "Rio de Janeiro",
            "addressLine1": "Av. Embaixador Abelardo Bueno, 3401"
          },
          "startDate": "2030-01-01T00:00:00Z",
          "endDate": "2030-01-10T00:00:00Z"
        }
      """))
      .andExpect(status().isUnauthorized());
  }
}