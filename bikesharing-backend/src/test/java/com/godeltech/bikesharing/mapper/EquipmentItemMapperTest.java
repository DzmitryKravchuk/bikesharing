package com.godeltech.bikesharing.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.godeltech.bikesharing.mapper.lookup.EquipmentGroupMapper;
import com.godeltech.bikesharing.mapper.lookup.EquipmentGroupMapperImpl;
import com.godeltech.bikesharing.mapper.lookup.EquipmentStatusMapper;
import com.godeltech.bikesharing.mapper.lookup.EquipmentStatusMapperImpl;
import com.godeltech.bikesharing.utils.EquipmentItemUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {
    EquipmentItemMapperImpl.class,
    EquipmentGroupMapperImpl.class,
    EquipmentStatusMapperImpl.class})
class EquipmentItemMapperTest {
  private static final Long ID = 1L;
  @Autowired
  private EquipmentItemMapper equipmentItemMapper;

  @Test
  void shouldMapEntityToModel() {
    var equipmentItem = EquipmentItemUtils.getEquipmentItem();
    equipmentItem.setId(ID);
    var expected = EquipmentItemUtils.getEquipmentItemModel(ID);

    var actual = equipmentItemMapper.mapToModel(equipmentItem);

    assertEquals(expected, actual);
  }

  @Test
  void shouldMapModelToEntity() {
    var equipmentItemModel = EquipmentItemUtils.getEquipmentItemModel(ID);

    var actual = equipmentItemMapper.mapToEntity(equipmentItemModel);

    var expected = EquipmentItemUtils.getEquipmentItem();
    expected.setId(ID);
    assertEquals(expected, actual);
  }
}