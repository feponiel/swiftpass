package com.feponiel.swiftpass.domain.application.usecases.exceptions;

public class EventHasAlreadyEndedException extends RuntimeException {
  public EventHasAlreadyEndedException() {
    super("Registration is not allowed for events that have already ended!");
  }
}
