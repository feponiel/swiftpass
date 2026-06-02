package com.feponiel.swiftpass.unit.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.feponiel.swiftpass.domain.application.repositories.RegistrationsRepository;
import com.feponiel.swiftpass.domain.application.services.StripeService;
import com.feponiel.swiftpass.domain.application.usecases.RefundRegistrationUseCase;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.RegistrationNotFoundException;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.RegistrationNotPaidException;
import com.feponiel.swiftpass.domain.business.entities.Registration;
import com.feponiel.swiftpass.domain.business.valueobjects.PaymentStatus;
import com.feponiel.swiftpass.factories.RegistrationFactory;

public class RefundRegistrationTest {
  @Mock private RegistrationsRepository registrationsRepository;
  @Mock private StripeService stripeService;

  @InjectMocks
  private RefundRegistrationUseCase refundRegistrationUseCase;

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
  void shouldRefundRegistration() {
    Registration registration = RegistrationFactory.make(r -> r
      .totalPaid(new BigDecimal("350.55"))
      .paidCurrency("USD")
      .paymentStatus(PaymentStatus.PAID)
    );

    when(registrationsRepository.findById(registration.getId())).thenReturn(Optional.of(registration));

    refundRegistrationUseCase.execute(registration.getId());

    verify(stripeService, times(1)).processPartialRefund(registration.getStripeSessionId(), registration.getTotalPaid());

    ArgumentCaptor<Registration> captor = ArgumentCaptor.captor();
    verify(registrationsRepository, times(1)).update(captor.capture());

    assertThat(captor.getValue().getPaymentStatus()).isEqualTo(PaymentStatus.REFUNDED);
  }

  @Test
  void shouldNotRefundRegistrationWhenItDoesNotExist() {
    UUID randomId = UUID.randomUUID();

    when(registrationsRepository.findById(randomId)).thenReturn(Optional.empty());

    assertThrows(RegistrationNotFoundException.class, () -> refundRegistrationUseCase.execute(randomId));

    verify(stripeService, never()).processPartialRefund(any(), any());
    verify(registrationsRepository, never()).update(any());
  }

  @Test
  void shouldNotRefundAnUnpaidRegistration() {
    Registration registration = RegistrationFactory.make(r -> r.paymentStatus(PaymentStatus.PENDING));

    when(registrationsRepository.findById(registration.getId())).thenReturn(Optional.of(registration));

    assertThrows(RegistrationNotPaidException.class, () -> refundRegistrationUseCase.execute(registration.getId()));

    verify(stripeService, never()).processPartialRefund(any(), any());
    verify(registrationsRepository, never()).update(any());
  }
}
