package com.godeltech.bikesharing.async;

import com.godeltech.bikesharing.models.EquipmentTimeInUseModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class Producer {
  @Value("${spring.rabbitmq.exchange-name}")
  private String EXCHANGE;
  @Value("${spring.rabbitmq.routing-key}")
  private String ROUTING_KEY;

  private final AmqpTemplate amqpTemplate;

  public void sendMessage(EquipmentTimeInUseModel item) {

    amqpTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, item);
  }
}
