package org.jepria.server.embeddedserver;

import org.jepria.server.service.ServiceReference;

public interface EmbeddedServer {
  void start();

  void shutdown();

  void deploy(ServiceReference serviceReference);

  /**
   * Возвращает ссылку на сервер (протокол, домен, порт)
   * @return Ссылка на сервер
   */
  String getUrl();
}
