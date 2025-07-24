package io.github.leaderman.makemoney.hustle.coze.workflow.stock;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FinanceInfo {
  @JsonProperty("jump_link")
  private String jumpLink;
  @JsonProperty("report_date")
  private String reportDate;
  @JsonProperty("report_descrip")
  private String reportDescrip;
  @JsonProperty("report_fields")
  private List<ReportField> reportFields;
}
