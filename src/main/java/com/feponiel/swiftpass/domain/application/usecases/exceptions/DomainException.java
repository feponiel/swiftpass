package com.feponiel.swiftpass.domain.application.usecases.exceptions;

import lombok.Getter;

@Getter
public abstract class DomainException extends RuntimeException {
  private final DomainExceptionCode code;
  
  public DomainException(String message, DomainExceptionCode code) {
    super(message);
    this.code = code;
  }
}
