package com.feponiel.swiftpass.infrastructure.database.entities;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tickets")
public class JPATicket {
  @Id
  @Column(name = "id")
  private UUID id;

  @Column(name = "event_id", nullable = false)
  private UUID eventId;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "description", length = 2_000)
  private String description;

  @Column(name = "price", nullable = false, precision = 10, scale = 2)
  private BigDecimal price;

  @Column(name = "currency", nullable = false, columnDefinition = "CHAR(3)")
  private String currency;

  @Column(name = "capacity", nullable = false)
  private Integer capacity;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  @Column(name = "edited_at")
  private Instant editedAt;
}
