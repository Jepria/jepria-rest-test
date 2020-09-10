package org.jepria.server.service;

import org.glassfish.jersey.server.ResourceConfig;

public final class ServiceReference {

  public static final String DEFAULT_API_PATH = "/api";

  public static final String DEFAULT_CONTEXT_PATH = "/ApplicationContext";

  public static final String DEFAULT_SERVLET_NAME = "jersey-servlet-container";

  private final String apiPath;

  private final String contextPath;

  private final String servletName;

  private final ResourceConfig resourceConfig;

  public ServiceReference(final ResourceConfig resourceConfig) {
    this(DEFAULT_API_PATH, DEFAULT_CONTEXT_PATH, DEFAULT_SERVLET_NAME, resourceConfig);
  }

  public ServiceReference(
      final String apiPath, final String contextPath, final String servletName, final ResourceConfig resourceConfig
  ) {
    this.apiPath = apiPath;
    this.contextPath = contextPath;
    this.servletName = servletName;
    this.resourceConfig = resourceConfig;
  }

  public String getApiPath() {
    return apiPath;
  }

  public String getContextPath() {
    return contextPath;
  }

  public String getServletName() {
    return servletName;
  }

  public ResourceConfig getResourceConfig() {
    return resourceConfig;
  }
}
