package com.feponiel.swiftpass.infrastructure.http.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feponiel.swiftpass.domain.application.usecases.DeleteEventUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@Secured("ROLE_ORGANIZER")
@RequestMapping("/events/{eventId}")
@RequiredArgsConstructor
public class DeleteEventController {
  private final DeleteEventUseCase deleteEventUseCase;

  @DeleteMapping
  public ResponseEntity<Void> handle(@PathVariable UUID eventId) {
    this.deleteEventUseCase.execute(eventId);

    return ResponseEntity.noContent().build();
  }
}
