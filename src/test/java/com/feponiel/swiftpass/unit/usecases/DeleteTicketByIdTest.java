package com.feponiel.swiftpass.unit.usecases;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.feponiel.swiftpass.domain.application.repositories.TicketsRepository;
import com.feponiel.swiftpass.domain.application.usecases.DeleteTicketByIdUseCase;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.TicketNotFoundException;
import com.feponiel.swiftpass.domain.business.entities.Ticket;
import com.feponiel.swiftpass.factories.TicketFactory;

public class DeleteTicketByIdTest {
  @Mock private TicketsRepository ticketsRepository;

  @InjectMocks
  private DeleteTicketByIdUseCase deleteTicketByIdUseCase;

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
  void shouldDeleteTicket() {
    Ticket ticket = TicketFactory.make();

    when(ticketsRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));

    deleteTicketByIdUseCase.execute(ticket.getId());

    verify(ticketsRepository, times(1)).deleteById(ticket.getId());
  }

  @Test
  void shouldNotDeleteTicketWhenEventDoesNotExist() {
    UUID randomId = UUID.randomUUID();

    when(ticketsRepository.findById(randomId)).thenReturn(Optional.empty());

    assertThrows(TicketNotFoundException.class, () -> deleteTicketByIdUseCase.execute(randomId));

    verify(ticketsRepository, never()).deleteById(randomId);
  }
}
