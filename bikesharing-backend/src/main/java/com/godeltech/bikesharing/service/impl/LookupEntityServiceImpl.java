package com.godeltech.bikesharing.service.impl;

import com.godeltech.bikesharing.exception.ResourceNotFoundException;
import com.godeltech.bikesharing.mapper.LookupMapper;
import com.godeltech.bikesharing.models.LookupEntityModel;
import com.godeltech.bikesharing.persistence.entity.common.LookupEntity;
import com.godeltech.bikesharing.persistence.repository.LookupRepository;
import com.godeltech.bikesharing.service.LookupEntityService;
import java.lang.reflect.ParameterizedType;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public abstract class LookupEntityServiceImpl<M extends LookupEntityModel, E extends LookupEntity>
    implements LookupEntityService<M, E> {

  private final LookupRepository<E> repository;
  private final LookupMapper<M, E> mapper;

  @Override
  @Transactional(readOnly = true)
  public M getByCode(String code) {
    log.info("getByCode: {}", code);
    return repository.findByCode(code).map(mapper::mapToModel)
        .orElseThrow(() -> new ResourceNotFoundException(
            String.format("%s not found with code: %s", getEntityInfo(), code)));
  }

  @Override
  @Transactional(readOnly = true)
  public Stream<M> findAll() {
    return repository.findAll()
        .stream()
        .map(mapper::mapToModel);
  }

  @SuppressWarnings("unchecked")
  String getEntityInfo() {
    return ((Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1])
        .getSimpleName();
  }
}
