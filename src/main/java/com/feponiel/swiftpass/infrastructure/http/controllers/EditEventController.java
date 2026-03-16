package com.feponiel.swiftpass.infrastructure.http.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feponiel.swiftpass.domain.application.usecases.EditEventUseCase;
import com.feponiel.swiftpass.infrastructure.http.payloads.EditEventPayload;
import com.feponiel.swiftpass.infrastructure.mappers.EventAddressMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@Secured("ROLE_ORGANIZER")
@RequestMapping("/events/{eventId}")
@RequiredArgsConstructor
public class EditEventController {
  private final EditEventUseCase editEventUseCase;
  private final EventAddressMapper eventAddressMapper;

  @PatchMapping
  public ResponseEntity<Void> handle(
    @PathVariable UUID eventId,
    @Valid @RequestBody EditEventPayload payload
  ) {
    this.editEventUseCase.execute(
      eventId,
      payload.getName(),
      payload.getDescription(),
      payload.getBannerUrl(),
      payload.getAgeRange(),
      eventAddressMapper.toBoundary(payload.getAddress()),
      payload.getStartDate(),
      payload.getEndDate()
    );

    return ResponseEntity.noContent().build();
  }
}
