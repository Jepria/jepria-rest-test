package org.jepria.server.environment;

public interface EnvironmentWithDao<T> {
  T getMockedDao();
}
