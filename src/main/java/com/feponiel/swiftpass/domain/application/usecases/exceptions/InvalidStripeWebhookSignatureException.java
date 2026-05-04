package com.feponiel.swiftpass.domain.application.usecases.exceptions;

public class InvalidStripeWebhookSignatureException extends DomainException {
  public InvalidStripeWebhookSignatureException() {
    super("Stripe Webhook signature is invalid!", DomainExceptionCode.INVALID_STRIPE_WEBHOOK);
  }
}
