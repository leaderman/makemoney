package io.github.leaderman.makemoney.hustle.eastmoney.service;

import java.util.List;

import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.SecurityModel;

public interface SecurityService {
  void sync(List<SecurityModel> models);

  boolean hasPosition(String securityCode);
}
