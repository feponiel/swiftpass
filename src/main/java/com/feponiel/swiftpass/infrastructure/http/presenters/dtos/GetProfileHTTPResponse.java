package com.feponiel.swiftpass.infrastructure.http.presenters.dtos;

import java.time.Instant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetProfileHTTPResponse {
  private final String id;
  private final String name;
  private final String email;
  private final String pictureUrl;
  private final String role;
  private final Instant createdAt;
  private final Instant updatedAt;
  private final Instant editedAt;
}
