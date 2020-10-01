package org.jepria.server.mock;

import com.technology.jep.jepcommon.security.pkg_Operator;
import org.jepria.server.mock.MockCredential;
import org.jepria.server.service.security.HttpBasicDynamicFeature;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import java.sql.SQLException;

public class MockBasicAuthCredential extends MockCredential {

  public MockBasicAuthCredential(String... roles) {
    super(roles);
  }

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
