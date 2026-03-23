package com.feponiel.swiftpass.domain.application.usecases.exceptions;

public class RegistrationNotFoundException extends RuntimeException {
  public RegistrationNotFoundException() {
    super("Registration not found!");
  }
}