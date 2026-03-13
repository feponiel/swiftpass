package com.feponiel.swiftpass.domain.application.services;

import com.feponiel.swiftpass.domain.application.boundaries.MutableUserData;

public interface SessionsService {
  void updateSessionUser(MutableUserData mutableUserData);
}
