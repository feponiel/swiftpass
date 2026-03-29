package com.feponiel.swiftpass.infrastructure.http.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feponiel.swiftpass.domain.application.usecases.PromoteUserToOrganizerUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@Secured("ROLE_ORGANIZER")
@RequestMapping("/users/{userId}/promote")
@RequiredArgsConstructor
public class PromoteUserToOrganizer {
  private PromoteUserToOrganizerUseCase promoteUserToOrganizerUseCase;

  public ResponseEntity<Void> handle(@PathVariable UUID userId) {
    this.promoteUserToOrganizerUseCase.execute(userId);

    return ResponseEntity.noContent().build();
  }
}
