package com.feponiel.swiftpass.infrastructure.http.presenters;

import com.feponiel.swiftpass.domain.business.entities.Event;
import com.feponiel.swiftpass.infrastructure.http.presenters.dtos.EventHTTPResponseModel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateNewEventPresenter {
  public static EventHTTPResponseModel toHTTP(Event event) {
    return new EventHTTPResponseModel(
      event.getId(),
      event.getHostId(),
      event.getName(),
      event.getDescription(),
      event.getBannerUrl(),
      event.getAgeRange(),
      event.getSalesOpen(),
      event.getAddress(),
      event.isOver(),
      event.getStartDate(),
      event.getEndDate(),
      event.getCreatedAt(),
      event.getUpdatedAt(),
      event.getEditedAt()
    );
  }
}
