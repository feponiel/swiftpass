package com.feponiel.swiftpass.domain.application.usecases.exceptions;

public class NoFieldProvidedToEditException extends RuntimeException {
  public NoFieldProvidedToEditException() {
    super("At least one field must be provided to edit this resource");
  }
}
