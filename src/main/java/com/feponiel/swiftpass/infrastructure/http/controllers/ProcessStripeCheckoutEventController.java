package com.feponiel.swiftpass.infrastructure.http.controllers;

import java.nio.charset.StandardCharsets;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feponiel.swiftpass.domain.application.usecases.ProcessStripeCheckoutEventUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/webhooks/stripe")
@RequiredArgsConstructor
public class ProcessStripeCheckoutEventController {
  private final ProcessStripeCheckoutEventUseCase processStripeCheckoutEventUseCase;

  @PostMapping
  public ResponseEntity<Void> handle(
    @RequestBody byte[] payload,
    @RequestHeader("Stripe-Signature") String stripeSignature
  ) {
    String rawPayload = new String(payload, StandardCharsets.UTF_8);

    this.processStripeCheckoutEventUseCase.execute(rawPayload, stripeSignature);

    return ResponseEntity.ok().build();
  }
}
