package com.feponiel.swiftpass.domain.application.usecases;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.feponiel.swiftpass.domain.application.repositories.EventsRepository;
import com.feponiel.swiftpass.domain.application.repositories.TicketsRepository;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.EventNotFoundException;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.InvalidCurrencyException;
import com.feponiel.swiftpass.domain.business.entities.Ticket;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateTicketUseCase {
  private final TicketsRepository ticketsRepository;
  private final EventsRepository eventsRepository;

  public Ticket execute(UUID eventId, String name, String description, BigDecimal price, String currency, Integer amountAvailable) {
    this.eventsRepository.findById(eventId)
      .orElseThrow(EventNotFoundException::new);

    try {
      Currency.getInstance(currency);
    } catch (Exception _) {
      throw new InvalidCurrencyException();
    }

    Ticket ticket = Ticket.builder()
      .id(UUID.randomUUID())
      .eventId(eventId)
      .name(name)
      .description(description)
      .price(price)
      .currency(currency)
      .amountAvailable(amountAvailable)
      .build();

    this.ticketsRepository.create(ticket);

    return ticket;
  }
}
