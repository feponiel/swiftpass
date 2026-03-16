package com.feponiel.swiftpass.domain.application.usecases.exceptions;

public class TicketNotFoundException extends RuntimeException {
  public TicketNotFoundException() {
    super("Ticket not found!");
  }
}
