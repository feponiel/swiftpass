package com.feponiel.swiftpass.infrastructure.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.feponiel.swiftpass.infrastructure.security.services.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
  @Value("${application.front-end-url}")
  private String frontEndUrl;

  @Autowired
  private CustomOAuth2UserService customOAuth2UserService;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/login", "/error").permitAll()
        .requestMatchers(HttpMethod.GET, "/events", "/events/*").permitAll()
        .requestMatchers(HttpMethod.POST, "/webhooks/stripe").permitAll()
        .anyRequest().authenticated()
      )
      .oauth2Login(oauth2 -> oauth2
        .userInfoEndpoint(userInfo -> userInfo
          .oidcUserService(customOAuth2UserService)
        )
        .defaultSuccessUrl(frontEndUrl, true)
        .failureUrl(frontEndUrl + "/login?error")
      )
      .logout(logout -> logout
        .logoutUrl("/logout")
        .logoutSuccessUrl(frontEndUrl)
        .invalidateHttpSession(true)
      );

    return http.build();
  }
}
