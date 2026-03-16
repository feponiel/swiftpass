package com.feponiel.swiftpass.domain.application.usecases;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.feponiel.swiftpass.domain.application.repositories.EventsRepository;
import com.feponiel.swiftpass.domain.application.repositories.TicketsRepository;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.EventNotFoundException;
import com.feponiel.swiftpass.domain.business.entities.Ticket;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListAllTicketsByEventIdUseCase {
  private final TicketsRepository ticketsRepository;
  private final EventsRepository eventsRepository;

  public List<Ticket> execute(UUID eventId) {
    this.eventsRepository.findById(eventId)
      .orElseThrow(EventNotFoundException::new);

    return this.ticketsRepository.listAllByEventId(eventId);
  }
}
