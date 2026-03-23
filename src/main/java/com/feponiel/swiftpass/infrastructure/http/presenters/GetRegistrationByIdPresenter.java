package com.feponiel.swiftpass.infrastructure.http.presenters;

import com.feponiel.swiftpass.domain.business.entities.Registration;
import com.feponiel.swiftpass.infrastructure.http.presenters.dtos.RegistrationHTTPResponseModel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetRegistrationByIdPresenter {
  public static RegistrationHTTPResponseModel toHTTP(Registration registration) {
    return new RegistrationHTTPResponseModel(
      registration.getId(),
      registration.getRegistrantId(),
      registration.getTicketId(),
      registration.getEventId(),
      registration.getHolderName(),
      registration.getPaymentStatus(),
      registration.getCheckoutUrl(),
      registration.getTotalPaid(),
      registration.getPaidCurrency(),
      registration.getCreatedAt(),
      registration.getUpdatedAt(),
      registration.getEditedAt()
    );
  }
}
