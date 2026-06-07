package com.feponiel.swiftpass.unit.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.feponiel.swiftpass.domain.application.repositories.EventsRepository;
import com.feponiel.swiftpass.domain.application.repositories.RegistrationsRepository;
import com.feponiel.swiftpass.domain.application.services.StripeService;
import com.feponiel.swiftpass.domain.application.usecases.CancelEventUseCase;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.EventNotFoundException;
import com.feponiel.swiftpass.domain.business.entities.Event;
import com.feponiel.swiftpass.domain.business.entities.Registration;
import com.feponiel.swiftpass.domain.business.valueobjects.EventStatus;
import com.feponiel.swiftpass.domain.business.valueobjects.PaymentStatus;
import com.feponiel.swiftpass.factories.EventFactory;
import com.feponiel.swiftpass.factories.RegistrationFactory;
import com.feponiel.swiftpass.unit.UnitTest;

class CancelEventTest extends UnitTest {
  @Mock private RegistrationsRepository registrationsRepository;
  @Mock private EventsRepository eventsRepository;
  @Mock private StripeService stripeService;

  @InjectMocks
  private CancelEventUseCase cancelEventUseCase;

  @Test
  void shouldMarkEventAsCanceledAndCloseItsSales() {
    Event event = EventFactory.make();

    when(eventsRepository.findById(event.getId())).thenReturn(Optional.of(event));
    when(registrationsRepository.listAllByEventIdAndPaymentStatus(any(), any())).thenReturn(List.of());
    when(stripeService.expireSessionsInBatch(any())).thenReturn(CompletableFuture.completedFuture(null));
    when(stripeService.processRefundsInBatch(any())).thenReturn(CompletableFuture.completedFuture(null));

    cancelEventUseCase.execute(event.getId());

    ArgumentCaptor<Event> captor = ArgumentCaptor.captor();
    verify(eventsRepository, times(1)).update(captor.capture());

    Event updatedEvent = captor.getValue();
    assertThat(updatedEvent.getStatus()).isEqualTo(EventStatus.CANCELED);
    assertThat(updatedEvent.getSalesOpen()).isFalse();
  }

  @Test
  void shouldSendPendingRegistrationsToExpirationWhenEventIsCanceled() {
    Event event = EventFactory.make();
    Registration registration1 = RegistrationFactory.make(registration -> registration.eventId(event.getId()));
    Registration registration2 = RegistrationFactory.make(registration -> registration.eventId(event.getId()));

    when(eventsRepository.findById(event.getId())).thenReturn(Optional.of(event));
    when(registrationsRepository.listAllByEventIdAndPaymentStatus(event.getId(), PaymentStatus.PENDING)).thenReturn(List.of(registration1, registration2));
    when(registrationsRepository.listAllByEventIdAndPaymentStatus(event.getId(), PaymentStatus.PAID)).thenReturn(List.of());
    when(stripeService.expireSessionsInBatch(any())).thenReturn(CompletableFuture.completedFuture(null));
    when(stripeService.processRefundsInBatch(any())).thenReturn(CompletableFuture.completedFuture(null));

    cancelEventUseCase.execute(event.getId());

    ArgumentCaptor<List<String>> captor = ArgumentCaptor.captor();
    verify(stripeService, times(1)).expireSessionsInBatch(captor.capture());

    List<String> expiredSessionIds = captor.getValue();
    assertThat(expiredSessionIds).containsExactlyInAnyOrder(
      registration1.getStripeSessionId(),
      registration2.getStripeSessionId()
    );
  }

  @Test
  void shouldRefundPaidRegistrationsWhenEventIsCanceled() {
    Event event = EventFactory.make();
    Registration registration1 = RegistrationFactory.make(registration -> registration.eventId(event.getId()).paymentStatus(PaymentStatus.PAID));
    Registration registration2 = RegistrationFactory.make(registration -> registration.eventId(event.getId()).paymentStatus(PaymentStatus.PAID));

    when(eventsRepository.findById(event.getId())).thenReturn(Optional.of(event));
    when(registrationsRepository.listAllByEventIdAndPaymentStatus(event.getId(), PaymentStatus.PENDING)).thenReturn(List.of());
    when(registrationsRepository.listAllByEventIdAndPaymentStatus(event.getId(), PaymentStatus.PAID)).thenReturn(List.of(registration1, registration2));
    when(stripeService.expireSessionsInBatch(any())).thenReturn(CompletableFuture.completedFuture(null));
    when(stripeService.processRefundsInBatch(any())).thenReturn(CompletableFuture.completedFuture(null));

    cancelEventUseCase.execute(event.getId());

    ArgumentCaptor<List<String>> captor = ArgumentCaptor.captor();
    verify(stripeService, times(1)).processRefundsInBatch(captor.capture());

    assertThat(captor.getValue()).containsExactlyInAnyOrder(
      registration1.getStripeSessionId(),
      registration2.getStripeSessionId()
    );

    verify(registrationsRepository, times(1)).updateFromPaidToRefundedByEventId(event.getId());
  }

  @Test
  void shouldNotCancelAnEventThatDoesNotExist() {
    UUID randomId = UUID.randomUUID();

    when(eventsRepository.findById(randomId)).thenReturn(Optional.empty());

    assertThrows(EventNotFoundException.class, () -> cancelEventUseCase.execute(randomId));
  }
}
