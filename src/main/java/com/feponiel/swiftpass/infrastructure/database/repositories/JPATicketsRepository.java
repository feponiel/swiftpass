package com.feponiel.swiftpass.infrastructure.database.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.feponiel.swiftpass.domain.application.repositories.TicketsRepository;
import com.feponiel.swiftpass.domain.business.entities.Ticket;
import com.feponiel.swiftpass.infrastructure.database.entities.JPATicket;
import com.feponiel.swiftpass.infrastructure.mappers.TicketMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class JPATicketsRepository implements TicketsRepository {
  @PersistenceContext private EntityManager entityManager;
  private final TicketMapper ticketMapper;

  public void create(Ticket ticket) {
    JPATicket parsedTicket = ticketMapper.toJPA(ticket);

    this.entityManager.persist(parsedTicket);
  }

  public Optional<Ticket> findById(UUID id) {
    return this.entityManager
      .createQuery("SELECT ticket FROM JPATicket ticket WHERE ticket.id = :id", JPATicket.class)
      .setParameter("id", id)
      .getResultStream()
      .findFirst()
      .map(ticketMapper::toDomain);
  }

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  public Optional<Ticket> findByIdWithLock(UUID id) {
    return this.entityManager
      .createQuery("SELECT ticket FROM JPATicket ticket WHERE ticket.id = :id", JPATicket.class)
      .setParameter("id", id)
      .getResultStream()
      .findFirst()
      .map(ticketMapper::toDomain);
  }

  public List<Ticket> listAllByEventId(UUID eventId) {
    return this.entityManager
      .createQuery("SELECT ticket FROM JPATicket ticket WHERE ticket.eventId = :eventId", JPATicket.class)
      .setParameter("eventId", eventId)
      .getResultList()
      .stream()
      .map(ticketMapper::toDomain)
      .toList();
  }

  public void update(Ticket ticket) {
    this.entityManager
      .createQuery("UPDATE JPATicket ticket SET ticket.name = :name, ticket.description = :description, ticket.price = :price, ticket.currency = :currency, ticket.capacity = :capacity ticket.updatedAt = :updatedAt, ticket.editedAt = :editedAt WHERE ticket.id = :id")
      .setParameter("name", ticket.getName())
      .setParameter("description", ticket.getDescription())
      .setParameter("price", ticket.getPrice())
      .setParameter("currency", ticket.getCurrency())
      .setParameter("capacity", ticket.getCapacity())
      .setParameter("updatedAt", ticket.getUpdatedAt())
      .setParameter("editedAt", ticket.getEditedAt())
      .setParameter("id", ticket.getId())
      .executeUpdate();
  }

  public void deleteById(UUID id) {
    this.entityManager
      .createQuery("DELETE JPATicket ticket WHERE ticket.id = :id")
      .setParameter("id", id)
      .executeUpdate();
  }
}
