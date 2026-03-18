package com.feponiel.swiftpass.domain.application.boundaries;

import java.util.UUID;

public record RegistrationItemData(
  UUID ticketId,
  String holderName
) {}
