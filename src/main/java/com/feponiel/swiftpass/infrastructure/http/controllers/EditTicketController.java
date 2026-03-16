package com.feponiel.swiftpass.infrastructure.http.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feponiel.swiftpass.domain.application.usecases.EditTicketUseCase;
import com.feponiel.swiftpass.infrastructure.http.payloads.EditTicketPayload;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@Secured("ROLE_ORGANIZER")
@RequestMapping("/tickets/{ticketId}")
@RequiredArgsConstructor
public class EditTicketController {
  private final EditTicketUseCase editTicketUseCase;

  @PatchMapping
  public ResponseEntity<Void> handle(
    @PathVariable UUID ticketId,
    @Valid @RequestBody EditTicketPayload payload
  ) {
    this.editTicketUseCase.execute(
      ticketId,
      payload.getName(),
      payload.getDescription(),
      payload.getPrice(),
      payload.getCurrency(),
      payload.getAmountAvailable()
    );

    return ResponseEntity.noContent().build();
  }
}
