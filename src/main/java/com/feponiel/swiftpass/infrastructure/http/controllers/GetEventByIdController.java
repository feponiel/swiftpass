package com.feponiel.swiftpass.infrastructure.http.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feponiel.swiftpass.domain.application.usecases.GetEventByIdUseCase;
import com.feponiel.swiftpass.domain.business.entities.Event;
import com.feponiel.swiftpass.infrastructure.http.presenters.GetEventByIdPresenter;
import com.feponiel.swiftpass.infrastructure.http.presenters.dtos.EventHTTPResponseModel;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events/{eventId}")
@RequiredArgsConstructor
public class GetEventByIdController {
  private final GetEventByIdUseCase getEventByIdUseCase;

  @GetMapping
  public ResponseEntity<EventHTTPResponseModel> handle(@PathVariable UUID eventId) {
    Event event = this.getEventByIdUseCase.execute(eventId);

    return ResponseEntity.ok(GetEventByIdPresenter.toHTTP(event));
  }
}
