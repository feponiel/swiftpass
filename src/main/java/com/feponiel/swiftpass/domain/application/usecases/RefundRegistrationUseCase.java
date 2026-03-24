package com.feponiel.swiftpass.domain.application.usecases;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.feponiel.swiftpass.domain.application.repositories.RegistrationsRepository;
import com.feponiel.swiftpass.domain.application.services.StripeService;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.RegistrationNotFoundException;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.RegistrationNotPaidException;
import com.feponiel.swiftpass.domain.business.entities.Registration;
import com.feponiel.swiftpass.domain.business.valueobjects.PaymentStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefundRegistrationUseCase {
  private final RegistrationsRepository registrationsRepository;
  private final StripeService stripeService;

  public void execute(UUID registrationId) {
    Registration registration = this.registrationsRepository.findById(registrationId)
      .orElseThrow(RegistrationNotFoundException::new);

    if (registration.getPaymentStatus() != PaymentStatus.PAID)
      throw new RegistrationNotPaidException();

    this.stripeService.processPartialRefund(registration.getStripeSessionId(), registration.getTotalPaid());

    registration.updatePaymentStatus(PaymentStatus.REFUNDED);
    this.registrationsRepository.update(registration);
  }
}
