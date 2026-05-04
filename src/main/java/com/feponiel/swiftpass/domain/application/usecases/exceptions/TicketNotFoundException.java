package com.feponiel.swiftpass.domain.application.usecases.exceptions;

public class TicketNotFoundException extends DomainException {
  public TicketNotFoundException() {
    super("Ticket not found!", DomainExceptionCode.TICKET_NOT_FOUND);
  }
}
