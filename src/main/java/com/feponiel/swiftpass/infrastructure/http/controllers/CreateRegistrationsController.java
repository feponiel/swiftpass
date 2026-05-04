package com.feponiel.swiftpass.infrastructure.http.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feponiel.swiftpass.domain.application.usecases.CreateRegistrationsUseCase;
import com.feponiel.swiftpass.domain.business.entities.Registration;
import com.feponiel.swiftpass.infrastructure.http.mappers.RegistrationItemsListMapper;
import com.feponiel.swiftpass.infrastructure.http.payloads.CreateRegistrationsPayload;
import com.feponiel.swiftpass.infrastructure.http.presenters.CreateRegistrationsPresenter;
import com.feponiel.swiftpass.infrastructure.http.presenters.dtos.RegistrationListHTTPResponseModel;
import com.feponiel.swiftpass.infrastructure.http.utils.AuthenticatedUserInfoExtractor;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/registrations")
@RequiredArgsConstructor
public class CreateRegistrationsController {
  private final CreateRegistrationsUseCase createRegistrationsUseCase;
  private final RegistrationItemsListMapper registrationItemsListMapper;

  @PostMapping
  public ResponseEntity<RegistrationListHTTPResponseModel> handle(
    @AuthenticationPrincipal OAuth2User authenticatedUser,
    @Valid @RequestBody CreateRegistrationsPayload payload
  ) {
    UUID authenticatedUserId = AuthenticatedUserInfoExtractor.extractId(authenticatedUser);

    List<Registration> registrations = this.createRegistrationsUseCase.execute(
      authenticatedUserId,
      registrationItemsListMapper.toBoundary(payload.getRegistrationItems())
    );

    return ResponseEntity.status(201).body(CreateRegistrationsPresenter.toHTTP(registrations));
  }
}
