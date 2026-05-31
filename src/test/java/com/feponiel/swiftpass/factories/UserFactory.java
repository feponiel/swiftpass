package com.feponiel.swiftpass.factories;

import java.util.UUID;
import java.util.function.Consumer;

import com.feponiel.swiftpass.domain.business.entities.User;

import net.datafaker.Faker;

public class UserFactory {
  private static final Faker faker = new Faker();

  public static User make() {
    return make(_ -> {});
  }

  public static User make(Consumer<User.UserBuilder> overrides) {
    var builder = User.builder()
      .id(UUID.randomUUID())
      .providerId("google")
      .name(faker.name().fullName())
      .email(faker.internet().emailAddress())
      .pictureUrl("https://picsum.photos/300");

    overrides.accept(builder);

    return builder.build();
  }
}
