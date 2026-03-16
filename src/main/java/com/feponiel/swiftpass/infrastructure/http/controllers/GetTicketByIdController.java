package com.feponiel.swiftpass.infrastructure.http.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feponiel.swiftpass.domain.application.usecases.GetTicketByIdUseCase;
import com.feponiel.swiftpass.domain.business.entities.Ticket;
import com.feponiel.swiftpass.infrastructure.http.presenters.GetTicketByIdPresenter;
import com.feponiel.swiftpass.infrastructure.http.presenters.dtos.TicketHTTPResponseModel;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tickets/{ticketId}")
@RequiredArgsConstructor
public class GetTicketByIdController {
  private final GetTicketByIdUseCase getTicketByIdUseCase;

  @GetMapping
  public ResponseEntity<TicketHTTPResponseModel> handle(@PathVariable UUID ticketId) {
    Ticket ticket = this.getTicketByIdUseCase.execute(ticketId);

    return ResponseEntity.ok(GetTicketByIdPresenter.toHTTP(ticket));
  }
}
