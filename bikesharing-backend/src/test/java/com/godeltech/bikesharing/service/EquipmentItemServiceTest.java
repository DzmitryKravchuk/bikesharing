package com.godeltech.bikesharing.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.godeltech.bikesharing.utils.EquipmentItemUtils;
import org.junit.jupiter.api.Test;

public class EquipmentItemServiceTest extends AbstractIntegrationTest {
  private static final String IN_USE_STATUS = "IN_USE";

  @Test
  public void shouldChangeEquipmentItemStatus() {
    var equipmentModel = EquipmentItemUtils.getEquipmentItemModel(null);
    equipmentItemService.save(equipmentModel);
    equipmentItemService.setEquipmentItemStatusInUse(equipmentModel.getRegistrationNumber());
    var equipmentFromBase = equipmentItemService.getByRegistrationNumber(equipmentModel.getRegistrationNumber());
    assertNotNull(equipmentFromBase);
    assertEquals(equipmentFromBase.getEquipmentStatus().getCode(), IN_USE_STATUS);
  }

}