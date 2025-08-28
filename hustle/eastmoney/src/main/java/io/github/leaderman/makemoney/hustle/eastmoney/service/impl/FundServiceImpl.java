package io.github.leaderman.makemoney.hustle.eastmoney.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.FundModel;
import io.github.leaderman.makemoney.hustle.eastmoney.service.FundService;

@Service
public class FundServiceImpl implements FundService {
  @Override
  public void sync(List<FundModel> funds) {
  }
}
