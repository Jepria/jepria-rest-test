package org.jepria.server.environment;

/**
 * Plain environment which contains server and service.
 */
public interface EnvironmentPlain {
  String getApiUrl();

  default void setUp() {};
  default void tearDown() {};

  default void beforeTest() {};
  default void afterTest() {};
}
