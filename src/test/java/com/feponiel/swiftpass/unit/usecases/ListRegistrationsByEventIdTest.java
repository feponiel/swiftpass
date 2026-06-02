package com.feponiel.swiftpass.unit.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.feponiel.swiftpass.domain.application.repositories.RegistrationsRepository;
import com.feponiel.swiftpass.domain.application.usecases.ListRegistrationsByEventIdUseCase;
import com.feponiel.swiftpass.domain.business.entities.Event;
import com.feponiel.swiftpass.domain.business.entities.Registration;
import com.feponiel.swiftpass.domain.business.valueobjects.PaymentStatus;
import com.feponiel.swiftpass.factories.EventFactory;
import com.feponiel.swiftpass.factories.RegistrationFactory;

public class ListRegistrationsByEventIdTest {
  @Mock private RegistrationsRepository registrationsRepository;

  @InjectMocks
  private ListRegistrationsByEventIdUseCase listRegistrationsByEventId;

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
  void shoudListRegistrationsByEventId() {
    Event event = EventFactory.make();
    Registration registration1 = RegistrationFactory.make(r -> r.eventId(event.getId()).paymentStatus(PaymentStatus.PENDING));
    Registration registration2 = RegistrationFactory.make(r -> r.eventId(event.getId()).paymentStatus(PaymentStatus.PENDING));

    when(registrationsRepository.listAllByEventIdAndPaymentStatus(event.getId(), PaymentStatus.PENDING)).thenReturn(List.of(registration1, registration2));

    var result = listRegistrationsByEventId.execute(event.getId(), PaymentStatus.PENDING);

    assertThat(result).containsExactlyInAnyOrder(registration1, registration2);
  }

  @Test
  void shouldReturnEmptyListWhenEventHasNoRegistrations() {
    UUID eventId = UUID.randomUUID();

    when(registrationsRepository.listAllByEventIdAndPaymentStatus(eventId, PaymentStatus.PENDING)).thenReturn(List.of());

    var result = listRegistrationsByEventId.execute(eventId, PaymentStatus.PENDING);

    assertThat(result).isEmpty();
  }

  @Test
  void shouldListRegistrationsEvenIfTheEventDoesNotExistAnymore() {
    UUID idFromDeletedEvent = UUID.randomUUID();
    Registration registration1 = RegistrationFactory.make(r -> r.eventId(idFromDeletedEvent).paymentStatus(PaymentStatus.PENDING));
    Registration registration2 = RegistrationFactory.make(r -> r.eventId(idFromDeletedEvent).paymentStatus(PaymentStatus.PENDING));

    when(registrationsRepository.listAllByEventIdAndPaymentStatus(idFromDeletedEvent, PaymentStatus.PENDING)).thenReturn(List.of(registration1, registration2));

    var result = listRegistrationsByEventId.execute(idFromDeletedEvent, PaymentStatus.PENDING);

    assertThat(result).containsExactlyInAnyOrder(registration1, registration2);
  }
}
