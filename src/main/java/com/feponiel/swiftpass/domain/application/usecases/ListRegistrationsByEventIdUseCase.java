package com.feponiel.swiftpass.domain.application.usecases;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.feponiel.swiftpass.domain.application.repositories.RegistrationsRepository;
import com.feponiel.swiftpass.domain.business.entities.Registration;
import com.feponiel.swiftpass.domain.business.valueobjects.PaymentStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListRegistrationsByEventIdUseCase {
  private final RegistrationsRepository registrationsRepository;

  public List<Registration> execute(UUID eventId, PaymentStatus paymentStatus) {
    // The existence of the eventId is intentionally not verified here, because even if an event is deleted,
    // the application preserves its registrations for fiscal purposes, so if it becomes necessary to
    // access these registrations, the event organizer will still be able to do so.

    return this.registrationsRepository.listAllByEventIdAndPaymentStatus(eventId, paymentStatus);
  }
}
