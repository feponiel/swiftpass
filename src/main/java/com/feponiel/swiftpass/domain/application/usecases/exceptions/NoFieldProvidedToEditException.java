package com.feponiel.swiftpass.domain.application.usecases.exceptions;

public class NoFieldProvidedToEditException extends DomainException {
  public NoFieldProvidedToEditException() {
    super("At least one field must be provided to edit this resource", DomainExceptionCode.NO_FIELD_PROVIDED);
  }
}
