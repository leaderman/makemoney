package io.github.leaderman.makemoney.hustle.eastmoney.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.github.leaderman.makemoney.hustle.eastmoney.domain.entity.FundEntity;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.FundModel;
import io.github.leaderman.makemoney.hustle.eastmoney.mapper.FuncMapper;
import io.github.leaderman.makemoney.hustle.eastmoney.service.FundService;

@Service
public class FundServiceImpl extends ServiceImpl<FuncMapper, FundEntity> implements FundService {
  @Override
  public void sync(List<FundModel> models) {
    // 获取代码列表。
    List<String> codes = models.stream().map(FundModel::getCode).collect(Collectors.toList());

    // 获取代码对应的 ID。
    Map<String, Long> codeToIds = this.lambdaQuery().select(FundEntity::getCode, FundEntity::getId)
        .in(FundEntity::getCode, codes).list().stream()
        .collect(Collectors.toMap(FundEntity::getCode, FundEntity::getId));

    // 转换为实体，且设置 ID。
    List<FundEntity> entities = models.stream().map(FundModel::to)
        .peek(entity -> entity.setId(codeToIds.get(entity.getCode())))
        .collect(Collectors.toList());

    // 保存或更新。
    this.saveOrUpdateBatch(entities);
  }
}
