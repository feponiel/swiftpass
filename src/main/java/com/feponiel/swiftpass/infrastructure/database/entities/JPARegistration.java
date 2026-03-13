package com.feponiel.swiftpass.infrastructure.database.entities;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.feponiel.swiftpass.domain.business.valueobjects.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "registrations")
public class JPARegistration {
  @Id
  @Column(name = "id")
  private UUID id;

  @Column(name = "registrant_id", nullable = false)
  private UUID registrantId;

  @Column(name = "ticket_id", nullable = false)
  private UUID ticketId;

  @Column(name = "event_id", nullable = false)
  private UUID eventId;

  @Column(name = "holder_name", nullable = false, length = 100)
  private String holderName;

  @Column(name = "payment_status", nullable = false)
  @Enumerated(EnumType.STRING)
  private PaymentStatus paymentStatus;

  @Column(name = "checkout_url", length = 2048)
  private String checkoutUrl;

  @Column(name = "stripe_session_id")
  private String stripeSessionId;

  @Column(name = "total_paid", precision = 10, scale = 2)
  private BigDecimal totalPaid;

  @Column(name = "paid_currency", columnDefinition = "CHAR(3)")
  private String paidCurrency;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  @Column(name = "edited_at")
  private Instant editedAt;
}
 