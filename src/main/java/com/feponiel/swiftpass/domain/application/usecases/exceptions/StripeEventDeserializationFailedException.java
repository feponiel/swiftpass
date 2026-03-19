package com.feponiel.swiftpass.domain.application.usecases.exceptions;

public class StripeEventDeserializationFailedException extends RuntimeException {
  public StripeEventDeserializationFailedException() {
    super("An error ocurred during Stripe event object deserialization!");
  }
}