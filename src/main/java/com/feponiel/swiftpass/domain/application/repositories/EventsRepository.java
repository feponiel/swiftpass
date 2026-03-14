package com.feponiel.swiftpass.domain.application.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.feponiel.swiftpass.domain.business.entities.Event;

public interface EventsRepository {
  void create(Event event);
  Optional<Event> findById(UUID id);
  List<Event> listAll();
  void update(Event event);
  void deleteById(UUID id);
}
