package com.feponiel.swiftpass.domain.application.usecases.exceptions;

public class EventNotFoundException extends DomainException {
  public EventNotFoundException() {
    super("Event not found!", DomainExceptionCode.EVENT_NOT_FOUND);
  }
}
