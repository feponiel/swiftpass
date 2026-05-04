package com.feponiel.swiftpass.domain.application.usecases.exceptions;

public class RegistrationNotPaidException extends DomainException {
  public RegistrationNotPaidException() {
    super("The registration has not yet been paid!", DomainExceptionCode.REGISTRATION_NOT_PAID);
  }
}
