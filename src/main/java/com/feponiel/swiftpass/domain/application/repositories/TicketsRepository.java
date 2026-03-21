package com.feponiel.swiftpass.domain.application.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.feponiel.swiftpass.domain.business.entities.Ticket;

public interface TicketsRepository {
  void create(Ticket ticket);
  Optional<Ticket> findById(UUID id);
  Optional<Ticket> findByIdWithLock(UUID id);
  List<Ticket> listAllByEventId(UUID eventId);
  void update(Ticket ticket);
  void deleteById(UUID id);
}
