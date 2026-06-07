package com.feponiel.swiftpass.unit.usecases;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.feponiel.swiftpass.domain.application.repositories.EventsRepository;
import com.feponiel.swiftpass.domain.application.usecases.CreateEventUseCase;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.EventEndDateBeforeStartDateException;
import com.feponiel.swiftpass.domain.business.entities.Event;
import com.feponiel.swiftpass.factories.EventFactory;
import com.feponiel.swiftpass.unit.UnitTest;

public class CreateEventTest extends UnitTest {
  @Mock private EventsRepository eventsRepository;

  @InjectMocks
  private CreateEventUseCase createEventUseCase;

  @Test
  void shouldCreateEvent() {
    Event eventTemplate = EventFactory.make();

    Event newEvent = createEventUseCase.execute(
      eventTemplate.getHostId(),
      eventTemplate.getName(),
      eventTemplate.getDescription(),
      eventTemplate.getAgeRange(),
      eventTemplate.getSalesOpen(),
      eventTemplate.getAddress().getPostalCode(),
      eventTemplate.getAddress().getCountry(),
      eventTemplate.getAddress().getState(),
      eventTemplate.getAddress().getCity(),
      eventTemplate.getAddress().getAddressLine1(),
      eventTemplate.getAddress().getAddressLine2(),
      eventTemplate.getStartDate(),
      eventTemplate.getEndDate()
    );

    verify(eventsRepository, times(1)).create(newEvent);
  }

  @Test
  void shouldNotCreateEventWhenEndDateIsBeforeStartDate() {
    Event eventTemplate = EventFactory.make();
    
    Instant startDate = Instant.now().plusSeconds(60 * 60 * 24); // tomorrow
    Instant endDate = startDate.minusSeconds(60 * 60 * 3); // 3 hours before the start date

    assertThrows(EventEndDateBeforeStartDateException.class, () -> createEventUseCase.execute(
      eventTemplate.getHostId(),
      eventTemplate.getName(),
      eventTemplate.getDescription(),
      eventTemplate.getAgeRange(),
      eventTemplate.getSalesOpen(),
      eventTemplate.getAddress().getPostalCode(),
      eventTemplate.getAddress().getCountry(),
      eventTemplate.getAddress().getState(),
      eventTemplate.getAddress().getCity(),
      eventTemplate.getAddress().getAddressLine1(),
      eventTemplate.getAddress().getAddressLine2(),
      startDate,
      endDate
    ));
  }
}
