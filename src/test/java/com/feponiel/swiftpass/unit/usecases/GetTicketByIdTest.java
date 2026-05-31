package com.feponiel.swiftpass.unit.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import com.feponiel.swiftpass.domain.application.usecases.GetTicketByIdUseCase;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.TicketNotFoundException;
import com.feponiel.swiftpass.domain.business.entities.Ticket;
import com.feponiel.swiftpass.factories.TicketFactory;

public class GetTicketByIdTest {
  @Mock private TicketsRepository ticketsRepository;

  @InjectMocks
  private GetTicketByIdUseCase getTicketByIdUseCase;

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
  void shouldGetTicketById() {
    Ticket ticket = TicketFactory.make();

    when(ticketsRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));

    Ticket result = getTicketByIdUseCase.execute(ticket.getId());

    assertThat(result).isEqualTo(ticket);
  }

  @Test
  void shouldNotGetTicketWhenIdDoesNotExist() {
    UUID randomId = UUID.randomUUID();

    when(ticketsRepository.findById(randomId)).thenReturn(Optional.empty());

    assertThrows(TicketNotFoundException.class, () -> getTicketByIdUseCase.execute(randomId));
  }
}
