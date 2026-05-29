package com.feponiel.swiftpass.factories;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.function.Consumer;

import com.feponiel.swiftpass.domain.business.entities.Ticket;

import net.datafaker.Faker;

public class TicketFactory {
  private static final Faker faker = new Faker();

  public static Ticket make() {
    return make(_ -> {});
  }

  public static Ticket make(Consumer<Ticket.TicketBuilder> overrides) {
    var builder = Ticket.builder()
      .id(UUID.randomUUID())
      .eventId(UUID.randomUUID())
      .name(faker.lorem().characters(20))
      .price(BigDecimal.valueOf(faker.number().randomNumber(4, false)))
      .currency(faker.money().currencyCode())
      .capacity((int) faker.number().randomNumber());

    overrides.accept(builder);

    return builder.build();
  }
}
