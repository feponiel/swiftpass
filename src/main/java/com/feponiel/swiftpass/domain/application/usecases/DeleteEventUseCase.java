package com.feponiel.swiftpass.domain.application.usecases;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.feponiel.swiftpass.domain.application.repositories.EventsRepository;
import com.feponiel.swiftpass.domain.application.repositories.RegistrationsRepository;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.ActiveEventDeletionNotAllowedException;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.EventNotFoundException;
import com.feponiel.swiftpass.domain.business.entities.Event;
import com.feponiel.swiftpass.domain.business.entities.Registration;
import com.feponiel.swiftpass.domain.business.valueobjects.EventStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteEventUseCase {
  private final EventsRepository eventsRepository;
  private final RegistrationsRepository registrationsRepository;

  public void execute(UUID eventId) {
    Event event = this.eventsRepository.findById(eventId)
      .orElseThrow(EventNotFoundException::new);

    List<Registration> eventRegistrationList = registrationsRepository.listAllByEventIdAndPaymentStatus(eventId, null);

    if (eventRegistrationList.size() > 0 && event.getStatus() != EventStatus.CANCELED) {
      throw new ActiveEventDeletionNotAllowedException();
    }

    this.eventsRepository.deleteById(eventId);
  }
}
