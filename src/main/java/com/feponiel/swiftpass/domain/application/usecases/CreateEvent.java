package com.feponiel.swiftpass.domain.application.usecases;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.feponiel.swiftpass.domain.application.repositories.EventsRepository;
import com.feponiel.swiftpass.domain.business.entities.Event;
import com.feponiel.swiftpass.domain.business.valueobjects.Address;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateEvent {
  private final EventsRepository eventsRepository;

  public Event execute(
    UUID hostId,
    String name,
    String description,
    Integer ageRange,
    Boolean salesOpen,
    String postalCode,
    String country,
    String state,
    String city,
    String addressLine1,
    String addressLine2,
    Instant startDate,
    Instant endDate
  ) {
    Address eventAddress = Address.builder()
      .postalCode(postalCode)
      .country(country)
      .state(state)
      .city(city)
      .addressLine1(addressLine1)
      .addressLine2(addressLine2)
      .build();

    Event event = Event.builder()
      .id(UUID.randomUUID())
      .hostId(hostId)
      .name(name)
      .description(description)
      .ageRange(ageRange)
      .salesOpen(salesOpen)
      .address(eventAddress)
      .startDate(startDate)
      .endDate(endDate)
      .build();

      this.eventsRepository.create(event);

      return event;
  }
}
