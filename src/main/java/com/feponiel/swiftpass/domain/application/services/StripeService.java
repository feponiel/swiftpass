package com.feponiel.swiftpass.domain.application.services;

import java.util.List;

import com.feponiel.swiftpass.domain.application.boundaries.CheckoutItemData;
import com.feponiel.swiftpass.domain.application.boundaries.CheckoutSessionData;

public interface StripeService {
  CheckoutSessionData createCheckoutSession(List<CheckoutItemData> checkoutItemsList);
}
