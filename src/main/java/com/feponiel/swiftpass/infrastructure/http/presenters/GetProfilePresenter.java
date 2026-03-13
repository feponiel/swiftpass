package com.feponiel.swiftpass.infrastructure.http.presenters;

import java.time.Instant;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GetProfilePresenter {
  private final String id;
  private final String name;
  private final String email;
  private final String pictureUrl;
  private final String role;
  private final Instant createdAt;
  private final Instant updatedAt;
  private final Instant editedAt;

  public static GetProfilePresenter toHTTP(Map<String, Object> authenticatedUserProps) {
    return new GetProfilePresenter(
      authenticatedUserProps.get("id").toString(),
      (String) authenticatedUserProps.get("name"),
      (String) authenticatedUserProps.get("email"),
      (String) authenticatedUserProps.get("picture"),
      (String) authenticatedUserProps.get("role"),
      (Instant) authenticatedUserProps.get("createdAt"),
      (Instant) authenticatedUserProps.get("updatedAt"),
      (Instant) authenticatedUserProps.get("editedAt")
    );
  }
}
