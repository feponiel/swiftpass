package com.feponiel.swiftpass.infrastructure.mappers;

import org.springframework.stereotype.Component;

import com.feponiel.swiftpass.domain.business.entities.User;
import com.feponiel.swiftpass.infrastructure.database.entities.JPAUser;

@Component
public class UserMapper {
  public User toDomain(JPAUser rawUser) {
    return User.builder()
      .id(rawUser.getId())
      .providerId(rawUser.getProviderId())
      .name(rawUser.getName())
      .email(rawUser.getEmail())
      .pictureUrl(rawUser.getPictureUrl())
      .role(rawUser.getRole())
      .createdAt(rawUser.getCreatedAt())
      .updatedAt(rawUser.getUpdatedAt())
      .editedAt(rawUser.getEditedAt())
      .build();
  }

  public JPAUser toJPA(User rawUser) {
    return new JPAUser(
      rawUser.getId(),
      rawUser.getProviderId(),
      rawUser.getName(),
      rawUser.getEmail(),
      rawUser.getPictureUrl(),
      rawUser.getRole(),
      rawUser.getCreatedAt(),
      rawUser.getUpdatedAt(),
      rawUser.getEditedAt()
    );
  }
}
