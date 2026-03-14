package com.feponiel.swiftpass.infrastructure.http.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feponiel.swiftpass.domain.application.usecases.ListAllEventsUseCase;
import com.feponiel.swiftpass.domain.business.entities.Event;
import com.feponiel.swiftpass.infrastructure.http.presenters.ListAllEventsPresenter;
import com.feponiel.swiftpass.infrastructure.http.presenters.dtos.ListAllEventsHTTPResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class ListAllEventsController {
  private final ListAllEventsUseCase listAllEventsUseCase;

  @GetMapping
  public ResponseEntity<ListAllEventsHTTPResponse> handle() {
    List<Event> events = this.listAllEventsUseCase.execute();

    return ResponseEntity.ok(ListAllEventsPresenter.toHTTP(events));
  }
}
