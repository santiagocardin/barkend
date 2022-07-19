package com.barkend.detector.listener;

import com.barkend.detector.model.BarkingDogEvent;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class BarkingDogEventProducerTest {

  @Container
  private static final KafkaContainer kafka =
      new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.0"));

  @DynamicPropertySource
  static void setup(DynamicPropertyRegistry registry) {
    kafka.start();
    registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
  }

  @Autowired BarkingDogEventProducer eventProducer;

  @Test
  @Disabled
  void sendBarkingDogEvent() throws InterruptedException {
    eventProducer.sendBarkingDogEvent(BarkingDogEvent.builder().type("many-dogs").build());
  }
}
