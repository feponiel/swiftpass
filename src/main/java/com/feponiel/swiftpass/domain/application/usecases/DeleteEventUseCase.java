package com.feponiel.swiftpass.domain.application.usecases;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.feponiel.swiftpass.domain.application.repositories.EventsRepository;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.EventNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteEventUseCase {
  private final EventsRepository eventsRepository;

  public void execute(UUID eventId) {
    this.eventsRepository.findById(eventId)
      .orElseThrow(EventNotFoundException::new);

    this.eventsRepository.deleteById(eventId);
  }
}
