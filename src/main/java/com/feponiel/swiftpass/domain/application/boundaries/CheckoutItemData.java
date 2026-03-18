package com.feponiel.swiftpass.domain.application.boundaries;

import java.math.BigDecimal;

public record CheckoutItemData(
  String ticketName,
  String ticketDescription,
  BigDecimal ticketPrice,
  String ticketCurrency,
  Long ticketsAmount
) {}
