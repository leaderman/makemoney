package io.github.leaderman.makemoney.hustle.config;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lark.oapi.service.bitable.v1.model.AppTableRecord;

import io.github.leaderman.makemoney.hustle.feishu.BitableClient;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * 配置客户端。
 */
@Component
@Slf4j
public class ConfigClient {
  private final ConfigurableEnvironment environment;
  private final BitableClient bitableClient;
  private final List<String> bitables;

  public ConfigClient(ConfigurableEnvironment environment, BitableClient bitableClient,
      @Value("${config.bitables}") List<String> bitables) {
    this.environment = environment;
    this.bitableClient = bitableClient;
    this.bitables = bitables;
  }

  /**
   * 定时加载配置。
   */
  @PostConstruct
  @Scheduled(cron = "${config.cron}")
  public void refresh() {
    try {
      Map<String, Object> fields = new HashMap<>();

      for (String bitable : this.bitables) {
        List<AppTableRecord> records = bitableClient.listRecords(bitable);
        for (AppTableRecord record : records) {
          fields.put(record.getFields().get("名称").toString(), record.getFields().get("值"));
        }
      }

      BitablePropertySource source = (BitablePropertySource) this.environment.getPropertySources()
          .get(BitablePropertySource.NAME);
      source.refresh(fields);

      log.info("多维表格配置刷新 {} 条记录", fields.size());
    } catch (Exception e) {
      log.error("多维表格配置刷新错误：{}", ExceptionUtils.getStackTrace(e));
    }
  }

  /**
   * 判断配置项是否存在。
   * 
   * @param name 配置项名称
   * @return 配置项是否存在
   */
  public boolean contains(String name) {
    return this.environment.containsProperty(name);
  }

  /**
   * 获取配置项的值。
   * 
   * @param name 配置项名称
   * @return 配置项的值
   */
  public Object get(String name) {
    if (!this.contains(name)) {
      throw new IllegalArgumentException("配置项 " + name + " 不存在");
    }

    return this.environment.getProperty(name);
  }

  /**
   * 获取配置项的值。
   * 
   * @param name 配置项名称
   * @return 配置项的值
   */
  public String getString(String name) {
    return (String) this.get(name);
  }

  /**
   * 获取配置项的值。
   * 
   * @param name 配置项名称
   * @return 配置项的值
   */
  public String[] getStringArray(String name) {
    return this.getString(name).lines().map(String::trim).toArray(String[]::new);
  }

  /**
   * 获取配置项的值，如果配置项不存在，则返回默认值。
   * 
   * @param name         配置项名称
   * @param defaultValue 默认值
   * @return 配置项的值
   */
  public String getString(String name, String defaultValue) {
    return this.contains(name) ? this.getString(name) : defaultValue;
  }

  /**
   * 获取配置项的值，如果配置项不存在，则返回默认值。
   * 
   * @param name         配置项名称
   * @param defaultValue 默认值
   * @return 配置项的值
   */
  public String[] getStringArray(String name, String[] defaultValue) {
    return this.contains(name) ? this.getStringArray(name) : defaultValue;
  }

  /**
   * 获取配置项的值。
   * 
   * @param name 配置项名称
   * @return 配置项的值
   */
  public int getInt(String name) {
    return Integer.parseInt(this.getString(name));
  }

  /**
   * 获取配置项的值，如果配置项不存在，则返回默认值。
   * 
   * @param name         配置项名称
   * @param defaultValue 默认值
   * @return 配置项的值
   */
  public int getInt(String name, int defaultValue) {
    return this.contains(name) ? this.getInt(name) : defaultValue;
  }

  /**
   * 获取配置项的值。
   * 
   * @param name 配置项名称
   * @return 配置项的值
   */
  public long getLong(String name) {
    return Long.parseLong(this.getString(name));
  }

  /**
   * 获取配置项的值，如果配置项不存在，则返回默认值。
   * 
   * @param name         配置项名称
   * @param defaultValue 默认值
   * @return 配置项的值
   */
  public long getLong(String name, long defaultValue) {
    return this.contains(name) ? this.getLong(name) : defaultValue;
  }

  /**
   * 获取配置项的值。
   * 
   * @param name 配置项名称
   * @return 配置项的值
   */
  public float getFloat(String name) {
    return Float.parseFloat(this.getString(name));
  }

  /**
   * 获取配置项的值，如果配置项不存在，则返回默认值。
   * 
   * @param name         配置项名称
   * @param defaultValue 默认值
   * @return 配置项的值
   */
  public float getFloat(String name, float defaultValue) {
    return this.contains(name) ? this.getFloat(name) : defaultValue;
  }

  /**
   * 获取配置项的值。
   * 
   * @param name 配置项名称
   * @return 配置项的值
   */
  public double getDouble(String name) {
    return Double.parseDouble(this.getString(name));
  }

  /**
   * 获取配置项的值，如果配置项不存在，则返回默认值。
   * 
   * @param name         配置项名称
   * @param defaultValue 默认值
   * @return 配置项的值
   */
  public double getDouble(String name, double defaultValue) {
    return this.contains(name) ? this.getDouble(name) : defaultValue;
  }

  /**
   * 获取配置项的值。
   * 
   * @param name 配置项名称
   * @return 配置项的值
   */
  public BigDecimal getBigDecimal(String name) {
    return new BigDecimal(this.getString(name));
  }

  /**
   * 获取配置项的值，如果配置项不存在，则返回默认值。
   * 
   * @param name         配置项名称
   * @param defaultValue 默认值
   * @return 配置项的值
   */
  public BigDecimal getBigDecimal(String name, BigDecimal defaultValue) {
    return this.contains(name) ? this.getBigDecimal(name) : defaultValue;
  }

  /**
   * 获取配置项的值。
   * 
   * @param name 配置项名称
   * @return 配置项的值
   */
  public boolean getBoolean(String name) {
    return Boolean.parseBoolean(this.getString(name));
  }

  /**
   * 获取配置项的值，如果配置项不存在，则返回默认值。
   * 
   * @param name         配置项名称
   * @param defaultValue 默认值
   * @return 配置项的值
   */
  public boolean getBoolean(String name, boolean defaultValue) {
    return this.contains(name) ? this.getBoolean(name) : defaultValue;
  }
}
