package io.github.leaderman.makemoney.hustle.eastmoney.service;

import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import io.github.leaderman.makemoney.hustle.eastmoney.domain.entity.FundPriceEntity;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.FundBucketPriceModel;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.FundPriceModel;

public interface FundPriceService extends IService<FundPriceEntity> {
  /**
   * 保存基金价格。
   * 
   * @param models 基金价格列表。
   */
  void save(List<FundPriceModel> models);

  /**
   * 获取基金价格数量。
   * 
   * @param code  基金代码。
   * @param start 开始时间。
   * @param end   结束时间。
   * @return 基金价格数量。
   */
  long count(String code, LocalDateTime start, LocalDateTime end);

  /**
   * 获取当天的基金价格数量。
   * 
   * @param code 基金代码。
   * @return 基金价格数量。
   */
  long count(String code);

  /**
   * 获取基金分桶价格。
   * 
   * @param code  基金代码。
   * @param size  价格数量。
   * @param start 开始时间。
   * @param end   结束时间。
   * @return 基金分桶价格列表。
   */
  List<FundBucketPriceModel> bucket(String code, int size, LocalDateTime start, LocalDateTime end);

  /**
   * 获取当天的基金分桶价格。
   * 
   * @param code 基金代码。
   * @param size 价格数量。
   * @return 基金分桶价格列表。
   */
  List<FundBucketPriceModel> bucket(String code, int size);
}
