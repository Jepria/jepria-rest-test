package org.jepria.server.mock;

public abstract class MockCredential {

  protected final String login;

  protected final Integer operatorId;

  public MockCredential() {
    this("mock-user", 1);
  }

  public MockCredential(String login, Integer operatorId) {
    this.login = login;
    this.operatorId = operatorId;
  }

  public String getLogin() {
    return login;
  }

  public Integer getOperatorId() {
    return operatorId;
  }

  public abstract void mock();
}
