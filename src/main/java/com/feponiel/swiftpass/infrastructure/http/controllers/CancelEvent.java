package com.feponiel.swiftpass.infrastructure.http.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feponiel.swiftpass.domain.application.usecases.CancelEventUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events/{eventId}/cancel")
@RequiredArgsConstructor
public class CancelEvent {
  private final CancelEventUseCase cancelEventUseCase;

  @PostMapping
  public ResponseEntity<Void> handle(@PathVariable UUID eventId) {
    this.cancelEventUseCase.execute(eventId);

    return ResponseEntity.ok().build();
  }
}
