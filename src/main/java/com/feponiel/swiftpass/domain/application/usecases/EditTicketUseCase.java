package com.feponiel.swiftpass.domain.application.usecases;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.feponiel.swiftpass.domain.application.repositories.TicketsRepository;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.InvalidCurrencyException;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.NoFieldProvidedToEditException;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.TicketNotFoundException;
import com.feponiel.swiftpass.domain.business.entities.Ticket;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EditTicketUseCase {
  private final TicketsRepository ticketsRepository;

  public void execute(
    UUID ticketId,
    String name,
    String description,
    BigDecimal price,
    String currency,
    Integer amountAvailable
  ) {
    if (
      name == null &&
      description == null &&
      price == null &&
      currency == null &&
      amountAvailable == null
    ) {
      throw new NoFieldProvidedToEditException();
    }

    if (currency != null) {
      try {
        Currency.getInstance(currency);
      } catch (Exception _) {
        throw new InvalidCurrencyException();
      }
    }

    Ticket ticket = this.ticketsRepository.findById(ticketId)
      .orElseThrow(TicketNotFoundException::new);

    ticket
      .changeName(name)
      .changeDescription(description)
      .changePrice(price)
      .changeCurrency(currency)
      .changeAmountAvailable(amountAvailable);

    this.ticketsRepository.update(ticket);
  }
}
