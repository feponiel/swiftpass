package com.feponiel.swiftpass.factories;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.feponiel.swiftpass.domain.business.entities.Event;
import com.feponiel.swiftpass.domain.business.valueobjects.Address;

import net.datafaker.Faker;

public class EventFactory {
  private static final Faker faker = new Faker();

  public static Event make() {
    return make(_ -> {});
  }

  public static Event make(Consumer<Event.EventBuilder> overrides) {
    Instant startDate = faker.timeAndDate().future(365, TimeUnit.DAYS);

    var builder = Event.builder()
      .id(UUID.randomUUID())
      .hostId(UUID.randomUUID())
      .name(faker.rockBand().name() + " New World Tour")
      .ageRange(faker.number().numberBetween(0, 18))
      .address(
        Address.builder()
          .country(faker.address().country())
          .city(faker.address().city())
          .addressLine1(faker.address().streetAddress())
          .build()
        )
      .salesOpen(true)
      .startDate(startDate)
      .endDate(startDate.plusSeconds(faker.number().numberBetween(3600, 18000)));

    overrides.accept(builder);

    return builder.build();
  }
}
