package com.feponiel.swiftpass.domain.application.services;

import java.math.BigDecimal;
import java.util.List;

import com.feponiel.swiftpass.domain.application.boundaries.CheckoutItemData;
import com.feponiel.swiftpass.domain.application.boundaries.CheckoutSessionData;
import com.feponiel.swiftpass.domain.application.boundaries.StripeCheckoutEventData;

public interface StripeService {
  CheckoutSessionData createCheckoutSession(List<CheckoutItemData> checkoutItemsList);
  StripeCheckoutEventData parseWebhookAndGetSessionData(String eventName, String stripeSignature);
  void processFullRefund(String paymentIntentId);
  void processPartialRefund(String stripeSessionId, BigDecimal amountToRefund);
  void endSession(String stripeSessionId);
}
