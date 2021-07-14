package com.godeltech.bikesharing.service.impl;

import com.godeltech.bikesharing.exception.ResourceNotFoundException;
import com.godeltech.bikesharing.mapper.ClientAccountMapper;
import com.godeltech.bikesharing.models.ClientAccountModel;
import com.godeltech.bikesharing.models.request.NewClientAccountRequest;
import com.godeltech.bikesharing.persistence.entity.ClientAccount;
import com.godeltech.bikesharing.persistence.repository.ClientAccountRepository;
import com.godeltech.bikesharing.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

  private final ClientAccountRepository repository;
  private final ClientAccountMapper clientAccountMapper;

  @Override
  public ClientAccountModel save(NewClientAccountRequest request) {
    log.info("save: {}", request);
    var clientAccount = clientAccountMapper.mapToEntity(request);
    clientAccount.setRating(0);
    return clientAccountMapper.mapToModel(repository.save(clientAccount));
  }

  @Override
  public ClientAccountModel getByPhoneNumber(String phoneNum) {
    log.info("getByPhoneNumber: {}", phoneNum);
    var clientAccount = repository.findByPhoneNumber(phoneNum)
        .orElseThrow(() -> new ResourceNotFoundException(ClientAccount.class.getName(), "phoneNumber", phoneNum));
    return clientAccountMapper.mapToModel(clientAccount);
  }

  @Override
  public ClientAccountModel getOrCreateByPhoneNumber(String clientPhoneNumber) {
    log.info("getOrCreateByPhoneNumber: {}", clientPhoneNumber);
    var searchPattern = getSearchPattern(clientPhoneNumber);
    var clientAccount = repository.findByPhoneNumberEndsWith(searchPattern)
        .orElseGet(() -> createAccountByPhoneNumber(clientPhoneNumber));
    return clientAccountMapper.mapToModel(clientAccount);
  }

  private ClientAccount createAccountByPhoneNumber(String clientPhoneNumber) {
    var clientAccount = new ClientAccount(clientPhoneNumber);
    repository.save(clientAccount);
    return clientAccount;
  }

  private String getSearchPattern(String clientPhoneNumber) {
    if (clientPhoneNumber.length() > 4) {
      return clientPhoneNumber.substring(clientPhoneNumber.length() - 4);
    } else {
      return clientPhoneNumber;
    }
  }

}