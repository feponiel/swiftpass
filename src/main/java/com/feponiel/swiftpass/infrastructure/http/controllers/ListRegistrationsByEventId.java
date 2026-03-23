package com.feponiel.swiftpass.infrastructure.http.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.feponiel.swiftpass.domain.application.usecases.ListRegistrationsByEventIdUseCase;
import com.feponiel.swiftpass.domain.business.entities.Registration;
import com.feponiel.swiftpass.domain.business.valueobjects.PaymentStatus;
import com.feponiel.swiftpass.infrastructure.http.presenters.ListRegistrationsByEventIdPresenter;
import com.feponiel.swiftpass.infrastructure.http.presenters.dtos.RegistrationListHTTPResponseModel;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events/{eventId}/registrations")
@RequiredArgsConstructor
public class ListRegistrationsByEventId {
  private final ListRegistrationsByEventIdUseCase listRegistrationsByEventIdUseCase;

  @GetMapping
  public ResponseEntity<RegistrationListHTTPResponseModel> handle(
    @PathVariable UUID eventId,
    @RequestParam(required = false) PaymentStatus status
  ) {
    List<Registration> registrations = this.listRegistrationsByEventIdUseCase.execute(eventId, status);

    return ResponseEntity.ok().body(ListRegistrationsByEventIdPresenter.toHTTP(registrations));
  }
}
