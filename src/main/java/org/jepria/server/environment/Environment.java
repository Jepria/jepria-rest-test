package org.jepria.server.environment;

import org.jepria.server.mock.MockCredential;

public interface Environment<T> {
  String getApiUrl();

  T getMockedDao();
  MockCredential getMockCredential();

  void setUp();
  void tearDown();

  void beforeTest();
  void afterTest();
}
