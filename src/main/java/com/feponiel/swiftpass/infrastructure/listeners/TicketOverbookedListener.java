package com.feponiel.swiftpass.infrastructure.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.feponiel.swiftpass.domain.application.services.StripeService;
import com.feponiel.swiftpass.domain.business.events.TicketOverbookedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TicketOverbookedListener {
  private final StripeService stripeService;

  @EventListener
  public void handle(TicketOverbookedEvent event) {
    this.stripeService.processFullRefund(event.getPaymentIntentId());
  }
}
