package com.feponiel.swiftpass.domain.application.usecases.exceptions;

public class RegistrationNotFoundException extends DomainException {
  public RegistrationNotFoundException() {
    super("Registration not found!", DomainExceptionCode.REGISTRATION_NOT_FOUND);
  }
}