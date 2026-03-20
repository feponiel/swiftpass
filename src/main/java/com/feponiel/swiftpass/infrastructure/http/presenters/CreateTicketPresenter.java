package com.feponiel.swiftpass.infrastructure.http.presenters;

import com.feponiel.swiftpass.domain.business.entities.Ticket;
import com.feponiel.swiftpass.infrastructure.http.presenters.dtos.TicketHTTPResponseModel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateTicketPresenter {
  public static TicketHTTPResponseModel toHTTP(Ticket ticket) {
    return new TicketHTTPResponseModel(
      ticket.getId(),
      ticket.getEventId(),
      ticket.getName(),
      ticket.getDescription(),
      ticket.getPrice(),
      ticket.getCurrency(),
      ticket.getCapacity(),
      ticket.getCreatedAt(),
      ticket.getUpdatedAt(),
      ticket.getEditedAt()
    );
  }
}
