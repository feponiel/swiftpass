package com.feponiel.swiftpass.infrastructure.http.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feponiel.swiftpass.domain.application.usecases.DeleteUser;
import com.feponiel.swiftpass.infrastructure.http.utils.AuthenticatedUserInfoExtractor;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class DeleteOwnAccount {
  private final DeleteUser deleteUser;

  @DeleteMapping
  public ResponseEntity<Void> handle(@AuthenticationPrincipal OAuth2User authenticatedUser) {
    UUID authenticatedUserId = AuthenticatedUserInfoExtractor.extractId(authenticatedUser);

    this.deleteUser.execute(authenticatedUserId, authenticatedUserId);

    return ResponseEntity.noContent().build();
  }
}
