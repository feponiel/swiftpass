package com.feponiel.swiftpass.infrastructure.http.presenters;

import java.util.List;

import com.feponiel.swiftpass.domain.business.entities.Ticket;
import com.feponiel.swiftpass.infrastructure.http.presenters.dtos.TicketListHTTPResponseModel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ListAllTicketsByEventIdPresenter {
  public static TicketListHTTPResponseModel toHTTP(List<Ticket> tickets) {
    List<TicketListHTTPResponseModel.Ticket> remodeledTicketList = tickets
      .stream()
      .map(ticket -> {
        return new TicketListHTTPResponseModel.Ticket(
          ticket.getId(),
          ticket.getEventId(),
          ticket.getName(),
          ticket.getDescription(),
          ticket.getPrice(),
          ticket.getCurrency(),
          ticket.getAmountAvailable(),
          ticket.isSoldOut(),
          ticket.getCreatedAt(),
          ticket.getUpdatedAt(),
          ticket.getEditedAt()
        );
      })
      .toList();

    return new TicketListHTTPResponseModel(remodeledTicketList);
  }
}
