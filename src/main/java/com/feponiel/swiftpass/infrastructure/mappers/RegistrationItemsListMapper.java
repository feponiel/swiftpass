package com.feponiel.swiftpass.infrastructure.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.feponiel.swiftpass.domain.application.boundaries.RegistrationItemData;
import com.feponiel.swiftpass.infrastructure.http.payloads.CreateRegistrationsPayload;

@Component
public class RegistrationItemsListMapper {
  public List<RegistrationItemData> toBoundary(List<CreateRegistrationsPayload.RegistrationItem> rawRegistrationItems) {
    return rawRegistrationItems
      .stream()
      .map(registrationItem -> {
        return new RegistrationItemData(
          registrationItem.getTicketId(),
          registrationItem.getHolderName()
        );
      })
      .toList();
  }
}
