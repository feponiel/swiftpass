package com.feponiel.swiftpass.domain.application.usecases;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.feponiel.swiftpass.domain.application.boundaries.EventAddressData;
import com.feponiel.swiftpass.domain.application.repositories.EventsRepository;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.EventNotFoundException;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.NoFieldProvidedToEditException;
import com.feponiel.swiftpass.domain.business.entities.Event;
import com.feponiel.swiftpass.domain.business.valueobjects.Address;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EditEventUseCase {
  private final EventsRepository eventsRepository;

  public void execute(
    UUID eventId,
    String name,
    String description,
    String bannerUrl,
    Integer ageRange,
    EventAddressData address,
    Instant startDate,
    Instant endDate
  ) {
    if (
      name == null &&
      description == null &&
      bannerUrl == null &&
      ageRange == null &&
      address == null &&
      startDate == null &&
      endDate == null
    ) {
      throw new NoFieldProvidedToEditException();
    }

    Event event = this.eventsRepository.findById(eventId)
      .orElseThrow(EventNotFoundException::new);

    Address newEventAddress = null;

    if (address != null) {
      newEventAddress = Address.builder()
        .postalCode(address.postalCode() == null ? event.getAddress().getPostalCode() : address.postalCode())
        .country(address.country() == null ? event.getAddress().getCountry() : address.country())
        .state(address.state() == null ? event.getAddress().getState() : address.state())
        .city(address.city() == null ? event.getAddress().getCity() : address.city())
        .addressLine1(address.addressLine1() == null ? event.getAddress().getAddressLine1() : address.addressLine1())
        .addressLine2(address.addressLine2() == null ? event.getAddress().getAddressLine2() : address.addressLine2())
        .build();
    }

    event
      .changeName(name)
      .changeDescription(description)
      .changeBanner(bannerUrl)
      .changeAgeRange(ageRange)
      .changeAddress(newEventAddress)
      .changeStartDate(startDate)
      .changeEndDate(endDate);

    this.eventsRepository.update(event);
  }
}
