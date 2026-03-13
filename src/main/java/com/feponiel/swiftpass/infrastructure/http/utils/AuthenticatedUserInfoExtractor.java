package com.feponiel.swiftpass.infrastructure.http.utils;

import java.util.UUID;

import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthenticatedUserInfoExtractor {
  public static UUID extractId(OAuth2User authenticatedUser) {
    return (UUID) authenticatedUser.getAttribute("id");
  }
}
