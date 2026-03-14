package com.feponiel.swiftpass.domain.application.usecases.exceptions;

public class EventNotFoundException extends RuntimeException {
  public EventNotFoundException() {
    super("Event not found!");
  }
}
