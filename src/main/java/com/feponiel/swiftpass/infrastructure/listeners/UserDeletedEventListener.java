package com.feponiel.swiftpass.infrastructure.listeners;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.feponiel.swiftpass.domain.business.events.UserDeletedEvent;

@Component
public class UserDeletedEventListener {
  @EventListener
  public void handle(UserDeletedEvent event) {
    UUID deletedUserId = event.getDeletedUserId();
    UUID deleterId = event.getDeleterId();

    if (deletedUserId.equals(deleterId)) {
      ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      
      new SecurityContextLogoutHandler().logout(
        requestAttributes.getRequest(),
        requestAttributes.getResponse(),
        auth
      );
    }
  }
}
