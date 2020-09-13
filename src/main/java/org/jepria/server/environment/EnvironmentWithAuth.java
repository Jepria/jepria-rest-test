package org.jepria.server.environment;

import org.jepria.server.mock.MockCredential;

public interface EnvironmentWithAuth {
  MockCredential getMockCredential();
}
