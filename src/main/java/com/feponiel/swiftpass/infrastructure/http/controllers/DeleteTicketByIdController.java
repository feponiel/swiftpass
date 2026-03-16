package com.feponiel.swiftpass.infrastructure.http.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feponiel.swiftpass.domain.application.usecases.DeleteTicketByIdUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@Secured("ROLE_ORGANIZER")
@RequestMapping("/tickets/{ticketId}")
@RequiredArgsConstructor
public class DeleteTicketByIdController {
  private final DeleteTicketByIdUseCase deleteTicketByIdUseCase;

  @DeleteMapping
  public ResponseEntity<Void> handle(@PathVariable UUID ticketId) {
    this.deleteTicketByIdUseCase.execute(ticketId);

    return ResponseEntity.noContent().build();
  }
}
