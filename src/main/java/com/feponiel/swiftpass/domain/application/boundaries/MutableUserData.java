package com.feponiel.swiftpass.domain.application.boundaries;

import java.time.Instant;

import com.feponiel.swiftpass.domain.business.valueobjects.Role;

public record MutableUserData(
  String name,
  String pictureUrl,
  Role role,
  Instant updatedAt,
  Instant editedAt
) {}
