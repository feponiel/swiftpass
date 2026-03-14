package com.feponiel.swiftpass.infrastructure.http.presenters.dtos;

import java.time.Instant;
import java.util.UUID;

import com.feponiel.swiftpass.domain.business.valueobjects.Address;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateNewEventHTTPResponse {
  private final UUID id;
  private final UUID hostId;
  private final String name;
  private final String description;
  private final String bannerUrl;
  private final Integer ageRange;
  private final Boolean salesOpen;
  private final Address address;
  private final Boolean isOver;
  private final Instant startDate;
  private final Instant endDate;
  private final Instant createdAt;
  private final Instant updatedAt;
  private final Instant editedAt;
}
