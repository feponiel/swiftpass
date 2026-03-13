package com.feponiel.swiftpass.domain.business.entities;

import java.time.Instant;
import java.util.UUID;

import com.feponiel.swiftpass.domain.business.valueobjects.Role;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class User extends Entity {
  private String providerId;
  private String name;
  private String email;
  private String pictureUrl;
  private Role role;
  private Instant createdAt;
  private Instant updatedAt;
  private Instant editedAt;

  @Builder
  private User(
    @NonNull UUID id,
    @NonNull String providerId,
    @NonNull String name,
    @NonNull String email,
    String pictureUrl,
    Role role,
    Instant createdAt,
    Instant updatedAt,
    Instant editedAt
  ) {
    super(id);
    this.providerId = providerId;
    this.name = name;
    this.email = email;
    this.pictureUrl = pictureUrl;
    this.role = role != null ? role : Role.DEFAULT;
    this.createdAt = createdAt != null ? createdAt : Instant.now();
    this.updatedAt = updatedAt;
    this.editedAt = editedAt;
  }

  protected void touch() {
    this.updatedAt = Instant.now();
  }

  protected void markEdited() {
    this.touch();

    this.editedAt = Instant.now();
  }

  public User changeName(String newName) {
    if (newName == null)
      return this;

    this.name = newName;
    
    this.markEdited();

    return this;
  }

  public User changePicture(String newPictureUrl) {
    if (newPictureUrl == null)
      return this;

    this.pictureUrl = newPictureUrl;
    
    this.markEdited();

    return this;
  }

  public void updateRole(Role newRole) {
    this.role = newRole;

    this.touch();
  }
}
