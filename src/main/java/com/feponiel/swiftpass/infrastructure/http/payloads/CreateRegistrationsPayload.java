package com.feponiel.swiftpass.infrastructure.http.payloads;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRegistrationsPayload {
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class RegistrationItem {
    @NotNull
    private UUID ticketId;

    @NotNull
    @Size(min = 2, max = 100, message = "Holder name must be between 2 to 100 characters")
    private String holderName;
  }

  @Valid
  @NotNull
  private List<RegistrationItem> registrationItems;
}
