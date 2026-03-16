package com.feponiel.swiftpass.domain.application.usecases.exceptions;

public class InvalidCurrencyException extends RuntimeException {
  public InvalidCurrencyException() {
    super("Currency must follow ISO 4217 format (e.g., BRL, USD, EUR).");
  }
}
