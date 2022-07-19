package com.barkend.detector.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.barkend.detector.client.model.SeldonMessage;
import com.barkend.detector.model.SoundClip;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
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
@AutoConfigureWireMock(port = 0)
class SeldonClientTest {

  @Container
  private static final KafkaContainer kafka =
      new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.0"));

  @DynamicPropertySource
  static void setup(DynamicPropertyRegistry registry) {
    kafka.start();
    registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
  }

  @Autowired SeldonClient seldonClient;

  @Test
  public void predictBarkClip() {

    SoundClip clip = new SoundClip();
    clip.setContent("fakecontent".getBytes());
    SeldonMessage prediction = seldonClient.predict(clip);
    Double dog = (Double) prediction.getData().getNdarray().get(4);

    assertEquals(dog, 0.9999957);
  }
}
