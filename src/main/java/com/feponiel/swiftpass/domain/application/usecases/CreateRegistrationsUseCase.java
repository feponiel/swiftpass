package com.feponiel.swiftpass.domain.application.usecases;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.feponiel.swiftpass.domain.application.boundaries.CheckoutItemData;
import com.feponiel.swiftpass.domain.application.boundaries.CheckoutSessionData;
import com.feponiel.swiftpass.domain.application.boundaries.RegistrationItemData;
import com.feponiel.swiftpass.domain.application.repositories.EventsRepository;
import com.feponiel.swiftpass.domain.application.repositories.RegistrationsRepository;
import com.feponiel.swiftpass.domain.application.repositories.TicketsRepository;
import com.feponiel.swiftpass.domain.application.services.StripeService;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.EventHasAlreadyEndedException;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.EventNotFoundException;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.EventSalesClosedException;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.TicketNotFoundException;
import com.feponiel.swiftpass.domain.business.entities.Event;
import com.feponiel.swiftpass.domain.business.entities.Registration;
import com.feponiel.swiftpass.domain.business.entities.Ticket;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateRegistrationsUseCase {
  private final RegistrationsRepository registrationsRepository;
  private final TicketsRepository ticketsRepository;
  private final EventsRepository eventsRepository;
  private final StripeService stripeService;

  public List<Registration> execute(UUID registrantId, List<RegistrationItemData> registrationItems) {
    List<Registration> registrationList = createRegistrations(registrantId, registrationItems);
    List<CheckoutItemData> checkoutItems = buildCheckoutItems(registrationList);
    CheckoutSessionData checkoutSessionData = stripeService.createCheckoutSession(checkoutItems);

    return attachCheckoutSession(registrationList, checkoutSessionData);
  }

  private List<Registration> createRegistrations(UUID registrantId, List<RegistrationItemData> registrationItems) {
    return registrationItems.stream()
        .map(item -> {
          Ticket ticket = this.ticketsRepository.findById(item.ticketId())
            .orElseThrow(TicketNotFoundException::new);

          Event event = this.eventsRepository.findById(ticket.getEventId())
            .orElseThrow(EventNotFoundException::new);

          if (Instant.now().isAfter(event.getEndDate()))
            throw new EventHasAlreadyEndedException();

          if (!event.getSalesOpen())
            throw new EventSalesClosedException();

          Registration registration = Registration.builder()
            .id(UUID.randomUUID())
            .registrantId(registrantId)
            .ticketId(item.ticketId())
            .eventId(ticket.getEventId())
            .holderName(item.holderName())
            .build();

          this.registrationsRepository.create(registration);

          return registration;
        })
        .toList();
  }

  private List<CheckoutItemData> buildCheckoutItems(List<Registration> registrationList) {
    return registrationList.stream()
        .collect(Collectors.groupingBy(Registration::getTicketId, Collectors.counting()))
        .entrySet().stream()
        .map(entry -> {
          Ticket ticket = this.ticketsRepository.findById(entry.getKey())
            .orElseThrow(TicketNotFoundException::new);

          return new CheckoutItemData(ticket.getName(), ticket.getDescription(), ticket.getPrice(), ticket.getCurrency(), entry.getValue());
        })
        .toList();
  }

  private List<Registration> attachCheckoutSession(List<Registration> registrationList, CheckoutSessionData checkoutData) {
    return registrationList.stream()
      .map(registration -> {
        registration.updateStripeSessionID(checkoutData.sessionId()).updateCheckoutURL(checkoutData.checkoutUrl());

        this.registrationsRepository.update(registration);

        return registration;
      })
      .toList();
  }
}
