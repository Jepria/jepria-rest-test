# jepria-rest-test
Library for testing services made using jepria-rest

Compatibility: jepria-rest-12.0.0 - jepria-rest-12.2.1

See jepria-showcase project for more detailed examples.

# How to use with DI and JerseyTest (versions jepria-rest-test-2.*)
1. Add dependencies to maven (note: use dependencyManagement for dependecies control)
```
<!-- for test: -->
<dependency>
  <groupId>org.junit.jupiter</groupId>
  <artifactId>junit-jupiter</artifactId>
  <version>5.6.0</version>
  <scope>test</scope>
</dependency>

<dependency>
  <groupId>org.jepria.test</groupId>
  <artifactId>jepria-rest-test</artifactId>
  <version>2.0.0</version>
  <scope>test</scope>
</dependency>

<dependency>
  <groupId>org.mockito</groupId>
  <artifactId>mockito-core</artifactId>
  <version>${mockito.version}</version>
  <scope>test</scope>
</dependency>

<dependency>
  <groupId>org.mockito</groupId>
  <artifactId>mockito-inline</artifactId>
  <version>${mockito.version}</version>
  <scope>test</scope>
</dependency>

<dependency>
  <groupId>org.mockito</groupId>
  <artifactId>mockito-junit-jupiter</artifactId>
  <version>${mockito.version}</version>
  <scope>test</scope>
</dependency>

<!-- grizzly embedded server -->
<dependency>
  <groupId>org.glassfish.jersey.test-framework.providers</groupId>
  <artifactId>jersey-test-framework-provider-grizzly2</artifactId>
  <version>${jersey.version}</version>
  <scope>test</scope>
</dependency>
```
2. Write tests. Simple example:
```
/**
 * Test class for SimpleJaxrsAdapter.
 */
class SimpleServiceTest extends JepriaTest {

  /**
   * Declare mock for dao.
   */
  @Mock
  private SimpleDao mockedDao;

  @Override
  protected Application configure() {
    MockitoAnnotations.openMocks(this); // initialize mocks

    // enable tracing if you need it
    enable(TestProperties.DUMP_ENTITY);
    enable(TestProperties.LOG_TRAFFIC);

    return new TestApplicationConfig(JepriaTest.credential) {
      {
        register(SimpleJaxrsAdapter.class); // register testing adapter

        register(new AbstractBinder() {
          @Override
          protected void configure() {
            bind(mockedDao).to(SimpleDao.class); // register mock for dao
            bindAsContract(SimpleService.class); // register service for our adapter
            
            // bind other classes
          }
        });
      }
    };
  }
  
  @Test
  void method_ok() {
    // write test code using Arrange-Act-Assert pattern
    // Arrange
    Mockito.when(this.mockedDao.method("input-data")).thenReturn("expected-data");
    
    // Act
    Response response = ...
    
    // Assert
    Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    Assert.assertEquals("expected-response", response.readEntity(String.class));
  }
}
```

# How to use with a static factory (versions jepria-rest-test-1.*)
1. Add dependencies to maven:
```
<!-- for test: -->
<dependency>
  <groupId>org.powermock</groupId>
  <artifactId>powermock-api-mockito</artifactId>
  <version>1.6.5</version>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>org.powermock</groupId>
  <artifactId>powermock-module-junit4</artifactId>
  <version>1.6.5</version>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>org.jepria.test</groupId>
  <artifactId>jepria-rest-test</artifactId>
  <version>1.0.1</version>
  <scope>test</scope>
</dependency>
<!-- grizzly embedded server -->
<dependency>
  <groupId>org.glassfish.jersey.test-framework.providers</groupId>
  <artifactId>jersey-test-framework-provider-grizzly2</artifactId>
  <version>${jersey.version}</version>
  <scope>test</scope>
</dependency>
```
2. Add junit4 dependency (downgrade junit 5 if you had already added it) 
```
<!--    <dependency>-->
<!--      <groupId>org.junit.jupiter</groupId>-->
<!--      <artifactId>junit-jupiter</artifactId>-->
<!--      <version>5.6.0</version>-->
<!--    </dependency>-->
    <!-- downgrade because of powermock -->
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.13</version>
      <scope>test</scope>
    </dependency>
 ```

3. Write tests. Simple example:
```
// include PowerMock
@RunWith(PowerMockRunner.class) 
// pkg_Operator.class for mock Auth, SimpleServerFactory.class for mock DAO
@PrepareForTest({pkg_Operator.class, SimpleServerFactory.class}) 
public class SimpleServiceTest { // Test class for SimpleJaxrsAdapter

  // Creation test environment
  private static final Environment<SimpleDao> env = new EnvironmentImpl.Builder<>(
      new ServiceReference(
          new RestConfig()  // Your application ResourceConfig
      ),
      // Your JaxrsAdapter factory and DAO classes. Don't forget to add factory to test class annotation
      new MockServerFactoryImpl<>(SimpleServerFactory.class) 
  ).build();

  // Below methods that preparing test environment
  @BeforeClass
  public static void setUp() {
    env.setUp();
  }

  @AfterClass
  public static void tearDown() {
    env.tearDown();
  }

  @Before
  public void beforeTest() {
    env.beforeTest();
  }

  @After
  public void afterTest() {
    env.afterTest();
  }

  @Test
  public void method_ok() {
    // write test code using Arrange-Act-Assert pattern
    // Arrange
    Mockito.when(env.getMockedDao().method("input-data")).thenReturn("expected-data");
    
    // Act
    Response response = ...
    
    // Assert
    Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    Assert.assertEquals("expected-response", response.readEntity(String.class));
  }
}
```
