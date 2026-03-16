package com.feponiel.swiftpass.domain.business.entities;

import java.time.Instant;
import java.util.UUID;

import com.feponiel.swiftpass.domain.business.valueobjects.Address;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class Event extends Entity {
  private UUID hostId;
  private String name;
  private String description;
  private String bannerUrl;
  private Integer ageRange;
  private Boolean salesOpen;
  private Address address;
  private Instant startDate;
  private Instant endDate;
  private Instant createdAt;
  private Instant updatedAt;
  private Instant editedAt;

  @Builder
  private Event(
    @NonNull UUID id,
    @NonNull UUID hostId,
    @NonNull String name,
    String description,
    String bannerUrl,
    @NonNull Integer ageRange,
    Boolean salesOpen,
    @NonNull Address address,
    @NonNull Instant startDate,
    @NonNull Instant endDate,
    Instant createdAt,
    Instant updatedAt,
    Instant editedAt
  ) {
    super(id);

    this.hostId = hostId;
    this.name = name;
    this.description = description;
    this.bannerUrl = bannerUrl;
    this.ageRange = ageRange;
    this.salesOpen = salesOpen != null ? salesOpen : false;
    this.address = address;
    this.startDate = startDate;
    this.endDate = endDate;
    this.createdAt = createdAt != null ? createdAt : Instant.now();
    this.updatedAt = updatedAt;
    this.editedAt = editedAt;
  }

  protected void touch() {
    this.updatedAt = Instant.now();
  }

  protected void markEdited() {
    this.touch();

    this.editedAt = Instant.now();
  }

  public Event changeName(String newName) {
    if (newName == null)
      return this;

    this.name = newName;

    this.markEdited();

    return this;
  }

  public Event changeDescription(String newDescription) {
    if (newDescription == null)
      return this;

    this.description = newDescription;

    this.markEdited();

    return this;
  }

  public Event changeBanner(String newBannerUrl) {
    if (newBannerUrl == null)
      return this;

    this.bannerUrl = newBannerUrl;

    this.markEdited();

    return this;
  }

  public Event changeAgeRange(Integer newAgeRange) {
    if (newAgeRange == null)
      return this;

    this.ageRange = newAgeRange;

    this.markEdited();

    return this;
  }

  public void openSales() {
    this.salesOpen = true;

    this.touch();
  }

  public void closeSales() {
    this.salesOpen = false;

    this.touch();
  }

  public Event changeAddress(Address newAddress) {
    if (newAddress == null)
      return this;

    this.address = newAddress;

    this.markEdited();

    return this;
  }

  public Event changeStartDate(Instant newStartDate) {
    if (newStartDate == null)
      return this;

    this.startDate = newStartDate;

    this.markEdited();

    return this;
  }

  public Event changeEndDate(Instant newEndDate) {
    if (newEndDate == null)
      return this;

    this.endDate = newEndDate;

    this.markEdited();

    return this;
  }

  public Boolean isOver() {
    Instant now = Instant.now();

    if (now.isBefore(this.endDate)) {
      return false;
    }

    return true;
  }
}
