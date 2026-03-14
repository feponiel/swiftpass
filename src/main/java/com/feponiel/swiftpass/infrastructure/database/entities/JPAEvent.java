package com.feponiel.swiftpass.infrastructure.database.entities;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class JPAEvent {
  @Id
  @Column(name = "id")
  private UUID id;

  @Column(name = "host_id", nullable = false)
  private UUID hostId;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "description", length = 10_000)
  private String description;

  @Column(name = "banner_url", length = 2048)
  private String bannerUrl;

  @Column(name="age_range", nullable = false)
  private Integer ageRange;

  @Column(name="sales_open", nullable = false)
  private Boolean salesOpen;

  @Column(name="postal_code")
  private String postalCode;

  @Column(name="country", nullable = false)
  private String country;

  @Column(name="state")
  private String state;

  @Column(name="city", nullable = false)
  private String city;

  @Column(name="address_line1", nullable = false)
  private String addressLine1;

  @Column(name="address_line2")
  private String addressLine2;

  @Column(name="start_date", nullable = false)
  private Instant startDate;

  @Column(name="end_date", nullable = false)
  private Instant endDate;

  @Column(name="created_at", nullable = false)
  private Instant createdAt;

  @Column(name="updated_at")
  private Instant updatedAt;

  @Column(name="edited_at")
  private Instant editedAt;
}
