package com.feponiel.swiftpass.infrastructure.http.payloads;

import java.time.Instant;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EditEventPayload {
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class EventAddress {
    @Size(min = 3, max = 20)
    private String postalCode;

    @Size(min = 3, max = 100)
    private String country;

    @Size(min = 2, max = 100)
    private String state;

    @Size(min = 2, max = 100)
    private String city;

    @Size(min = 2, max = 100)
    private String addressLine1;

    @Size(min = 2, max = 100)
    private String addressLine2;
  }

  @Size(min = 2, max = 100)
  private String name;

  @Size(min = 10, max = 10_000)
  private String description;

  @URL
  @Size(max = 2048)
  private String bannerUrl;

  @Min(0)
  @Max(100)
  private Integer ageRange;

  @Valid
  private EventAddress address;

  private Instant startDate;

  @Future
  private Instant endDate;
}
