package com.feponiel.swiftpass.infrastructure.http.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feponiel.swiftpass.domain.application.usecases.CancelRegistrationsByStripeSessionIdUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/checkout-sessions/{stripeSessionId}/cancel")
@RequiredArgsConstructor
public class CancelRegistrationsByStripeSessionId {
  private final CancelRegistrationsByStripeSessionIdUseCase cancelRegistrationsByStripeSessionIdUseCase;

  @PostMapping
  public ResponseEntity<Void> handle(@PathVariable String stripeSessionId) {
    this.cancelRegistrationsByStripeSessionIdUseCase.execute(stripeSessionId);

    return ResponseEntity.noContent().build();
  }
}
