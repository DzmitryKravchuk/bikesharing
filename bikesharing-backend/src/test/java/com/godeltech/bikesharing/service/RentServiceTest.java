package com.godeltech.bikesharing.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.godeltech.bikesharing.utils.EquipmentItemUtils;
import com.godeltech.bikesharing.utils.RentOperationUtils;
import org.junit.jupiter.api.Test;

public class RentServiceTest extends AbstractIntegrationTest {
  private static final String IN_USE_STATUS = "IN_USE";

  @Test
  public void shouldStartRentOperationProperly() {
    var equipmentModel = EquipmentItemUtils.getEquipmentItemModel(null);
    equipmentItemService.save(equipmentModel);
    var request = RentOperationUtils.getStartRentOperationRequest();
    var rentOperationModel = rentService.startRentOperation(request);
    var rentOperationModelFromBase = rentService.getById(rentOperationModel.getId());
    var equipmentFromBase = equipmentItemService.getByRegistrationNumber(equipmentModel.getRegistrationNumber());
    assertNotNull(rentOperationModelFromBase.getId());
    assertNotNull(equipmentFromBase);
    assertNotNull(clientService.getByPhoneNumber(request.getClientPhoneNumber()));
    assertEquals(rentOperationModelFromBase, rentOperationModel);
    assertEquals(equipmentFromBase.getEquipmentStatus().getCode(), IN_USE_STATUS);
  }

}