package com.feponiel.swiftpass.infrastructure.http.payloads;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EditTicketPayload {
  @Size(min = 2, max = 100)
  private String name;

  @Size(min = 10, max = 2_000)
  private String description;

  @Min(0)
  private BigDecimal price;

  @Pattern(regexp = "^[A-Z]{3}$")
  private String currency;

  @Min(1)
  private Integer capacity;
}
