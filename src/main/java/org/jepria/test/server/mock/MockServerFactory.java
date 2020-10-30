package org.jepria.test.server.mock;

public interface MockServerFactory<T> {
  T getDao();
}
