package org.jepria.test.server.mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Представление мока пользователя системы.
 */
public abstract class MockCredential {

  private final String login;

  private final Integer operatorId;

  private final List<String> roles;

  public MockCredential(final String... roles) {
    this("mock-user", 1, roles);
  }

  public MockCredential(final String login, final Integer operatorId, final String... roles) {
    this.login = login;
    this.operatorId = operatorId;
    this.roles = Collections.unmodifiableList(Arrays.asList(roles));
  }

  public final String getLogin() {
    return login;
  }

  public final Integer getOperatorId() {
    return operatorId;
  }

  public final List<String> getRoles() {
    return roles;
  }

  public final void mock() {
    logon();
    grant();
  }

  protected abstract void logon();

  /**
   * Выдает пользователю права для работы в системе.
   */
  protected abstract void grant();
}
