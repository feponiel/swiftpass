package com.feponiel.swiftpass.infrastructure.http.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feponiel.swiftpass.domain.application.usecases.CancelEventUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@Secured("ROLE_ORGANIZER")
@RequestMapping("/events/{eventId}/cancel")
@RequiredArgsConstructor
public class CancelEventController {
  private final CancelEventUseCase cancelEventUseCase;

  @PostMapping
  public ResponseEntity<Void> handle(@PathVariable UUID eventId) {
    this.cancelEventUseCase.execute(eventId);

    return ResponseEntity.ok().build();
  }
}
