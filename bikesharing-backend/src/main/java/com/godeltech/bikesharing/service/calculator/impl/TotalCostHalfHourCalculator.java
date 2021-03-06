package com.godeltech.bikesharing.service.calculator.impl;

import com.godeltech.bikesharing.models.RentCostModel;
import com.godeltech.bikesharing.models.RentTimeModel;
import com.godeltech.bikesharing.models.enums.RentTimeUnit;
import com.godeltech.bikesharing.service.calculator.TotalCostCalculator;
import org.springframework.stereotype.Component;

@Component
public class TotalCostHalfHourCalculator implements TotalCostCalculator {

  @Override
  public Long calculateTotalCost(RentCostModel rentCostModel, RentTimeModel rentTimeModel) {
    return rentCostModel.getHalfHourPrice() * rentTimeModel.getAmount();
  }

  @Override
  public RentTimeUnit getType() {
    return RentTimeUnit.HALF_HOUR;
  }
}
