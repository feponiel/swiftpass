package com.feponiel.swiftpass.domain.business.valueobjects;

import lombok.Getter;
import lombok.NonNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public final class Address {
  private final String postalCode;
  private final String country;
  private final String state;
  private final String city;
  private final String addressLine1;
  private final String addressLine2;

  @Builder
  private Address(
    String postalCode,
    @NonNull String country,
    String state,
    @NonNull String city,
    @NonNull String addressLine1,
    String addressLine2
  ) {

    this.postalCode = postalCode;
    this.country = country;
    this.state = state;
    this.city = city;
    this.addressLine1 = addressLine1;
    this.addressLine2 = addressLine2;
  }

  public static AddressBuilder create() {
    return new AddressBuilder();
  }
}
