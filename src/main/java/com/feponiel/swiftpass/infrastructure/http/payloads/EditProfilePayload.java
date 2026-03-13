package com.feponiel.swiftpass.infrastructure.http.payloads;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EditProfilePayload {
  @Size(min = 2, max = 100, message = "Name must be between 2 to 100 characters")
  private String name;

  @URL(message = "Picture URL is invalid!")
  @Size(max = 2048, message = "Picture URL is invalid!")
  private String pictureUrl;
}
