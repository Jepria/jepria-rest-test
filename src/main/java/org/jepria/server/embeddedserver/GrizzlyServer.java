package org.jepria.server.embeddedserver;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jepria.server.service.ServiceReference;

import java.io.IOException;
import java.net.URI;

public final class GrizzlyServer extends EmbeddedServerImpl {

  private HttpServer grizzly;

  public GrizzlyServer() {
    this(DEFAULT_BASE_URI, DEFAULT_PORT);
  }

  public GrizzlyServer(final String baseUri, final int port) {
    super(baseUri, port);
  }

  @Override
  public void start() {
    if (grizzly != null) {
      throw new IllegalStateException();
    }
    grizzly = GrizzlyHttpServerFactory.createHttpServer(
        URI.create(super.getBaseUri() + ":" + super.getPort())
    );

    try {
      grizzly.start();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deploy(final ServiceReference serviceReference) {
    WebappContext context = new WebappContext("WebappContext", serviceReference.getContextPath());

    ServletRegistration registration = context.addServlet(
        serviceReference.getServletName(), new ServletContainer(serviceReference.getResourceConfig())
    );
    registration.setLoadOnStartup(1);
    registration.addMapping(serviceReference.getApiPath() + "/*");

    context.deploy(grizzly);
  }

  @Override
  public void shutdown() {
    grizzly.shutdown();
  }
}
