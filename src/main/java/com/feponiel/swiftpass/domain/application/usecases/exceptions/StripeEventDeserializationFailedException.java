package com.feponiel.swiftpass.domain.application.usecases.exceptions;

public class StripeEventDeserializationFailedException extends DomainException {
  public StripeEventDeserializationFailedException() {
    super("An error ocurred during Stripe event object deserialization!", DomainExceptionCode.STRIPE_EVENT_DESERIALIZATION_FAILED);
  }
}