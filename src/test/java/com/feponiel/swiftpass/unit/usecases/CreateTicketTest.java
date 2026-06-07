package com.feponiel.swiftpass.unit.usecases;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.feponiel.swiftpass.domain.application.repositories.EventsRepository;
import com.feponiel.swiftpass.domain.application.repositories.TicketsRepository;
import com.feponiel.swiftpass.domain.application.usecases.CreateTicketUseCase;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.EventNotFoundException;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.InvalidCurrencyException;
import com.feponiel.swiftpass.domain.business.entities.Event;
import com.feponiel.swiftpass.domain.business.entities.Ticket;
import com.feponiel.swiftpass.factories.EventFactory;
import com.feponiel.swiftpass.factories.TicketFactory;
import com.feponiel.swiftpass.unit.UnitTest;

public class CreateTicketTest extends UnitTest {
  @Mock private TicketsRepository ticketsRepository;
  @Mock private EventsRepository eventsRepository;

  @InjectMocks
  private CreateTicketUseCase createTicketUseCase;

  @Test
  void shouldCreateTicket() {
    Event event = EventFactory.make();
    Ticket ticketTemplate = TicketFactory.make(t -> t.eventId(event.getId()));

    when(eventsRepository.findById(event.getId())).thenReturn(Optional.of(event));

    Ticket newTicket = createTicketUseCase.execute(
      ticketTemplate.getEventId(),
      ticketTemplate.getName(),
      ticketTemplate.getDescription(),
      ticketTemplate.getPrice(),
      ticketTemplate.getCurrency(),
      ticketTemplate.getCapacity()
    );

    verify(ticketsRepository, times(1)).create(newTicket);
  }

  @Test
  void shouldNotCreateTicketWhenEventDoesNotExist() {
    Ticket ticket = TicketFactory.make();

    when(eventsRepository.findById(ticket.getEventId())).thenReturn(Optional.empty());

    assertThrows(EventNotFoundException.class, () -> createTicketUseCase.execute(
      ticket.getEventId(),
      ticket.getName(),
      ticket.getDescription(),
      ticket.getPrice(),
      ticket.getCurrency(),
      ticket.getCapacity()
    ));

    verify(ticketsRepository, never()).create(any());
  }

  @Test
  void shouldNotCreateTicketWhenCurrencyIsInvalid() {
    Event event = EventFactory.make();
    Ticket ticketWithInvalidCurrency = TicketFactory.make(t -> t.eventId(event.getId()).currency("ZZZ"));

    when(eventsRepository.findById(event.getId())).thenReturn(Optional.of(event));

    assertThrows(InvalidCurrencyException.class, () -> createTicketUseCase.execute(
      ticketWithInvalidCurrency.getEventId(),
      ticketWithInvalidCurrency.getName(),
      ticketWithInvalidCurrency.getDescription(),
      ticketWithInvalidCurrency.getPrice(),
      ticketWithInvalidCurrency.getCurrency(),
      ticketWithInvalidCurrency.getCapacity()
    ));

    verify(ticketsRepository, never()).create(any());
  }
}
