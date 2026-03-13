package com.feponiel.swiftpass.infrastructure.http.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feponiel.swiftpass.infrastructure.http.presenters.GetProfilePresenter;

@RestController
@RequestMapping("/me")
public class GetProfile {
  @GetMapping
  public ResponseEntity<GetProfilePresenter> handle(@AuthenticationPrincipal OAuth2User authenticatedUser) {
    var authenticatedUserProps = authenticatedUser.getAttributes();

    return ResponseEntity.ok(GetProfilePresenter.toHTTP(authenticatedUserProps));
  }
}
