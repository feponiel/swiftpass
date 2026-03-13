package com.feponiel.swiftpass.domain.application.usecases.exceptions;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException() {
    super("User not found!");
  }
}
