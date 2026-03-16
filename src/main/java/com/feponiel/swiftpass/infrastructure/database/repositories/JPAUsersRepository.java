package com.feponiel.swiftpass.infrastructure.database.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.feponiel.swiftpass.domain.application.repositories.UsersRepository;
import com.feponiel.swiftpass.domain.business.entities.User;
import com.feponiel.swiftpass.infrastructure.database.entities.JPAUser;
import com.feponiel.swiftpass.infrastructure.mappers.UserMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class JPAUsersRepository implements UsersRepository {
  @PersistenceContext private EntityManager entityManager;
  private final UserMapper userMapper;

  @Override
  public void create(User user) {
    JPAUser parsedUser = userMapper.toJPA(user);

    entityManager.persist(parsedUser);
  }

  @Override
  public Optional<User> findById(UUID id) {
    return this.entityManager
      .createQuery("SELECT user FROM JPAUser user WHERE user.id = :id", JPAUser.class)
      .setParameter("id", id)
      .getResultStream()
      .findFirst()
      .map(userMapper::toDomain);
  }

  @Override
  public Optional<User> findByProviderId(String providerId) {
    return this.entityManager
      .createQuery("SELECT user FROM JPAUser user WHERE user.providerId = :providerId", JPAUser.class)
      .setParameter("providerId", providerId)
      .getResultStream()
      .findFirst()
      .map(userMapper::toDomain);
  }

  @Override
  public void update(User user) {
    this.entityManager
      .createQuery("UPDATE JPAUser user SET user.name = :name, user.pictureUrl = :pictureUrl, user.role = :role, user.updatedAt = :updatedAt, user.editedAt = :editedAt WHERE user.id = :id")
      .setParameter("name", user.getName())
      .setParameter("pictureUrl", user.getPictureUrl())
      .setParameter("role", user.getRole())
      .setParameter("updatedAt", user.getUpdatedAt())
      .setParameter("editedAt", user.getEditedAt())
      .setParameter("id", user.getId())
      .executeUpdate();
  }

  @Override
  public void deleteById(UUID id) {
    this.entityManager
      .createQuery("DELETE FROM JPAUser user WHERE user.id = :id")
      .setParameter("id", id)
      .executeUpdate();
  }
}
