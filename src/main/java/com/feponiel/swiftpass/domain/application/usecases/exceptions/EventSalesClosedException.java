package com.feponiel.swiftpass.domain.application.usecases.exceptions;

public class EventSalesClosedException extends RuntimeException {
  public EventSalesClosedException() {
    super("Registration is not allowed for events with sales closed!");
  }
}
