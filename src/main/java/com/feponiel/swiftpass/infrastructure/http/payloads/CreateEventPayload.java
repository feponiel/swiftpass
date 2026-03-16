package com.feponiel.swiftpass.infrastructure.http.payloads;

import java.time.Instant;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventPayload {
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class EventAddress {
    @Size(min = 3, max = 20)
    private String postalCode;

    @NotNull
    @Size(min = 3, max = 100)
    private String country;

    @Size(min = 2, max = 100)
    private String state;

    @NotNull
    @Size(min = 2, max = 100)
    private String city;

    @NotNull
    @Size(min = 2, max = 100)
    private String addressLine1;

    @Size(min = 2, max = 100)
    private String addressLine2;
  }

  @NotNull
  @Size(min = 2, max = 100)
  private String name;

  @Size(min = 10, max = 10_000)
  private String description;

  @URL
  @Size(max = 2048)
  private String bannerUrl;

  @NotNull
  @Min(0)
  @Max(100)
  private Integer ageRange;

  @NotNull
  private Boolean salesOpen;

  @Valid
  @NotNull
  private EventAddress address;

  @NotNull
  private Instant startDate;

  @NotNull
  @Future
  private Instant endDate;
}
