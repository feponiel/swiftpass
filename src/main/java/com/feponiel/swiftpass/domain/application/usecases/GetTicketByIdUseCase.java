package com.feponiel.swiftpass.domain.application.usecases;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.feponiel.swiftpass.domain.application.repositories.TicketsRepository;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.TicketNotFoundException;
import com.feponiel.swiftpass.domain.business.entities.Ticket;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetTicketByIdUseCase {
  private final TicketsRepository ticketsRepository;

  public Ticket execute(UUID tickedId) {
    return this.ticketsRepository.findById(tickedId)
      .orElseThrow(TicketNotFoundException::new);
  }
}
