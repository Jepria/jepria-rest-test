package org.jepria.test.server.environment;

public interface EnvironmentWithDao<T> {
  T getMockedDao();
}
