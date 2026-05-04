package com.feponiel.swiftpass.infrastructure.database.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.feponiel.swiftpass.domain.application.repositories.EventsRepository;
import com.feponiel.swiftpass.domain.business.entities.Event;
import com.feponiel.swiftpass.domain.business.valueobjects.Address;
import com.feponiel.swiftpass.infrastructure.database.entities.JPAEvent;
import com.feponiel.swiftpass.infrastructure.database.mappers.EventMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class JPAEventsRepository implements EventsRepository {
  @PersistenceContext private EntityManager entityManager;
  private final EventMapper eventMapper;

  @Override
  public void create(Event event) {
    JPAEvent parsedEvent = eventMapper.toJPA(event);

    this.entityManager.persist(parsedEvent);
  }

  @Override
  public Optional<Event> findById(UUID id) {
    return this.entityManager
      .createQuery("SELECT event FROM JPAEvent event WHERE event.id = :id", JPAEvent.class)
      .setParameter("id", id)
      .getResultStream()
      .findFirst()
      .map(eventMapper::toDomain);
  }

  @Override
  public List<Event> listAll() {
    return this.entityManager
      .createQuery("SELECT event FROM JPAEvent event", JPAEvent.class)
      .getResultList()
      .stream()
      .map(eventMapper::toDomain)
      .toList();
  }

  @Override
  public void update(Event event) {
    Address eventAddress = event.getAddress();

    this.entityManager
      .createQuery("UPDATE JPAEvent event SET event.name = :name, event.description = :description, event.bannerUrl = :bannerUrl, event.ageRange = :ageRange, event.salesOpen = :salesOpen, event.postalCode = :postalCode, event.country = :country, event.state = :state, event.city = :city, event.addressLine1 = :addressLine1, event.addressLine2 = :addressLine2, event.startDate = :startDate, event.endDate = :endDate, event.updatedAt = :updatedAt, event.editedAt = :editedAt WHERE event.id = :id")
      .setParameter("name", event.getName())
      .setParameter("description", event.getDescription())
      .setParameter("bannerUrl", event.getBannerUrl())
      .setParameter("ageRange", event.getAgeRange())
      .setParameter("salesOpen", event.getSalesOpen())
      .setParameter("postalCode", eventAddress.getPostalCode())
      .setParameter("country", eventAddress.getCountry())
      .setParameter("state", eventAddress.getState())
      .setParameter("city", eventAddress.getCity())
      .setParameter("addressLine1", eventAddress.getAddressLine1())
      .setParameter("addressLine2", eventAddress.getAddressLine2())
      .setParameter("startDate", event.getStartDate())
      .setParameter("endDate", event.getEndDate())
      .setParameter("updatedAt", event.getUpdatedAt())
      .setParameter("editedAt", event.getEditedAt())
      .setParameter("id", event.getId())
      .executeUpdate();
  }

  @Override
  public void deleteById(UUID id) {
    this.entityManager
      .createQuery("DELETE JPAEvent event WHERE event.id = :id")
      .setParameter("id", id)
      .executeUpdate();
  }
}
