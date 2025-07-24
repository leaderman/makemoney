package io.github.leaderman.makemoney.hustle.coze.workflow.stock;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ReportField {
  @JsonProperty("item_field")
  private String field;
  @JsonProperty("item_title")
  private String title;
  @JsonProperty("item_tongbi")
  private String tongbi;
  @JsonProperty("item_value")
  private String value;
}
