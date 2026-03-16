package com.feponiel.swiftpass.infrastructure.http.payloads;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTicketPayload {
  @NotNull
  @Size(min = 2, max = 100)
  private String name;

  @Size(min = 10, max = 2_000)
  private String description;

  @NotNull
  @Min(0)
  private BigDecimal price;

  @NotNull
  @Pattern(regexp = "^[A-Z]{3}$")
  private String currency;

  @NotNull
  private Integer amountAvailable;
}
