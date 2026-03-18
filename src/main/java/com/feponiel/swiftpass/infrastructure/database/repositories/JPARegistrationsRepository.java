package com.feponiel.swiftpass.infrastructure.database.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.feponiel.swiftpass.domain.application.repositories.RegistrationsRepository;
import com.feponiel.swiftpass.domain.business.entities.Registration;
import com.feponiel.swiftpass.domain.business.valueobjects.PaymentStatus;
import com.feponiel.swiftpass.infrastructure.database.entities.JPARegistration;
import com.feponiel.swiftpass.infrastructure.mappers.RegistrationMapper;

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
  public List<Registration> listAllByPaymentStatus(PaymentStatus paymentStatus) {
    return this.entityManager
      .createQuery("SELECT registration FROM JPARegistration registration WHERE registration.paymentStatus = :paymentStatus", JPARegistration.class)
      .setParameter("paymentStatus", paymentStatus)
      .getResultList()
      .stream()
      .map(registrationMapper::toDomain)
      .toList();
  }

  @Override
  public void update(Registration registration) {
    this.entityManager
      .createQuery("UPDATE JPARegistration registration SET registration.holderName = :holderName, registration.paymentStatus = :paymentStatus, registration.checkoutUrl = :checkoutUrl, registration.stripeSessionId = :stripeSessionId, registration.totalPaid = :totalPaid, registration.paidCurrency = :paidCurrency WHERE registration.id = :id")
      .setParameter("holderName", registration.getHolderName())
      .setParameter("paymentStatus", registration.getPaymentStatus())
      .setParameter("checkoutUrl", registration.getCheckoutUrl())
      .setParameter("stripeSessionId", registration.getStripeSessionId())
      .setParameter("totalPaid", registration.getTotalPaid())
      .setParameter("paidCurrency", registration.getPaidCurrency())
      .setParameter("id", registration.getId())
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
