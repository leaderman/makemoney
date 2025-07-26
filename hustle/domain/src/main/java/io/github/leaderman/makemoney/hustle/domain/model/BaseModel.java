package io.github.leaderman.makemoney.hustle.domain.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public abstract class BaseModel {
  private Long id;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
