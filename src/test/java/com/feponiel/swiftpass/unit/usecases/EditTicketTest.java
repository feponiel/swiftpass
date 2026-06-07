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

import com.feponiel.swiftpass.domain.application.repositories.TicketsRepository;
import com.feponiel.swiftpass.domain.application.usecases.EditTicketUseCase;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.InvalidCurrencyException;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.NoFieldProvidedToEditException;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.TicketNotFoundException;
import com.feponiel.swiftpass.domain.business.entities.Ticket;
import com.feponiel.swiftpass.factories.TicketFactory;
import com.feponiel.swiftpass.unit.UnitTest;

public class EditTicketTest extends UnitTest {
  @Mock private TicketsRepository ticketsRepository;

  @InjectMocks
  private EditTicketUseCase editTicketUseCase;

  @Test
  void shouldEditTicket() {
    Ticket ticket = TicketFactory.make();

    when(ticketsRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));

    editTicketUseCase.execute(ticket.getId(), "New Ticket Name", null, null, null, null);

    ArgumentCaptor<Ticket> captor = ArgumentCaptor.captor();
    verify(ticketsRepository, times(1)).update(captor.capture());

    assertThat(captor.getValue().getName()).isEqualTo("New Ticket Name");
  }

  @Test
  void shouldNotEditTicketWhenNoFieldsAreProvidedToEdit() {
    UUID randomId = UUID.randomUUID();

    assertThrows(NoFieldProvidedToEditException.class, () -> editTicketUseCase.execute(randomId, null, null, null, null, null));

    verify(ticketsRepository, never()).update(any());
  }

  @Test
  void shouldNotEditTicketWhenCurrencyIsInvalid() {
    Ticket ticket = TicketFactory.make();

    assertThrows(InvalidCurrencyException.class, () -> editTicketUseCase.execute(ticket.getId(), null, null, null, "ZZZ", null));

    verify(ticketsRepository, never()).update(any());
  }

  @Test
  void shouldNotEditTicketWhenItDoesNotExist() {
    UUID randomId = UUID.randomUUID();

    when(ticketsRepository.findById(randomId)).thenReturn(Optional.empty());

    assertThrows(TicketNotFoundException.class, () -> editTicketUseCase.execute(randomId, "New Ticket Name", null, null, null, null));

    verify(ticketsRepository, never()).update(any());
  }
}
