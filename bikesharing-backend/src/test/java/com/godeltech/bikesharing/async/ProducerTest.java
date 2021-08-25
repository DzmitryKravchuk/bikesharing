package com.godeltech.bikesharing.async;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.godeltech.bikesharing.models.TimeInUseModel;
import com.godeltech.bikesharing.models.request.EquipmentTimeInUseRequest;
import com.godeltech.bikesharing.service.AbstractIntegrationTest;
import com.godeltech.bikesharing.utils.TimeInUseUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProducerTest extends AbstractIntegrationTest {
  private static final Long ID = 1L;
  private static final EquipmentTimeInUseRequest EQUIPMENT_TIME_IN_USE_MODEL = TimeInUseUtils
      .getEquipmentTimeInUseRequest(ID);
  private static TimeInUseModel expected;

  @BeforeEach
  @DataSet(value = {
      "/dataset/equipmentGroup/equipmentGroupAll.yml",
      "/dataset/equipmentStatus/equipmentStatusAll.yml",
      "/dataset/equipmentItem/equipmentItemAll.yml",},
      cleanBefore = true, useSequenceFiltering = false)
  public void setUp() {
    expected = TimeInUseUtils.getTimeInUseModel(ID);
  }

  @Test
  @ExpectedDataSet(value = "/dataset/timeInUse/timeInUseCreated.yml")
  public void shouldCreateTimeInUse() throws InterruptedException {
    expected.setMaintenanceDate(null);
    producer.sendMessage(EQUIPMENT_TIME_IN_USE_MODEL);
    Thread.sleep(500);

    var actual = timeInUseService.getOrCreateByEquipmentItemId(ID);

    assertEquals(expected.getEquipmentItem(), actual.getEquipmentItem());
    assertEquals(expected.getMinutesInUse(), actual.getMinutesInUse());
  }

  @Test
  @DataSet(value = "/dataset/timeInUse/timeInUseInitial.yml")
  @ExpectedDataSet(value = "/dataset/timeInUse/timeInUseUpdated.yml")
  public void shouldAddTimeInUse() throws InterruptedException {
    producer.sendMessage(EQUIPMENT_TIME_IN_USE_MODEL);
    Thread.sleep(5000);

    var actual = timeInUseService.getOrCreateByEquipmentItemId(ID);

    assertEquals(expected.getEquipmentItem(), actual.getEquipmentItem());
    assertEquals(expected.getMinutesInUse(), actual.getMinutesInUse());
  }
}