package com.feponiel.swiftpass.infrastructure.mappers;

import org.springframework.stereotype.Component;

import com.feponiel.swiftpass.domain.business.entities.Ticket;
import com.feponiel.swiftpass.infrastructure.database.entities.JPATicket;

@Component
public class TicketMapper {
  public Ticket toDomain(JPATicket rawTicket) {
    return Ticket.builder()
      .id(rawTicket.getId())
      .eventId(rawTicket.getEventId())
      .name(rawTicket.getName())
      .description(rawTicket.getDescription())
      .price(rawTicket.getPrice())
      .currency(rawTicket.getCurrency())
      .amountAvailable(rawTicket.getAmountAvailable())
      .createdAt(rawTicket.getCreatedAt())
      .updatedAt(rawTicket.getUpdatedAt())
      .editedAt(rawTicket.getEditedAt())
      .build();
  }

  public JPATicket toJPA(Ticket rawTicket) {
    return new JPATicket(
      rawTicket.getId(),
      rawTicket.getEventId(),
      rawTicket.getName(),
      rawTicket.getDescription(),
      rawTicket.getPrice(),
      rawTicket.getCurrency(),
      rawTicket.getAmountAvailable(),
      rawTicket.getCreatedAt(),
      rawTicket.getUpdatedAt(),
      rawTicket.getEditedAt()
    );
  }
}
