package io.github.leaderman.makemoney.hustle.config;

import java.util.Map;

import org.springframework.core.env.MapPropertySource;

public class BitablePropertySource extends MapPropertySource {
  public static final String NAME = "bitable";

  public BitablePropertySource(Map<String, Object> source) {
    super(NAME, source);
  }

  public void refresh(Map<String, Object> source) {
    this.source.putAll(source);
  }
}
