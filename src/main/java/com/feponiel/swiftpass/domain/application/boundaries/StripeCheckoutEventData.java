package com.feponiel.swiftpass.domain.application.boundaries;

public record StripeCheckoutEventData(
  String eventType,
  String sessionId,
  String paymentIntentId
) {}
