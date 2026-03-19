package com.feponiel.swiftpass.domain.application.providers;

public interface StripeConfigProvider {
  String getWebhookSecret();
  String getSecretKey();
}
