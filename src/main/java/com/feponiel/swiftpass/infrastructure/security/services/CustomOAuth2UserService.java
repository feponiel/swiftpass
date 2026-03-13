package com.feponiel.swiftpass.infrastructure.security.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.feponiel.swiftpass.domain.application.usecases.FindOrCreateUserByGoogle;
import com.feponiel.swiftpass.domain.business.entities.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OidcUserRequest, OidcUser> {
  private final FindOrCreateUserByGoogle findOrCreateUserByGoogle;
  private final OidcUserService delegate = new OidcUserService();

  @Override
  public OidcUser loadUser(OidcUserRequest request) {
    OidcUser oidcUser = delegate.loadUser(request);

    String providerId = oidcUser.getAttribute("sub");
    String name = oidcUser.getAttribute("name");
    String email = oidcUser.getAttribute("email");
    String pictureUrl = oidcUser.getAttribute("picture");

    User user = this.findOrCreateUserByGoogle.execute(providerId, name, email, pictureUrl);

    Map<String, Object> attributes = new HashMap<>(oidcUser.getAttributes());
    attributes.put("id", user.getId());
    attributes.put("name", user.getName());
    attributes.put("picture", user.getPictureUrl());
    attributes.put("role", user.getRole().name());
    attributes.put("createdAt", user.getCreatedAt());
    attributes.put("updatedAt", user.getUpdatedAt());
    attributes.put("editedAt", user.getEditedAt());

    List<GrantedAuthority> authorities = List.of(
      new SimpleGrantedAuthority("ROLE_" + user.getRole())
    );

    return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo(), "sub") {
      @Override
      public Map<String, Object> getAttributes() {
        return attributes;
      }
    };
  }
}