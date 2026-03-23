package com.feponiel.swiftpass.infrastructure.http.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feponiel.swiftpass.domain.application.usecases.ListRegistrationsByRegistrantIdUseCase;
import com.feponiel.swiftpass.domain.business.entities.Registration;
import com.feponiel.swiftpass.infrastructure.http.presenters.ListOwnRegistrationsPresenter;
import com.feponiel.swiftpass.infrastructure.http.presenters.dtos.RegistrationListHTTPResponseModel;
import com.feponiel.swiftpass.infrastructure.http.utils.AuthenticatedUserInfoExtractor;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/me/registrations")
@RequiredArgsConstructor
public class ListOwnRegistrations {
  private final ListRegistrationsByRegistrantIdUseCase listRegistrationsByRegistrantIdUseCase;

  @GetMapping
  public ResponseEntity<RegistrationListHTTPResponseModel> handle(@AuthenticationPrincipal OAuth2User authenticatedUser) {
    UUID authenticatedUserId = AuthenticatedUserInfoExtractor.extractId(authenticatedUser);

    List<Registration> registrations = this.listRegistrationsByRegistrantIdUseCase.execute(authenticatedUserId);

    return ResponseEntity.ok().body(ListOwnRegistrationsPresenter.toHTTP(registrations));
  }
}
