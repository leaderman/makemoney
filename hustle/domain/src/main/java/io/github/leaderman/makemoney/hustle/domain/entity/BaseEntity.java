package io.github.leaderman.makemoney.hustle.domain.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

@Data
public abstract class BaseEntity {
  @TableId(type = IdType.AUTO)
  private Long id;
  @TableField(value = "created_at")
  private LocalDateTime createdAt;
  @TableField(value = "updated_at")
  private LocalDateTime updatedAt;
}
