package com.feponiel.swiftpass.infrastructure.http.utils;

import java.net.URI;
import java.util.UUID;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpLocationBuilder {
  public static URI fromResourceId(UUID resourceId) {
    return ServletUriComponentsBuilder
      .fromCurrentRequest()
      .path("/{id}")
      .buildAndExpand(resourceId.toString())
      .toUri();
  }
}
