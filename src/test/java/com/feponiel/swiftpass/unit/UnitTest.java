package com.feponiel.swiftpass.unit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.mockito.MockitoAnnotations;

@Tag("unit")
public abstract class UnitTest {
  private AutoCloseable mocks;

  @BeforeEach
  void setup() {
    mocks = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }
}
