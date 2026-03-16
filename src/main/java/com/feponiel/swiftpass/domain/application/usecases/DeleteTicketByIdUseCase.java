package com.feponiel.swiftpass.domain.application.usecases;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.feponiel.swiftpass.domain.application.repositories.TicketsRepository;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.TicketNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteTicketByIdUseCase {
  private final TicketsRepository ticketsRepository;

  public void execute(UUID ticketId) {
    this.ticketsRepository.findById(ticketId)
      .orElseThrow(TicketNotFoundException::new);

    this.ticketsRepository.deleteById(ticketId);
  }
}
