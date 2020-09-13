package org.jepria.server.environment;

import org.jepria.server.embeddedserver.EmbeddedServer;
import org.jepria.server.embeddedserver.GrizzlyServer;
import org.jepria.server.mock.MockCredential;
import org.jepria.server.mock.MockServerFactory;
import org.jepria.server.service.ServiceReference;

public final class EnvironmentImpl<T> implements Environment<T> {
  private final EmbeddedServer embeddedServer;
  private final ServiceReference serviceReference;
  private final MockServerFactory<T> mockServerFactory;
  private final MockCredential mockCredential;

  /**
   * TODO(mockedDao): Mockito doesn't work without this field.
   */
  private T mockedDao;

  private EnvironmentImpl(final Builder<T> builder) {
    this.embeddedServer = builder.embeddedServer;
    this.serviceReference = builder.serviceReference;
    this.mockServerFactory = builder.mockServerFactory;
    this.mockCredential = builder.mockCredential;
  }

  @Override
  public String getApiUrl() {
    return new StringBuilder()
        .append(this.embeddedServer.getUrl())
        .append(this.serviceReference.getContextPath())
        .append(this.serviceReference.getApiPath())
        .toString();
  }

  /**
   * TODO: мок создается каждый раз, в этом есть проблема?
   * @return DAO mock.
   */
  @Override
  public T getMockedDao() {
    return mockedDao;
  }

  @Override
  public MockCredential getMockCredential() {
    return this.mockCredential;
  }

  @Override
  public void setUp() {
    this.embeddedServer.start();
    this.embeddedServer.deploy(this.serviceReference);
  }

  @Override
  public void tearDown() {
    this.embeddedServer.shutdown();
  }

  /**
   * Объекты необходимо мокировать перед каждым тестом: <br/>
   * - мокаем DAO. <br/>
   * - мокаем авторизацию пользователя в сервисах.
   */
  @Override
  public void beforeTest() {
    this.mockedDao = this.mockServerFactory.getDao(); // TODO(mocked dao)
    this.mockCredential.mock();
  }

  public static class Builder<T> {
    private EmbeddedServer embeddedServer = new GrizzlyServer();
    private final ServiceReference serviceReference;
    private final MockServerFactory<T> mockServerFactory;
    private MockCredential mockCredential = new MockCredential() {
      @Override
      protected void logon() {}

      @Override
      protected void grant() {}
    };

    public Builder(
        final ServiceReference serviceReference,
        final MockServerFactory<T> mockServerFactory
    ) {
      this.mockServerFactory = mockServerFactory;
      this.serviceReference = serviceReference;
    }

    public Builder<T> embeddedServer(final EmbeddedServer embeddedServer) {
      this.embeddedServer = embeddedServer;
      return this;
    }

    public Builder<T> mockCredential(final MockCredential mockCredential) {
      this.mockCredential = mockCredential;
      return this;
    }

    public EnvironmentImpl<T> build() {
      return new EnvironmentImpl<>(this);
    }
  }
}
