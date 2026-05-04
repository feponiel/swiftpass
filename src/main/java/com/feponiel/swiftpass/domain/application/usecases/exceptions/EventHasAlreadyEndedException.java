package com.feponiel.swiftpass.domain.application.usecases.exceptions;

public class EventHasAlreadyEndedException extends DomainException {
  public EventHasAlreadyEndedException() {
    super("Registration is not allowed for events that have already ended!", DomainExceptionCode.EVENT_ALREADY_ENDED);
  }
}
