package com.godeltech.bikesharing.utils;

import com.godeltech.bikesharing.models.lookup.RentStatusModel;
import com.godeltech.bikesharing.persistence.entity.lookup.RentStatus;

public class RentStatusUtils {
  public static final String NAME = "NewRentStatus";
  public static final String CODE = "LASTING";
  public static final Long ID = 1L;

  public static RentStatus getRentStatus() {
    var rentStatus = new RentStatus();
    rentStatus.setId(ID);
    rentStatus.setName(NAME);
    rentStatus.setCode(CODE);
    return rentStatus;
  }

  public static RentStatusModel getRentStatusModel() {
    var rentStatusModel = new RentStatusModel();
    rentStatusModel.setId(ID);
    rentStatusModel.setName(NAME);
    rentStatusModel.setCode(CODE);
    return rentStatusModel;
  }
}
