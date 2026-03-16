package com.feponiel.swiftpass.infrastructure.mappers;

import org.springframework.stereotype.Component;

import com.feponiel.swiftpass.domain.application.boundaries.EventAddressData;
import com.feponiel.swiftpass.infrastructure.http.payloads.EditEventPayload;

@Component
public class EventAddressMapper {
  public EventAddressData toBoundary(EditEventPayload.EventAddress rawEventAddress) {
    if (rawEventAddress == null)
      return null;

    return new EventAddressData(
      rawEventAddress.getPostalCode(),
      rawEventAddress.getCountry(),
      rawEventAddress.getState(),
      rawEventAddress.getCity(),
      rawEventAddress.getAddressLine1(),
      rawEventAddress.getAddressLine2()
    );
  }
}
