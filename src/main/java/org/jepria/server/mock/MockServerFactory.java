package org.jepria.server.mock;

import org.jepria.server.ServerFactory;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class MockServerFactory<T> {

  private final Class<T> daoClass;

  private final Class<? extends ServerFactory<T>> factoryClass;

  public MockServerFactory(Class<T> daoClass, Class<? extends ServerFactory<T>> factoryClass) {
    this.daoClass = daoClass;
    this.factoryClass = factoryClass;
  }

  public T getDao() throws ReflectiveOperationException {
    // Создаем мок дао
    T mockDao = Mockito.mock(daoClass);

    // Создаем фабрику и мокаем только метод получения дао
    // Подставляем вместо него, ранее созданный мок дао
    ServerFactory<T> spyFactory = (ServerFactory<T>) Mockito.spy(
        factoryClass.getMethod("getInstance").invoke(null)
    );
    Mockito.when(spyFactory.getDao()).thenReturn(mockDao);

    // Мокаем статический метод, который будет вызываться во время вызова сервиса,
    // возвращаем ранее созданный мок фабрики
    PowerMockito.stub(
        PowerMockito.method(
            factoryClass, "getInstance"
        )
    ).toReturn(spyFactory);

    return mockDao;
  }
}
