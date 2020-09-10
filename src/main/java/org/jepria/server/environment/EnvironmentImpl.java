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

  private T mockedDao;

  private EnvironmentImpl(final Builder<T> builder) {
    embeddedServer = builder.embeddedServer;
    serviceReference = builder.serviceReference;
    mockServerFactory = builder.mockServerFactory;
    mockCredential = builder.mockCredential;
  }

  @Override
  public String getApiUrl() {
    return new StringBuilder()
        .append(embeddedServer.getUrl())
        .append(serviceReference.getContextPath())
        .append(serviceReference.getApiPath())
        .toString();
  }

  @Override
  public T getMockedDao() {
    return mockedDao;
  }

  @Override
  public MockCredential getMockCredential() {
    return mockCredential;
  }

  @Override
  public void setUp() {
    embeddedServer.start();
    embeddedServer.deploy(serviceReference);
  }

  @Override
  public void tearDown() {
    embeddedServer.shutdown();
  }

  /**
   * Моки необходимо инициализировать перед каждым тестом
   */
  @Override
  public void beforeTest() {
    try {
      mockedDao = mockServerFactory.getDao();
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
    if (mockCredential != null) { // TODO: remove
      mockCredential.mock();
    }
  }

  @Override
  public void afterTest() {}

  public static class Builder<T> {
    private EmbeddedServer embeddedServer = new GrizzlyServer();
    private final ServiceReference serviceReference;
    private final MockServerFactory<T> mockServerFactory;
    private MockCredential mockCredential; // TODO: init with default auth

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
