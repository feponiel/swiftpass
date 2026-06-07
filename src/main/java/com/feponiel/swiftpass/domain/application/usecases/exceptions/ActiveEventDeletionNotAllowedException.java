package com.feponiel.swiftpass.domain.application.usecases.exceptions;

public class ActiveEventDeletionNotAllowedException extends DomainException {
  public ActiveEventDeletionNotAllowedException() {
    super("Active events with registrations can't be deleted without canceling it before!", DomainExceptionCode.ACTIVE_EVENT_DELETION_NOT_ALLOWED);
  }
}
