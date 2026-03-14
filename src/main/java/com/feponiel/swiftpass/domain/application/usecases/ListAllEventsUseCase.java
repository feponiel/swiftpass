package com.feponiel.swiftpass.domain.application.usecases;

import java.util.List;

import org.springframework.stereotype.Service;

import com.feponiel.swiftpass.domain.application.repositories.EventsRepository;
import com.feponiel.swiftpass.domain.business.entities.Event;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListAllEventsUseCase {
  private final EventsRepository eventsRepository;

  public List<Event> execute() {
    return eventsRepository.listAll();
  }
}
