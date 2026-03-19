package com.feponiel.swiftpass.domain.application.usecases.exceptions;

public class InvalidStripeWebhookSignatureException extends RuntimeException {
  public InvalidStripeWebhookSignatureException() {
    super("Stripe Webhook signature is invalid!");
  }
}
