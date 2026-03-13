package com.feponiel.swiftpass.infrastructure.listeners;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.feponiel.swiftpass.domain.application.boundaries.MutableUserData;
import com.feponiel.swiftpass.domain.application.services.SessionsService;
import com.feponiel.swiftpass.domain.business.entities.User;
import com.feponiel.swiftpass.domain.business.events.UserEditedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserEditedEventListener {
  private final SessionsService sessionsService;

  @EventListener
  public void handle(UserEditedEvent event) {
    User editedUser = event.getEditedUser();
    UUID editedUserId = editedUser.getId();
    UUID editorId = event.getEditorId();

    if (editedUserId.equals(editorId)) {
      MutableUserData updatedData = new MutableUserData(
        editedUser.getName(),
        editedUser.getPictureUrl(),
        editedUser.getRole(),
        editedUser.getUpdatedAt(),
        editedUser.getEditedAt()
      );

      this.sessionsService.updateSessionUser(updatedData);
    }
  }
}
