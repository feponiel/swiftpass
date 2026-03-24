package com.feponiel.swiftpass.infrastructure.http.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feponiel.swiftpass.domain.application.usecases.RefundRegistrationUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@Secured("ROLE_ORGANIZER")
@RequestMapping("/registrations/{registrationId}/refund")
@RequiredArgsConstructor
public class RefundRegistration {
  private final RefundRegistrationUseCase refundRegistrationUseCase;

  @PostMapping
  public ResponseEntity<Void> handle(@PathVariable UUID registrationId) {
    this.refundRegistrationUseCase.execute(registrationId);

    return ResponseEntity.noContent().build();
  }
}
