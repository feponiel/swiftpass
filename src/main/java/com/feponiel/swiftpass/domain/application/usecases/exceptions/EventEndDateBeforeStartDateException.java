package com.feponiel.swiftpass.domain.application.usecases.exceptions;

public class EventEndDateBeforeStartDateException extends DomainException {
  public EventEndDateBeforeStartDateException() {
    super("Event end date can't be before the start date!", DomainExceptionCode.EVENT_END_DATE_BEFORE_START_DATE);
  }
}
