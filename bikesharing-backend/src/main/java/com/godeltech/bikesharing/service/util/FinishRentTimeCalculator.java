package com.godeltech.bikesharing.service.util;

import com.godeltech.bikesharing.models.RentCostModel;
import com.godeltech.bikesharing.models.RentOperationModel;
import com.godeltech.bikesharing.models.lookup.TimePeriodModel;
import com.godeltech.bikesharing.service.RentCostService;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FinishRentTimeCalculator {
  private final RentCostService rentCostService;

  public void setFinishTime(RentOperationModel rentOperationModel) {
    // get costs for equipment group
    var rentCostList = rentCostService.getByEquipmentGroupCode(rentOperationModel
        .getEquipmentItem().getEquipmentGroup().getCode());

    // make a map of CountUnits for periods (Key - long value for period's startTime)
    var countUnitMap = getCountUnitList(rentCostList);

    // get minutesCount to add to startTime, client payed for
    var plusMinutesCount = getPlusMinutesCount(rentOperationModel.getDeposit(), countUnitMap);

    rentOperationModel.setEndTime(rentOperationModel.getStartTime().plusMinutes(plusMinutesCount));
  }

  private long getPlusMinutesCount(Long deposit, List<CountUnit> countUnits) {
    var sortedCountUnitList = countUnits.stream()
        .sorted(Comparator.comparingLong(CountUnit::getPeriodStartTimeInMinutes)
            .reversed())
        .collect(Collectors.toList());
    for (CountUnit countUnit : sortedCountUnitList) {
      if (deposit >= countUnit.getFullCostOfPreviousPeriods()) {
        return countUnit.getFullTimeOfPreviousPeriodsInMinutes()
            + (deposit - countUnit.getFullCostOfPreviousPeriods())
            / countUnit.getCost5minCurrentPeriod() * 5;
      }
    }
    return 0;
  }

  private List<CountUnit> getCountUnitList(List<RentCostModel> rentCostList) {
    var countUnitList = rentCostList.stream()
        .map(this::getCountUnit)
        .sorted(Comparator.comparingLong(CountUnit::getPeriodStartTimeInMinutes))
        .collect(Collectors.toList());

    long timePeriodCount = 0;
    long costPeriodCount = 0;
    long fullTimeOfPreviousPeriod = 0;
    for (CountUnit e : countUnitList) {
      e.setFullCostOfPreviousPeriods(costPeriodCount);
      e.setFullTimeOfPreviousPeriodsInMinutes(fullTimeOfPreviousPeriod);
      costPeriodCount = updateTimePeriodCost(timePeriodCount, costPeriodCount, e);
      timePeriodCount = updateTimePeriodCount(timePeriodCount, e);
      fullTimeOfPreviousPeriod = getPeriodEndTimeInMinutes(e);
    }
    return countUnitList;
  }

  private Long getPeriodEndTimeInMinutes(CountUnit e) {
    if (!(e.getPeriodEndTimeInMinutes() == null)) {
      return e.getPeriodEndTimeInMinutes();
    }
    return e.getPeriodStartTimeInMinutes();
  }

  private Long getPeriodEndTimeInMinutes(RentCostModel model) {
    if (TimePeriodModel.PERIOD_1_HOUR_CODE.equals(model.getTimePeriod().getCode())) {
      return 60L;
    } else if (TimePeriodModel.PERIOD_3_HOURS_CODE.equals(model.getTimePeriod().getCode())) {
      return 180L;
    } else if (TimePeriodModel.PERIOD_6_HOURS_CODE.equals(model.getTimePeriod().getCode())) {
      return 360L;
    } else if (TimePeriodModel.PERIOD_12_HOURS_CODE.equals(model.getTimePeriod().getCode())) {
      return 720L;
    } else {
      return null;
    }
  }

  private CountUnit getCountUnit(RentCostModel m) {
    return new CountUnit(getPeriodStartTimeInMinutes(m),
        getPeriodEndTimeInMinutes(m),
        m.getCost(),
        0L,
        0L);
  }

  private Long getPeriodStartTimeInMinutes(RentCostModel model) {
    if (TimePeriodModel.PERIOD_1_HOUR_CODE.equals(model.getTimePeriod().getCode())) {
      return 0L;
    } else if (TimePeriodModel.PERIOD_3_HOURS_CODE.equals(model.getTimePeriod().getCode())) {
      return 60L;
    } else if (TimePeriodModel.PERIOD_6_HOURS_CODE.equals(model.getTimePeriod().getCode())) {
      return 180L;
    } else if (TimePeriodModel.PERIOD_12_HOURS_CODE.equals(model.getTimePeriod().getCode())) {
      return 360L;
    } else {
      return 720L;
    }
  }

  private long updateTimePeriodCost(long timePeriodCount, long costPeriodCount, CountUnit countUnit) {
    if (!(countUnit.getPeriodEndTimeInMinutes() == null)) {
      return costPeriodCount
          + countUnit.getCost5minCurrentPeriod() * (countUnit.getPeriodEndTimeInMinutes() - timePeriodCount) / 5;
    }
    return costPeriodCount;
  }

  private long updateTimePeriodCount(long timePeriodCount, CountUnit countUnit) {
    if (!(countUnit.getPeriodEndTimeInMinutes() == null)) {
      return countUnit.periodEndTimeInMinutes - timePeriodCount;
    }
    return timePeriodCount;
  }

  @Getter
  @AllArgsConstructor
  public static class CountUnit {
    private final Long periodStartTimeInMinutes;
    private final Long periodEndTimeInMinutes;
    private final Long cost5minCurrentPeriod;
    @Setter
    private Long fullCostOfPreviousPeriods;
    @Setter
    private Long fullTimeOfPreviousPeriodsInMinutes;
  }


}
