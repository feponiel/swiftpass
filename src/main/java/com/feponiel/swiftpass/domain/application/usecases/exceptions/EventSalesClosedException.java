package com.feponiel.swiftpass.domain.application.usecases.exceptions;

public class EventSalesClosedException extends DomainException {
  public EventSalesClosedException() {
    super("Registration is not allowed for events with sales closed!", DomainExceptionCode.EVENT_SALES_CLOSED);
  }
}
