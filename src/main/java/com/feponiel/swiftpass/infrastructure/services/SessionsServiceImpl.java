package com.feponiel.swiftpass.infrastructure.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

import com.feponiel.swiftpass.domain.application.boundaries.MutableUserData;
import com.feponiel.swiftpass.domain.application.services.SessionsService;

@Service
public class SessionsServiceImpl implements SessionsService {
  public void updateSessionUser(MutableUserData mutableUserData) {
    OAuth2AuthenticationToken auth = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

    Map<String, Object> attributes = new HashMap<>(auth.getPrincipal().getAttributes());
    attributes.put("name", mutableUserData.name());
    attributes.put("picture", mutableUserData.pictureUrl());
    attributes.put("role", mutableUserData.role().name());
    attributes.put("updatedAt", mutableUserData.updatedAt());
    attributes.put("editedAt", mutableUserData.editedAt());

    List<GrantedAuthority> authorities = List.of(
      new SimpleGrantedAuthority("ROLE_" + mutableUserData.role())
    );

    DefaultOAuth2User updatedUser = new DefaultOAuth2User(
      authorities,
      attributes,
      "sub"
    );

    OAuth2AuthenticationToken updatedAuthToken = new OAuth2AuthenticationToken(
      updatedUser,
      authorities,
      auth.getAuthorizedClientRegistrationId()
    );

    SecurityContextHolder.getContext().setAuthentication(updatedAuthToken);
  }
}
