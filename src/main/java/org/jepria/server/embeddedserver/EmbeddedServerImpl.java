package org.jepria.server.embeddedserver;

public abstract class EmbeddedServerImpl implements EmbeddedServer {
  public static final String DEFAULT_BASE_URI = "http://localhost";

  public static final int DEFAULT_PORT = 9998;

  private final String baseUri;

  private final int port;

  protected EmbeddedServerImpl(String baseUri, int port) {
    this.baseUri = baseUri;
    this.port = port;
  }

  public String getBaseUri() {
    return baseUri;
  }

  public int getPort() {
    return port;
  }

  @Override
  public String getUrl() {
    return baseUri + ":" + port;
  }
}
