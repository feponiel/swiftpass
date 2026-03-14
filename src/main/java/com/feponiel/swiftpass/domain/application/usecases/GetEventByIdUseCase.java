package com.feponiel.swiftpass.domain.application.usecases;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.feponiel.swiftpass.domain.application.repositories.EventsRepository;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.EventNotFoundException;
import com.feponiel.swiftpass.domain.business.entities.Event;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetEventByIdUseCase {
  private final EventsRepository eventsRepository;

  public Event execute(UUID eventId) {
    return this.eventsRepository.findById(eventId)
      .orElseThrow(EventNotFoundException::new);
  }
}
