package com.feponiel.swiftpass.infrastructure.providers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.feponiel.swiftpass.domain.application.providers.StripeConfigProvider;

@Component
public class StripeConfigProviderImpl implements StripeConfigProvider {
  @Value("${application.stripe.secret-key}")
  private String secretKey;

  @Value("${application.stripe.webhook-secret}")
  private String webhookSecret;

  public String getWebhookSecret() {
    return webhookSecret;
  }

  public String getSecretKey() {
    return secretKey;
  }
}
