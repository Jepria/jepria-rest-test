package org.jepria.test.server.mock;

import org.jepria.server.ServerFactory;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

/**
 * Фабрика, которая создает мок dao. Идентична оригинальной {@link ServerFactory}
 * @param <T> DAO type.
 */
public final class MockServerFactory<T> {

  private final Class<T> daoClass;

  private final Class<? extends ServerFactory<T>> factoryClass;

  public MockServerFactory(final Class<T> daoClass, final Class<? extends ServerFactory<T>> factoryClass) {
    this.daoClass = daoClass;
    this.factoryClass = factoryClass;
  }

  public T getDao() {
    // Создаем мок дао
    final T mockDao = Mockito.mock(this.daoClass);

    // Создаем фабрику и мокаем только метод получения дао
    // Подставляем вместо него, ранее созданный мок дао
    final ServerFactory<T> spyFactory;
    try {
      spyFactory = (ServerFactory<T>) Mockito.spy(
          this.factoryClass.getMethod("getInstance").invoke(null)
      );
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException("Source factory are not capable with this mock.", e);
    }
    Mockito.when(spyFactory.getDao()).thenReturn(mockDao);

    // Мокаем статический метод, который будет вызываться во время вызова сервиса,
    // возвращаем ранее созданный мок фабрики
    PowerMockito.stub(
        PowerMockito.method(
            this.factoryClass, "getInstance"
        )
    ).toReturn(spyFactory);

    return mockDao;
  }
}
