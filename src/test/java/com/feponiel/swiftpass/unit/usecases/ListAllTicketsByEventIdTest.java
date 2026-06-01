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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.feponiel.swiftpass.domain.application.repositories.EventsRepository;
import com.feponiel.swiftpass.domain.application.repositories.TicketsRepository;
import com.feponiel.swiftpass.domain.application.usecases.ListAllTicketsByEventIdUseCase;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.EventNotFoundException;
import com.feponiel.swiftpass.domain.business.entities.Event;
import com.feponiel.swiftpass.domain.business.entities.Ticket;
import com.feponiel.swiftpass.factories.EventFactory;
import com.feponiel.swiftpass.factories.TicketFactory;

public class ListAllTicketsByEventIdTest {
  @Mock private TicketsRepository ticketsRepository;
  @Mock private EventsRepository eventsRepository;

  @InjectMocks
  private ListAllTicketsByEventIdUseCase listAllTicketsByEventIdUseCase;

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
  void shouldListAllTicketsByEventId() {
    Event event = EventFactory.make();

    Ticket ticket1 = TicketFactory.make(t -> t.eventId(event.getId()));
    Ticket ticket2 = TicketFactory.make(t -> t.eventId(event.getId()));

    List<Ticket> tickets = List.of(ticket1, ticket2);

    when(eventsRepository.findById(event.getId())).thenReturn(Optional.of(event));
    when(ticketsRepository.listAllByEventId(event.getId())).thenReturn(tickets);

    var result = listAllTicketsByEventIdUseCase.execute(event.getId());

    verify(eventsRepository, times(1)).findById(any());
    verify(ticketsRepository, times(1)).listAllByEventId(any());

    assertThat(result).isEqualTo(tickets);
  }

  @Test
  void shouldReturnEmptyListWhenEventHasNoTickets() {
      Event event = EventFactory.make();

      when(eventsRepository.findById(event.getId())).thenReturn(Optional.of(event));
      when(ticketsRepository.listAllByEventId(event.getId())).thenReturn(List.of());

      var result = listAllTicketsByEventIdUseCase.execute(event.getId());

      assertThat(result).isEmpty();
  }

  @Test
  void shouldNotListTicketsWhenEventDoesNotExist() {
    UUID randomId = UUID.randomUUID();

    when(eventsRepository.findById(randomId)).thenReturn(Optional.empty());

    assertThrows(EventNotFoundException.class, () -> listAllTicketsByEventIdUseCase.execute(randomId));

    verify(eventsRepository, times(1)).findById(any());
    verify(ticketsRepository, never()).listAllByEventId(any());
  }
}
