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

  public void changeName(String newName) {
    this.name = newName;

    this.markEdited();
  }

  public void changeDescription(String newDescription) {
    this.description = newDescription;

    this.markEdited();
  }

  public void changeBanner(String newBannerUrl) {
    this.bannerUrl = newBannerUrl;

    this.markEdited();
  }

  public void changeAgeRange(Integer newAgeRange) {
    this.ageRange = newAgeRange;

    this.markEdited();
  }

  public void openSales() {
    this.salesOpen = true;

    this.touch();
  }

  public void closeSales() {
    this.salesOpen = false;

    this.touch();
  }

  public void changeAddress(Address newAddress) {
    this.address = newAddress;

    this.markEdited();
  }

  public void changeStartDate(Instant newStartDate) {
    this.startDate = newStartDate;

    this.markEdited();
  }

  public void changeEndDate(Instant newEndDate) {
    this.endDate = newEndDate;

    this.markEdited();
  }

  public Boolean isOver() {
    Instant now = Instant.now();

    if (now.isBefore(this.endDate)) {
      return false;
    }

    return true;
  }
}
