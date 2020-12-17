package org.jepria.test.server.service.rest.jersey;

import org.glassfish.jersey.test.JerseyTest;
import org.jepria.server.service.security.PrincipalImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


/**
 * Базовый класс для запуска тестов
 */
public class JepriaTest extends JerseyTest {

  /**
   * Учетные данные пользователя, после передачи в конструктор {@link TestApplicationConfig}
   * можно использовать в тестовых методах для проверок.
   */
  protected static final PrincipalImpl credential = new PrincipalImpl("user", 123);

  /**
   * Совместимость с junit5
   * @throws Exception
   */
  @BeforeEach
  void before() throws Exception {
    super.setUp();
  }

  /**
   * Совместимость с junit5
   * @throws Exception
   */
  @AfterEach
  void after() throws Exception {
    super.tearDown();
  }
}
