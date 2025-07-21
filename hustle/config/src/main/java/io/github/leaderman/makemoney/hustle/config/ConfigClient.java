package io.github.leaderman.makemoney.hustle.config;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lark.oapi.service.bitable.v1.model.AppTableRecord;

import io.github.leaderman.makemoney.hustle.feishu.BitableClient;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * 配置客户端，从飞书多维表格中加载配置。
 */
@Component
@Slf4j
public class ConfigClient {
  private final BitableClient bitableClient;
  private final List<String> bitables;

  private final Map<String, Object> fields = new ConcurrentHashMap<>();

  public ConfigClient(BitableClient bitableClient, @Value("${config.bitables}") List<String> bitables) {
    this.bitableClient = bitableClient;
    this.bitables = bitables;
  }

  /**
   * 定时加载配置。
   */
  @PostConstruct
  @Scheduled(cron = "${config.cron}")
  public void load() {
    try {
      for (String bitable : this.bitables) {
        List<AppTableRecord> records = bitableClient.listRecords(bitable);
        for (AppTableRecord record : records) {
          this.fields.put(record.getFields().get("名称").toString(), record.getFields().get("值"));
        }

        log.info("多维表格 {} 加载完成，共加载 {} 条记录", bitable, records.size());
      }
    } catch (Exception e) {
      log.error("多维表格配置加载失败：{}", ExceptionUtils.getStackTrace(e));
    }
  }

  /**
   * 判断配置项是否存在。
   * 
   * @param name 配置项名称
   * @return 配置项是否存在
   */
  public boolean contains(String name) {
    return this.fields.containsKey(name);
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

    return this.fields.get(name);
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
