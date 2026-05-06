package com.feponiel.swiftpass.factories;

import java.util.UUID;
import java.util.function.Consumer;

import com.feponiel.swiftpass.domain.business.entities.Registration;

import net.datafaker.Faker;

public class RegistrationFactory {
  private static final Faker faker = new Faker();

  public static Registration make() {
    return make(_ -> {});
  }

  public static Registration make(Consumer<Registration.RegistrationBuilder> overrides) {
    var builder = Registration.builder()
      .id(UUID.randomUUID())
      .registrantId(UUID.randomUUID())
      .ticketId(UUID.randomUUID())
      .eventId(UUID.randomUUID())
      .holderName(faker.name().fullName())
      .stripeSessionId("cs_test_" + faker.lorem().characters(20));

    overrides.accept(builder);

    return builder.build();
  }
}
