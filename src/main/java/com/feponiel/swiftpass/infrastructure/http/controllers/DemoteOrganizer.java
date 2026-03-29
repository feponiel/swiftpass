package com.feponiel.swiftpass.infrastructure.http.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feponiel.swiftpass.domain.application.usecases.DemoteOrganizerUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@Secured("ROLE_ORGANIZER")
@RequestMapping("/users/{userId}/demote")
@RequiredArgsConstructor
public class DemoteOrganizer {
  private final DemoteOrganizerUseCase demoteOrganizerUseCase;

  public ResponseEntity<Void> handle(@PathVariable UUID userId) {
    this.demoteOrganizerUseCase.execute(userId);

    return ResponseEntity.noContent().build();
  }
}
