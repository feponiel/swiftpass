package com.feponiel.swiftpass.infrastructure.http.presenters;

import java.util.List;

import com.feponiel.swiftpass.domain.business.entities.Event;
import com.feponiel.swiftpass.infrastructure.http.presenters.dtos.EventListHTTPResponseModel;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ListAllEventsPresenter {
  public static EventListHTTPResponseModel toHTTP(List<Event> events) {
    List<EventListHTTPResponseModel.Event> remodeledEventList = events
      .stream()
      .map(event -> {
        return new EventListHTTPResponseModel.Event(
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
      })
      .toList();

    return new EventListHTTPResponseModel(remodeledEventList);
  }
}
