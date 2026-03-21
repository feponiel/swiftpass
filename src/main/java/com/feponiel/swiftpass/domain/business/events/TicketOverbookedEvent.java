package com.feponiel.swiftpass.domain.business.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TicketOverbookedEvent extends DomainEvent {
  private final String paymentIntentId;
}
