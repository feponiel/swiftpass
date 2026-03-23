package com.feponiel.swiftpass.infrastructure.http.presenters;

import java.util.List;

import com.feponiel.swiftpass.domain.business.entities.Registration;
import com.feponiel.swiftpass.infrastructure.http.presenters.dtos.RegistrationListHTTPResponseModel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ListRegistrationsByEventIdPresenter {
  public static RegistrationListHTTPResponseModel toHTTP(List<Registration> registrationList) {
    List<RegistrationListHTTPResponseModel.Registration> remodeledRegistrationList = registrationList
      .stream()
      .map(registration -> {
        return new RegistrationListHTTPResponseModel.Registration(
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
      })
      .toList();
    
    return new RegistrationListHTTPResponseModel(remodeledRegistrationList);
  }
}
