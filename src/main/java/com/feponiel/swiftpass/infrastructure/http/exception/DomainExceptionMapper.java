package com.feponiel.swiftpass.infrastructure.http.exception;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.feponiel.swiftpass.domain.application.usecases.exceptions.DomainException;
import com.feponiel.swiftpass.domain.application.usecases.exceptions.DomainExceptionCode;

@Component
public class DomainExceptionMapper {
  public record ExceptionMeta(int status, String typeURI) {}

  private static final Map<DomainExceptionCode, ExceptionMeta> META = Map.ofEntries(
    Map.entry(DomainExceptionCode.EVENT_ALREADY_ENDED, new ExceptionMeta(409, "/errors/event-already-ended")),
    Map.entry(DomainExceptionCode.EVENT_NOT_FOUND, new ExceptionMeta(404, "/errors/event-not-found")),
    Map.entry(DomainExceptionCode.EVENT_SALES_CLOSED, new ExceptionMeta(409, "/errors/event-sales-closed")),
    Map.entry(DomainExceptionCode.INVALID_CURRENCY, new ExceptionMeta(422, "/errors/invalid-currency")),
    Map.entry(DomainExceptionCode.INVALID_STRIPE_WEBHOOK, new ExceptionMeta(400, "errors/invalid-stripe-webhook")),
    Map.entry(DomainExceptionCode.NO_FIELD_PROVIDED, new ExceptionMeta(400, "errors/no-field-provided")),
    Map.entry(DomainExceptionCode.REGISTRATION_NOT_FOUND, new ExceptionMeta(404, "errors/registration-not-found")),
    Map.entry(DomainExceptionCode.REGISTRATION_NOT_PAID, new ExceptionMeta(409, "errors/registration-not-paid")),
    Map.entry(DomainExceptionCode.STRIPE_EVENT_DESERIALIZATION_FAILED, new ExceptionMeta(400, "errors/stripe-event-deserialization-failed")),
    Map.entry(DomainExceptionCode.TICKET_NOT_FOUND, new ExceptionMeta(404, "errors/ticket-not-found")),
    Map.entry(DomainExceptionCode.USER_NOT_FOUND, new ExceptionMeta(404, "errors/user-not-found"))
  );

  public ExceptionMeta resolve(DomainException exception) {
    return META.getOrDefault(exception.getCode(), new ExceptionMeta(500, "/errors/internal"));
  }
}
