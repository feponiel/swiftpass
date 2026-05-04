package com.feponiel.swiftpass.domain.application.usecases.exceptions;

public class UserNotFoundException extends DomainException {
  public UserNotFoundException() {
    super("User not found!", DomainExceptionCode.USER_NOT_FOUND);
  }
}
