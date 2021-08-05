package com.godeltech.bikesharing.service;

import com.godeltech.bikesharing.models.EquipmentItemModel;
import org.springframework.data.domain.Page;

public interface EquipmentItemService {
  int PAGE_SIZE = 10;

  EquipmentItemModel getByRegistrationNumber(String registrationNumber);

  EquipmentItemModel save(EquipmentItemModel equipmentItemModel);

  EquipmentItemModel getById(Long id);

  void updateEquipmentItemStatus(String registrationNumber, String status);

  String getEquipmentStatusCodeByRegistrationNumber(String equipmentRegistrationNumber);

  Page<EquipmentItemModel> getAllByStatusCode(String statusCode, int pageNum);
}
