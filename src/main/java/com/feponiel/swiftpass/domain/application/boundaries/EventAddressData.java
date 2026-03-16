package com.feponiel.swiftpass.domain.application.boundaries;

public record EventAddressData(
  String postalCode,
  String country,
  String state,
  String city,
  String addressLine1,
  String addressLine2
) {}
