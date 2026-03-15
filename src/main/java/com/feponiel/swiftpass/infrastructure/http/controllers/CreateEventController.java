package com.feponiel.swiftpass.infrastructure.http.controllers;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feponiel.swiftpass.domain.application.usecases.CreateEventUseCase;
import com.feponiel.swiftpass.domain.business.entities.Event;
import com.feponiel.swiftpass.infrastructure.http.payloads.CreateNewEventPayload;
import com.feponiel.swiftpass.infrastructure.http.presenters.CreateNewEventPresenter;
import com.feponiel.swiftpass.infrastructure.http.presenters.dtos.EventHTTPResponseModel;
import com.feponiel.swiftpass.infrastructure.http.utils.AuthenticatedUserInfoExtractor;
import com.feponiel.swiftpass.infrastructure.http.utils.HttpLocationBuilder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@Secured("ROLE_ADMIN")
@RequestMapping("/events")
@RequiredArgsConstructor
public class CreateEventController {
  private final CreateEventUseCase createEventUseCase;

  @PostMapping
  public ResponseEntity<EventHTTPResponseModel> handle(
    @AuthenticationPrincipal OAuth2User authenticatedUser,
    @RequestBody @Valid CreateNewEventPayload payload
  ) {
    UUID authenticatedUserId = AuthenticatedUserInfoExtractor.extractId(authenticatedUser);

    Event event = this.createEventUseCase.execute(
      authenticatedUserId,
      payload.getName(),
      payload.getDescription(),
      payload.getAgeRange(),
      payload.getSalesOpen(),
      payload.getAddress().getPostalCode(),
      payload.getAddress().getCountry(),
      payload.getAddress().getState(),
      payload.getAddress().getCity(),
      payload.getAddress().getAddressLine1(),
      payload.getAddress().getAddressLine2(),
      payload.getStartDate(),
      payload.getEndDate()
    );

    URI resourceLocation = HttpLocationBuilder.fromResourceId(event.getId());

    return ResponseEntity
      .created(resourceLocation)
      .body(CreateNewEventPresenter.toHTTP(event));
  }
}
