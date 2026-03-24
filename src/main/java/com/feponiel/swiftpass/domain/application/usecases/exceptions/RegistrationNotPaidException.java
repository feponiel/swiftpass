package com.feponiel.swiftpass.domain.application.usecases.exceptions;

public class RegistrationNotPaidException extends RuntimeException {
  public RegistrationNotPaidException() {
    super("The registration has not yet been paid!");
  }
}
