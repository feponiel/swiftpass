package com.feponiel.swiftpass.infrastructure.http.presenters;

import java.util.List;

import com.feponiel.swiftpass.domain.business.entities.Event;
import com.feponiel.swiftpass.infrastructure.http.presenters.dtos.ListAllEventsHTTPResponse;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ListAllEventsPresenter {
  public static ListAllEventsHTTPResponse toHTTP(List<Event> events) {
    List<ListAllEventsHTTPResponse.Event> remodeledEventList = events
      .stream()
      .map(event -> {
        return new ListAllEventsHTTPResponse.Event(
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

    return new ListAllEventsHTTPResponse(remodeledEventList);
  }
}
