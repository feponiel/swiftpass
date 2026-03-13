package com.feponiel.swiftpass.domain.application.repositories;

import java.util.Optional;
import java.util.UUID;

import com.feponiel.swiftpass.domain.business.entities.User;

public interface UsersRepository {
  void create(User user);
  Optional<User> findById(UUID id);
  Optional<User> findByProviderId(String providerId);
  void update(User user);
  void deleteById(UUID id);
}
