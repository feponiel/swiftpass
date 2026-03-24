package com.feponiel.swiftpass.domain.application.usecases;

import org.springframework.stereotype.Service;

import com.feponiel.swiftpass.domain.application.services.StripeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CancelRegistrationsByStripeSessionIdUseCase {
  private final StripeService stripeService;

  public void execute(String stripeSessionId) {
    this.stripeService.endSession(stripeSessionId);
  }
}
