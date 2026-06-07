package com.feponiel.swiftpass.unit.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.feponiel.swiftpass.domain.application.boundaries.EventAddressData;
import com.feponiel.swiftpass.domain.application.repositories.EventsRepository;
import com.feponiel.swiftpass.domain.application.usecases.EditEventUseCase;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.EventNotFoundException;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.NoFieldProvidedToEditException;
import com.feponiel.swiftpass.domain.business.entities.Event;
import com.feponiel.swiftpass.factories.EventFactory;
import com.feponiel.swiftpass.unit.UnitTest;

public class EditEventTest extends UnitTest {
  @Mock private EventsRepository eventsRepository;

  @InjectMocks
  private EditEventUseCase editEventUseCase;

  @Test
  void shouldEditEvent() {
    Event event = EventFactory.make();

    when(eventsRepository.findById(event.getId())).thenReturn(Optional.of(event));

    editEventUseCase.execute(event.getId(), "New Event Name", null, null, null, null, null, null);

    ArgumentCaptor<Event> captor = ArgumentCaptor.captor();
    verify(eventsRepository, times(1)).update(captor.capture());

    assertThat(captor.getValue().getName()).isEqualTo("New Event Name");
  }

  @Test
  void shouldEditEventWithPartialAddress() {
    Event event = EventFactory.make();
    EventAddressData partialAddress = new EventAddressData("01310-100", null, null, null, null, null);

    when(eventsRepository.findById(event.getId())).thenReturn(Optional.of(event));

    editEventUseCase.execute(event.getId(), null, null, null, null, partialAddress, null, null);

    verify(eventsRepository, times(1)).update(any());
  }

  @Test
  void shouldNotEditAnEventWhenNoFieldsAreProvidedToEdit() {
    UUID randomId = UUID.randomUUID();

    assertThrows(NoFieldProvidedToEditException.class, () -> editEventUseCase.execute(randomId, null, null, null, null, null, null, null));

    verify(eventsRepository, never()).findById(any());
    verify(eventsRepository, never()).update(any());
  }

  @Test
  void shouldNotEditAnEventWhenItDoesNotExist() {
    UUID randomId = UUID.randomUUID();

    when(eventsRepository.findById(randomId)).thenReturn(Optional.empty());

    assertThrows(EventNotFoundException.class, () -> editEventUseCase.execute(randomId, "New Event Name", null, null, null, null, null, null));

    verify(eventsRepository, never()).update(any());
  }
}
