package com.feponiel.swiftpass.domain.application.usecases;

import java.util.List;

import org.springframework.stereotype.Service;

import com.feponiel.swiftpass.domain.application.providers.StripeConfigProvider;
import com.feponiel.swiftpass.domain.application.repositories.RegistrationsRepository;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.InvalidStripeWebhookSignatureException;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.StripeEventDeserializationFailedException;
import com.feponiel.swiftpass.domain.business.entities.Registration;
import com.feponiel.swiftpass.domain.business.valueobjects.PaymentStatus;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProcessStripeCheckoutEventUseCase {
  private final RegistrationsRepository registrationsRepository;
  private final StripeConfigProvider stripeConfigProvider;

  public void execute(String eventName, String stripeSignature) {
    try {
      Event stripeEvent = Webhook.constructEvent(eventName, stripeSignature, stripeConfigProvider.getWebhookSecret());

      switch (stripeEvent.getType()) {
        case "checkout.session.completed" -> handleCheckoutCompleted(stripeEvent);
        case "checkout.session.expired" -> handleCheckoutExpired(stripeEvent);
      }
    } catch (SignatureVerificationException _) {
      throw new InvalidStripeWebhookSignatureException();
    }
  }

  private void handleCheckoutCompleted(Event stripeEvent) {
    Session checkoutSession = (Session) stripeEvent
      .getDataObjectDeserializer()
      .getObject()
      .orElseThrow(StripeEventDeserializationFailedException::new);

    List<Registration> registrations = this.registrationsRepository.listAllByStripeSessionId(checkoutSession.getId());

    registrations.forEach(registration -> {
      registration.updatePaymentStatus(PaymentStatus.PAID);

      this.registrationsRepository.update(registration);
    });
  }

  private void handleCheckoutExpired(Event stripeEvent) {
    Session checkoutSession = (Session) stripeEvent
      .getDataObjectDeserializer()
      .getObject()
      .orElseThrow(StripeEventDeserializationFailedException::new);

    List<Registration> registrations = this.registrationsRepository.listAllByStripeSessionId(checkoutSession.getId());

    registrations.forEach(registration -> {
      registration.updatePaymentStatus(PaymentStatus.EXPIRED);

      this.registrationsRepository.update(registration);
    });
  }
}
