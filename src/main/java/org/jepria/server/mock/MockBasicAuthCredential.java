package org.jepria.server.mock;

import org.jepria.server.service.security.HttpBasicDynamicFeature;
import org.jepria.server.service.security.pkg_Operator;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import java.sql.SQLException;

public class MockBasicAuthCredential extends MockCredential {

  public MockBasicAuthCredential(final String... roles) {
    super(roles);
  }

  public MockBasicAuthCredential(final String login, final Integer operatorId, final String... roles) {
    super(login, operatorId, roles);
  }

  /**
   * Attention: you need to add <b>pkg_Operator.class</b>
   * to @PrepareForTest annotation in your test class to mock.
   */
  @Override
  protected void logon() {
    PowerMockito.mockStatic(pkg_Operator.class);
    try {
      Mockito.when(
          pkg_Operator.logon(Mockito.anyObject(), Mockito.eq(super.getLogin()))
      ).thenReturn(
          super.getOperatorId()
      );
    } catch (SQLException e) {
      // it will never happen, because we mock function
      e.printStackTrace();
    }
  }

  /**
   * Don't forget to add HttpBasicDynamicFeature.HttpBasicContainerRequestFilter.class
   * to @PrepareForTest - annotation in your Test-class
   */
  @Override
  protected void grant() {
    try {
      Class jerseySecurityContextClass = Whitebox.getInnerClassType(
          HttpBasicDynamicFeature.HttpBasicContainerRequestFilter.class, "JerseySecurityContext"
      );
      PowerMockito.replace(
          PowerMockito.method(jerseySecurityContextClass, "isUserInRole", String.class)
      ).with((proxy, method, args) -> {
        if (super.getRoles().contains(args[0])) {
          return true;
        }
        return false;
      });
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
