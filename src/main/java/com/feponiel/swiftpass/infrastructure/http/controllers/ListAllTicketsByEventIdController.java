package com.feponiel.swiftpass.infrastructure.http.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feponiel.swiftpass.domain.application.usecases.ListAllTicketsByEventIdUseCase;
import com.feponiel.swiftpass.domain.business.entities.Ticket;
import com.feponiel.swiftpass.infrastructure.http.presenters.ListAllTicketsByEventIdPresenter;
import com.feponiel.swiftpass.infrastructure.http.presenters.dtos.TicketListHTTPResponseModel;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events/{eventId}/tickets")
@RequiredArgsConstructor
public class ListAllTicketsByEventIdController {
  private final ListAllTicketsByEventIdUseCase listAllTicketsByEventIdUseCase;

  @GetMapping
  public ResponseEntity<TicketListHTTPResponseModel> handle(@PathVariable UUID eventId) {
    List<Ticket> tickets = this.listAllTicketsByEventIdUseCase.execute(eventId);

    return ResponseEntity.ok(ListAllTicketsByEventIdPresenter.toHTTP(tickets));
  }
}
