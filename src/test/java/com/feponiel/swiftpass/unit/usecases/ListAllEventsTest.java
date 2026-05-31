package com.feponiel.swiftpass.unit.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.feponiel.swiftpass.domain.application.repositories.EventsRepository;
import com.feponiel.swiftpass.domain.application.usecases.ListAllEventsUseCase;
import com.feponiel.swiftpass.domain.business.entities.Event;
import com.feponiel.swiftpass.factories.EventFactory;

public class ListAllEventsTest {
  @Mock private EventsRepository eventsRepository;

  @InjectMocks
  private ListAllEventsUseCase listAllEventsUseCase;

  private AutoCloseable mocks;

  @BeforeEach
  void setup() {
    mocks = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }

  @Test
  void shouldListAllEvents() {
    Event event1 = EventFactory.make();
    Event event2 = EventFactory.make();
    Event event3 = EventFactory.make();

    List<Event> eventsList = List.of(event1, event2, event3);

    when(eventsRepository.listAll()).thenReturn(eventsList);

    var events = listAllEventsUseCase.execute();

    assertThat(events).isEqualTo(eventsList);
  }

  @Test
  void shouldReturnEmptyListWhenNoEventsExist() {
    when(eventsRepository.listAll()).thenReturn(List.of());

    var events = listAllEventsUseCase.execute();

    assertThat(events).isEmpty();
  }
}
