package com.feponiel.swiftpass.infrastructure.database.mappers;

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
      .capacity(rawTicket.getCapacity())
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
      rawTicket.getCapacity(),
      rawTicket.getCreatedAt(),
      rawTicket.getUpdatedAt(),
      rawTicket.getEditedAt()
    );
  }
}
