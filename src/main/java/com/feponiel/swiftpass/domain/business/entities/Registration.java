package com.feponiel.swiftpass.domain.business.entities;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.feponiel.swiftpass.domain.business.valueobjects.PaymentStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class Registration extends Entity {
  private UUID registrantId;
  private UUID ticketId;
  private UUID eventId;
  private String holderName;
  private PaymentStatus paymentStatus;
  private String checkoutUrl;
  private String stripeSessionId;
  private BigDecimal totalPaid;
  private String paidCurrency;
  private Instant createdAt;
  private Instant updatedAt;
  private Instant editedAt;

  @Builder
  private Registration(
    @NonNull UUID id,
    @NonNull UUID registrantId,
    @NonNull UUID ticketId,
    @NonNull UUID eventId,
    @NonNull String holderName,
    PaymentStatus paymentStatus,
    String checkoutUrl,
    String stripeSessionId,
    BigDecimal totalPaid,
    String paidCurrency,
    @NonNull Instant createdAt,
    Instant updatedAt,
    Instant editedAt
  ) {
    super(id);

    this.registrantId = registrantId;
    this.ticketId = ticketId;
    this.eventId = eventId;
    this.holderName = holderName;
    this.paymentStatus = paymentStatus != null ? paymentStatus : PaymentStatus.PENDING;
    this.checkoutUrl = checkoutUrl;
    this.stripeSessionId = stripeSessionId;
    this.totalPaid = totalPaid;
    this.paidCurrency = paidCurrency;
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

  public void changeHolderName(String newHolderName) {
    this.holderName = newHolderName;

    this.markEdited();
  }

  public void updatePaymentStatus(PaymentStatus newPaymentStatus) {
    this.paymentStatus = newPaymentStatus;

    this.touch();
  }

  public void updateCheckoutURL(String newCheckoutURL) {
    this.checkoutUrl = newCheckoutURL;

    this.touch();
  }

  public void updateStripeSessionID(String newStripeSessionID) {
    this.stripeSessionId = newStripeSessionID;

    this.touch();
  }

  public void updateTotalPaid(BigDecimal newTotalPaid) {
    this.totalPaid = newTotalPaid;

    this.touch();
  }

  public void updatePaidCurrency(String newPaidCurrency) {
    this.paidCurrency = newPaidCurrency;

    this.touch();
  }

  public void cancel() {
    this.paymentStatus = PaymentStatus.CANCELLED;
  }

  public void refund() {
    this.paymentStatus = PaymentStatus.REFUNDED;
  }
}
