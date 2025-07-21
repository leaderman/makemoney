package io.github.leaderman.makemoney.hustle.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ConfigClientTest {
  @Autowired
  private ConfigClient configClient;

  @Test
  public void testGet() {
    Object value = configClient.get("hustle.test.string");
    System.out.println(value);
  }

  @Test
  public void testGetValues() {
    String strVal = configClient.getString("hustle.test.string");
    int intVal = configClient.getInt("hustle.test.int");
    long longVal = configClient.getLong("other.test.long");
    float floatVal = configClient.getFloat("hustle.test.float");
    double doubleVal = configClient.getDouble("other.test.double");
    boolean boolVal = configClient.getBoolean("hustle.test.boolean");

    System.out.println(strVal);
    System.out.println(intVal);
    System.out.println(longVal);
    System.out.println(floatVal);
    System.out.println(doubleVal);
    System.out.println(boolVal);
  }

  @Test
  public void testGetDefaultValues() {
    String strVal = configClient.getString("hustle.test.strin2g", "default");
    int intVal = configClient.getInt("hustle.test.int2", 1);
    long longVal = configClient.getLong("other.test.long2", 2L);
    float floatVal = configClient.getFloat("hustle.test.float2", 3.0f);
    double doubleVal = configClient.getDouble("other.test.double2", 4.0);
    boolean boolVal = configClient.getBoolean("hustle.test.boolean2", true);

    System.out.println(strVal);
    System.out.println(intVal);
    System.out.println(longVal);
    System.out.println(floatVal);
    System.out.println(doubleVal);
    System.out.println(boolVal);
  }

  @Test
  public void testLoad() throws InterruptedException {
    Thread.sleep(5 * 60 * 1000);
  }
}
