package com.feponiel.swiftpass.infrastructure.http.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI swiftPassOpenAPI() {
    return new OpenAPI()
      .info(new Info()
        .title("SwiftPass API")
        .description("A secure and automated event ticketing engine")
        .version("1.0.0"));
  }
}