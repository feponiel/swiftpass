package com.feponiel.swiftpass.unit.usecases;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.feponiel.swiftpass.domain.application.repositories.EventsRepository;
import com.feponiel.swiftpass.domain.application.usecases.DeleteEventUseCase;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.EventNotFoundException;
import com.feponiel.swiftpass.domain.business.entities.Event;
import com.feponiel.swiftpass.factories.EventFactory;
import com.feponiel.swiftpass.unit.UnitTest;

public class DeleteEventTest extends UnitTest {
  @Mock private EventsRepository eventsRepository;

  @InjectMocks
  private DeleteEventUseCase deleteEventUseCase;

  @Test
  void shouldDeleteEvent() {
    Event event = EventFactory.make();

    when(eventsRepository.findById(event.getId())).thenReturn(Optional.of(event));

    deleteEventUseCase.execute(event.getId());

    verify(eventsRepository, times(1)).deleteById(event.getId());
  }

  @Test
  void shouldNotDeleteEventWhenItDoesNotExist() {
    UUID randomId = UUID.randomUUID();

    when(eventsRepository.findById(randomId)).thenReturn(Optional.empty());

    assertThrows(EventNotFoundException.class, () -> deleteEventUseCase.execute(randomId));

    verify(eventsRepository, never()).deleteById(randomId);
  }
}
