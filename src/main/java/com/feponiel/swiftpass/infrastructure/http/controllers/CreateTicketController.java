package com.feponiel.swiftpass.infrastructure.http.controllers;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feponiel.swiftpass.domain.application.usecases.CreateTicketUseCase;
import com.feponiel.swiftpass.domain.business.entities.Ticket;
import com.feponiel.swiftpass.infrastructure.http.payloads.CreateTicketPayload;
import com.feponiel.swiftpass.infrastructure.http.presenters.CreateTicketPresenter;
import com.feponiel.swiftpass.infrastructure.http.presenters.dtos.TicketHTTPResponseModel;
import com.feponiel.swiftpass.infrastructure.http.utils.HttpLocationBuilder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@Secured("ROLE_ORGANIZER")
@RequestMapping("/events/{eventId}/tickets")
@RequiredArgsConstructor
public class CreateTicketController {
  private final CreateTicketUseCase createTicketUseCase;

  @PostMapping
  public ResponseEntity<TicketHTTPResponseModel> handle(
    @PathVariable("eventId") UUID eventId,
    @Valid @RequestBody CreateTicketPayload payload
  ) {
    Ticket ticket = this.createTicketUseCase.execute(
      eventId,
      payload.getName(),
      payload.getDescription(),
      payload.getPrice(),
      payload.getCurrency(),
      payload.getAmountAvailable()
    );

    URI resourceLocation = HttpLocationBuilder.fromResourceId(ticket.getId());

    return ResponseEntity.created(resourceLocation).body(CreateTicketPresenter.toHTTP(ticket));
  }
}
