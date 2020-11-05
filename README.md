# jepria-rest-test
Library for testing services made using jepria-rest

Compatibility: jepria-rest-12.0.0 - jepria-rest-12.2.1

# How to use:
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
  <version>1.0.0</version>
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
      new MockServerFactory<>(SimpleDao.class, SimpleServerFactory.class) 
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

# Pros and cons
```
+ full support for jepria-rest 
+ doesn't depend on external framework (web-server implementation can be easily replaced)
+ don't use DI/IoC containers
+ use "real" ResourceConfig from application 
+ full analog deploy on web-server
+ minified setting code in application tests 

- dependens on PowerMock
- downgrade junit4 to junit 5 because of PowerMock
```
