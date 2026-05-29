package com.feponiel.swiftpass.unit.usecases;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.feponiel.swiftpass.domain.application.boundaries.CheckoutSessionData;
import com.feponiel.swiftpass.domain.application.boundaries.RegistrationItemData;
import com.feponiel.swiftpass.domain.application.repositories.EventsRepository;
import com.feponiel.swiftpass.domain.application.repositories.RegistrationsRepository;
import com.feponiel.swiftpass.domain.application.repositories.TicketsRepository;
import com.feponiel.swiftpass.domain.application.services.StripeService;
import com.feponiel.swiftpass.domain.application.usecases.CreateRegistrationsUseCase;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.EventHasAlreadyEndedException;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.EventNotFoundException;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.EventSalesClosedException;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.TicketNotFoundException;
import com.feponiel.swiftpass.domain.business.entities.Event;
import com.feponiel.swiftpass.domain.business.entities.Registration;
import com.feponiel.swiftpass.domain.business.entities.Ticket;
import com.feponiel.swiftpass.factories.EventFactory;
import com.feponiel.swiftpass.factories.TicketFactory;

class CreateRegistrationsTest {
  @Mock private RegistrationsRepository registrationsRepository;
  @Mock private TicketsRepository ticketsRepository;
  @Mock private EventsRepository eventsRepository;
  @Mock private StripeService stripeService;

  @InjectMocks
  private CreateRegistrationsUseCase createRegistrationsUseCase;

  private AutoCloseable mocks;

  @BeforeEach
  void setup() {
    mocks = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }

  @Test
  void shouldCreateRegistrations() {
    Event event = EventFactory.make();
    Ticket ticket = TicketFactory.make(t -> t.eventId(event.getId()));
    RegistrationItemData item = new RegistrationItemData(ticket.getId(), "John Doe");

    when(ticketsRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
    when(eventsRepository.findById(event.getId())).thenReturn(Optional.of(event));
    when(stripeService.createCheckoutSession(any())).thenReturn(new CheckoutSessionData("session_123", "https://checkout.stripe.com/123"));

    createRegistrationsUseCase.execute(UUID.randomUUID(), List.of(item));

    verify(registrationsRepository, times(1)).create(any(Registration.class));
    verify(registrationsRepository, times(1)).update(any(Registration.class));
  }

  @Test
  void shouldNotCreateRegistrationsWhenTicketDoesNotExist() {
    RegistrationItemData item = new RegistrationItemData(UUID.randomUUID(), "John Doe");

    when(ticketsRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(TicketNotFoundException.class, () ->
      createRegistrationsUseCase.execute(UUID.randomUUID(), List.of(item))
    );

    verify(registrationsRepository, never()).create(any());
  }

  @Test
  void shouldNotCreateRegistrationsWhenEventDoesNotExist() {
    Ticket ticket = TicketFactory.make();
    RegistrationItemData item = new RegistrationItemData(ticket.getId(), "John Doe");

    when(ticketsRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
    when(eventsRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(EventNotFoundException.class, () ->
      createRegistrationsUseCase.execute(UUID.randomUUID(), List.of(item))
    );

    verify(registrationsRepository, never()).create(any());
  }

  @Test
  void shouldNotCreateRegistrationsWhenEventHasAlreadyEnded() {
    Event event = EventFactory.make(e -> e
      .startDate(Instant.now().minusSeconds(60 * 60 * 5))  // 5 hours ago
      .endDate(Instant.now().minusSeconds(60 * 60 * 2))    // 2 hours ago
    );
    
    Ticket ticket = TicketFactory.make(t -> t.eventId(event.getId()));
    RegistrationItemData item = new RegistrationItemData(ticket.getId(), "John Doe");

    when(ticketsRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
    when(eventsRepository.findById(event.getId())).thenReturn(Optional.of(event));

    assertThrows(EventHasAlreadyEndedException.class, () ->
      createRegistrationsUseCase.execute(UUID.randomUUID(), List.of(item))
    );

    verify(registrationsRepository, never()).create(any());
  }

  @Test
  void shouldNotCreateRegistrationsWhenEventSalesAreClosed() {
    Event event = EventFactory.make(e -> e.salesOpen(false));
    Ticket ticket = TicketFactory.make(t -> t.eventId(event.getId()));
    RegistrationItemData item = new RegistrationItemData(ticket.getId(), "John Doe");

    when(ticketsRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
    when(eventsRepository.findById(event.getId())).thenReturn(Optional.of(event));

    assertThrows(EventSalesClosedException.class, () ->
      createRegistrationsUseCase.execute(UUID.randomUUID(), List.of(item))
    );

    verify(registrationsRepository, never()).create(any());
  }
}