package com.feponiel.swiftpass.infrastructure.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.feponiel.swiftpass.domain.application.boundaries.CheckoutItemData;
import com.feponiel.swiftpass.domain.application.boundaries.CheckoutSessionData;
import com.feponiel.swiftpass.domain.application.services.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StripeServiceImpl implements StripeService {
  @Value("${application.stripe.secret-key}")
  private String stripeSecretKey;

  @Value("${application.front-end-url}")
  private String frontEndUrl;

  public CheckoutSessionData createCheckoutSession(List<CheckoutItemData> checkoutItemsList) {
    Stripe.apiKey = stripeSecretKey;

    try {
      List<SessionCreateParams.LineItem> lineItems = checkoutItemsList
        .stream()
        .map(item -> {
          return SessionCreateParams.LineItem.builder()
            .setQuantity(item.ticketsCapacity())
            .setPriceData(
              SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency(item.ticketCurrency())
                .setUnitAmount(item.ticketPrice().multiply(BigDecimal.valueOf(100)).longValue())
                .setProductData(
                  SessionCreateParams.LineItem.PriceData.ProductData.builder()
                    .setName(item.ticketName())
                    .setDescription(item.ticketDescription())
                    .build()
                )
                .build()
            )
            .build();
        })
        .toList();

      SessionCreateParams params = SessionCreateParams.builder()
        .setMode(SessionCreateParams.Mode.PAYMENT)
        .setSuccessUrl(frontEndUrl + "/checkout/success")
        .setCancelUrl(frontEndUrl + "/checkout/cancel")
        .addAllLineItem(lineItems)
        .build();

      Session session = Session.create(params);

      return new CheckoutSessionData(session.getId(), session.getUrl());
    } catch (StripeException error) {
      throw new RuntimeException("Failed to create Stripe checkout session!", error);
    }
  }
}
