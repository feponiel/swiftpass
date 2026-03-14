package com.feponiel.swiftpass.infrastructure.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.feponiel.swiftpass.domain.application.boundaries.MutableUserData;
import com.feponiel.swiftpass.domain.application.services.SessionsService;
import com.feponiel.swiftpass.domain.business.entities.User;
import com.feponiel.swiftpass.domain.business.events.ProfileEditedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProfileEditedEventListener {
  private final SessionsService sessionsService;

  @EventListener
  public void handle(ProfileEditedEvent event) {
    User user = event.getUser();

    MutableUserData updatedData = new MutableUserData(
      user.getName(),
      user.getPictureUrl(),
      user.getRole(),
      user.getUpdatedAt(),
      user.getEditedAt()
    );


    this.sessionsService.updateSessionUser(updatedData);
  }
}
