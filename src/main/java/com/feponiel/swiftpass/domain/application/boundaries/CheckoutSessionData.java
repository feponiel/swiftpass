package com.feponiel.swiftpass.domain.application.boundaries;

public record CheckoutSessionData(
  String sessionId,
  String checkoutUrl
) {}
