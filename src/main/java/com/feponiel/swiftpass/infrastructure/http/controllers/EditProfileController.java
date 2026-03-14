package com.feponiel.swiftpass.infrastructure.http.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feponiel.swiftpass.domain.application.usecases.EditProfileUseCase;
import com.feponiel.swiftpass.infrastructure.http.payloads.EditProfilePayload;
import com.feponiel.swiftpass.infrastructure.http.utils.AuthenticatedUserInfoExtractor;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class EditProfileController {
  private final EditProfileUseCase editProfileUseCase;

  @PatchMapping
  public ResponseEntity<Void> handle(
    @AuthenticationPrincipal OAuth2User authenticatedUser,
    @Valid @RequestBody EditProfilePayload payload
  ) {
    UUID authenticatedUserId = AuthenticatedUserInfoExtractor.extractId(authenticatedUser);

    this.editProfileUseCase.execute(
      authenticatedUserId,
      payload.getName(),
      payload.getPictureUrl()
    );

    return ResponseEntity.noContent().build();
  }
}
