package com.feponiel.swiftpass.infrastructure.mappers;

import org.springframework.stereotype.Component;

import com.feponiel.swiftpass.domain.business.entities.Event;
import com.feponiel.swiftpass.domain.business.valueobjects.Address;
import com.feponiel.swiftpass.infrastructure.database.entities.JPAEvent;

@Component
public class EventMapper {
  public Event toDomain(JPAEvent rawEvent) {
    Address eventAddressLocation = Address.builder()
      .postalCode(rawEvent.getPostalCode())
      .country(rawEvent.getCountry())
      .state(rawEvent.getState())
      .city(rawEvent.getCity())
      .addressLine1(rawEvent.getAddressLine1())
      .addressLine2(rawEvent.getAddressLine2())
      .build();

    return Event.builder()
      .id(rawEvent.getId())
      .hostId(rawEvent.getHostId())
      .name(rawEvent.getName())
      .description(rawEvent.getDescription())
      .bannerUrl(rawEvent.getBannerUrl())
      .ageRange(rawEvent.getAgeRange())
      .salesOpen(rawEvent.getSalesOpen())
      .address(eventAddressLocation)
      .startDate(rawEvent.getStartDate())
      .endDate(rawEvent.getEndDate())
      .createdAt(rawEvent.getCreatedAt())
      .updatedAt(rawEvent.getUpdatedAt())
      .editedAt(rawEvent.getEditedAt())
      .build();
  }

  public JPAEvent toJPA(Event rawEvent) {
    return new JPAEvent(
      rawEvent.getId(),
      rawEvent.getHostId(),
      rawEvent.getName(),
      rawEvent.getDescription(),
      rawEvent.getBannerUrl(),
      rawEvent.getAgeRange(),
      rawEvent.getSalesOpen(),
      rawEvent.getAddress().getPostalCode(),
      rawEvent.getAddress().getCountry(),
      rawEvent.getAddress().getState(),
      rawEvent.getAddress().getCity(),
      rawEvent.getAddress().getAddressLine1(),
      rawEvent.getAddress().getAddressLine2(),
      rawEvent.getStartDate(),
      rawEvent.getEndDate(),
      rawEvent.getCreatedAt(),
      rawEvent.getUpdatedAt(),
      rawEvent.getEditedAt()
    );
  }
}
