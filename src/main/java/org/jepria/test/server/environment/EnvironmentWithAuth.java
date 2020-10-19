package org.jepria.test.server.environment;

import org.jepria.test.server.mock.MockCredential;

public interface EnvironmentWithAuth {
  MockCredential getMockCredential();
}
