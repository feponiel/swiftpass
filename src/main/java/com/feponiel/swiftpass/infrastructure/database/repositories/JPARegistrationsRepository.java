package com.feponiel.swiftpass.infrastructure.database.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.feponiel.swiftpass.domain.application.repositories.RegistrationsRepository;
import com.feponiel.swiftpass.domain.business.entities.Registration;
import com.feponiel.swiftpass.domain.business.valueobjects.PaymentStatus;
import com.feponiel.swiftpass.infrastructure.database.entities.JPARegistration;
import com.feponiel.swiftpass.infrastructure.database.mappers.RegistrationMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class JPARegistrationsRepository implements RegistrationsRepository {
  @PersistenceContext private EntityManager entityManager;
  private final RegistrationMapper registrationMapper;

  @Override
  public void create(Registration registration) {
    JPARegistration parsedRegistration = registrationMapper.toJPA(registration);

    this.entityManager.persist(parsedRegistration);
  }

  @Override
  public Optional<Registration> findById(UUID id) {
    return this.entityManager
      .createQuery("SELECT registration FROM JPARegistration registration WHERE registration.id = :id", JPARegistration.class)
      .setParameter("id", id)
      .getResultStream()
      .findFirst()
      .map(registrationMapper::toDomain);
  }

  @Override
  public List<Registration> listAllByRegistrantId(UUID registrantId) {
    return this.entityManager
      .createQuery("SELECT registration FROM JPARegistration registration WHERE registration.registrantId = :registrantId", JPARegistration.class)
      .setParameter("registrantId", registrantId)
      .getResultList()
      .stream()
      .map(registrationMapper::toDomain)
      .toList();
  }

  @Override
  public List<Registration> listAllByStripeSessionId(String stripeSessionId) {
    return entityManager
      .createQuery("SELECT registration FROM JPARegistration registration WHERE registration.stripeSessionId = :stripeSessionId", JPARegistration.class)
      .setParameter("stripeSessionId", stripeSessionId)
      .getResultList()
      .stream()
      .map(registrationMapper::toDomain)
      .toList();
  }

  @Override
  public List<Registration> listAllByEventIdAndPaymentStatus(UUID eventId, PaymentStatus paymentStatus) {
    return this.entityManager
      .createQuery("SELECT registration FROM JPARegistration registration WHERE registration.eventId = :eventId AND (:paymentStatus IS NULL OR registration.paymentStatus = :paymentStatus)", JPARegistration.class)
      .setParameter("eventId", eventId)
      .setParameter("paymentStatus", paymentStatus)
      .getResultList()
      .stream()
      .map(registrationMapper::toDomain)
      .toList();
  }

  @Override
  public Integer countConfirmedByTicketId(UUID ticketId) {
    return ((Long) this.entityManager
      .createQuery("SELECT COUNT(registration) FROM JPARegistration registration WHERE registration.ticketId = :ticketId AND registration.paymentStatus = :paymentStatus")
      .setParameter("ticketId", ticketId)
      .setParameter("paymentStatus", PaymentStatus.PAID)
      .getSingleResult())
      .intValue();
  }

  @Override
  public void update(Registration registration) {
    this.entityManager
      .createQuery("UPDATE JPARegistration registration SET registration.holderName = :holderName, registration.paymentStatus = :paymentStatus, registration.checkoutUrl = :checkoutUrl, registration.stripeSessionId = :stripeSessionId, registration.totalPaid = :totalPaid, registration.paidCurrency = :paidCurrency, registration.updatedAt = :updatedAt, registration.editedAt = :editedAt WHERE registration.id = :id")
      .setParameter("holderName", registration.getHolderName())
      .setParameter("paymentStatus", registration.getPaymentStatus())
      .setParameter("checkoutUrl", registration.getCheckoutUrl())
      .setParameter("stripeSessionId", registration.getStripeSessionId())
      .setParameter("totalPaid", registration.getTotalPaid())
      .setParameter("paidCurrency", registration.getPaidCurrency())
      .setParameter("updatedAt", registration.getUpdatedAt())
      .setParameter("editedAt", registration.getEditedAt())
      .setParameter("id", registration.getId())
      .executeUpdate();
  }

  @Override
  public void updateFromPaidToRefundedByEventId(UUID eventId) {
    this.entityManager
      .createQuery("UPDATE JPARegistration registration SET registration.paymentStatus = :refunded WHERE registration.paymentStatus = :paid AND registration.eventId = :eventId")
      .setParameter("refunded", PaymentStatus.REFUNDED)
      .setParameter("paid", PaymentStatus.PAID)
      .setParameter("eventId", eventId)
      .executeUpdate();
  }

  @Override
  public void deleteById(UUID id) {
    this.entityManager
      .createQuery("DELETE JPARegistration registration WHERE registration.id = :id")
      .setParameter("id", id)
      .executeUpdate();
  }
}
