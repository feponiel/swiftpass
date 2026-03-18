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
    Instant createdAt,
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

  public Registration changeHolderName(String newHolderName) {
    this.holderName = newHolderName;

    this.markEdited();

    return this;
  }

  public Registration updatePaymentStatus(PaymentStatus newPaymentStatus) {
    if (newPaymentStatus == null)
      return this;

    this.paymentStatus = newPaymentStatus;

    this.touch();

    return this;
  }

  public Registration updateCheckoutURL(String newCheckoutURL) {
    if (newCheckoutURL == null)
      return this;

    this.checkoutUrl = newCheckoutURL;

    this.touch();

    return this;
  }

  public Registration updateStripeSessionID(String newStripeSessionID) {
    if (newStripeSessionID == null)
      return this;

    this.stripeSessionId = newStripeSessionID;

    this.touch();

    return this;
  }

  public Registration updateTotalPaid(BigDecimal newTotalPaid) {
    if (newTotalPaid == null)
      return this;

    this.totalPaid = newTotalPaid;

    this.touch();

    return this;
  }

  public Registration updatePaidCurrency(String newPaidCurrency) {
    if (newPaidCurrency == null)
      return this;

    this.paidCurrency = newPaidCurrency;

    this.touch();

    return this;
  }

  public void cancel() {
    this.paymentStatus = PaymentStatus.CANCELLED;

    this.touch();
  }

  public void refund() {
    this.paymentStatus = PaymentStatus.REFUNDED;

    this.touch();
  }
}
