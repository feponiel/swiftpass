package com.feponiel.swiftpass.infrastructure.http.presenters;

import java.time.Instant;
import java.util.Map;

import com.feponiel.swiftpass.infrastructure.http.presenters.dtos.ProfileHTTPResponseModel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetProfilePresenter {
  public static ProfileHTTPResponseModel toHTTP(Map<String, Object> authenticatedUserProps) {
    return new ProfileHTTPResponseModel(
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
