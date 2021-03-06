package com.godeltech.bikesharing.service.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.godeltech.bikesharing.models.lookup.EquipmentGroupModel;
import com.godeltech.bikesharing.service.AbstractIntegrationTest;
import com.godeltech.bikesharing.utils.EquipmentGroupUtils;
import org.junit.jupiter.api.Test;

class EquipmentGroupManagementServiceTest extends AbstractIntegrationTest {
  private static final Long ID = 1L;
  private static final String NEW_NAME = "SuperBicycle";
  private static final EquipmentGroupModel equipmentGroup = EquipmentGroupUtils.getEquipmentGroupModel();

  @Test
  public void shouldSaveNewEquipmentGroup() {

    var actual = equipmentGroupManagementService.save(equipmentGroup);

    assertNotNull(actual);
    assertEquals(ID, actual.getId());
  }

  @Test
  @DataSet(value = "/dataset/equipmentGroup/equipmentGroupAll.yml",
      cleanBefore = true, useSequenceFiltering = false)
  @ExpectedDataSet(value = "/dataset/equipmentGroup/equipmentGroupUpdated.yml")
  public void shouldUpdateEquipmentGroup() {
    equipmentGroup.setId(ID);
    equipmentGroup.setName(NEW_NAME);

    var actual = equipmentGroupManagementService.update(equipmentGroup, ID);

    assertEquals(equipmentGroup, actual);
  }
}