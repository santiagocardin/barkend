/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.barkend.alarm.service;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
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
@AutoConfigureWireMock(port = 0)
@ActiveProfiles("test")
class AlarmLauncherServiceTest {

  @Container
  private static final KafkaContainer kafka =
      new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.0"));

  @DynamicPropertySource
  static void setup(DynamicPropertyRegistry registry) {
    kafka.start();
    registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
  }

  @SpyBean AlarmLauncherService alarmLauncherService;

  @MockBean Clock clock;

  @BeforeEach
  void setupClock() {
    when(clock.getZone()).thenReturn(ZoneId.of("Europe/Madrid"));
  }

  @Test
  void shouldFireAlarm() throws InterruptedException {

    when(clock.instant()).thenReturn(Instant.parse("2020-12-01T10:05:23.653Z"));

    this.alarmLauncherService.fireAlarm();

    verify(this.alarmLauncherService).setRelayStatus(Boolean.TRUE);
    await()
        .untilAsserted(
            () -> {
              verify(this.alarmLauncherService).setRelayStatus(Boolean.FALSE);
            });
  }

  @Test
  void shouldNotFireAlarmAtNight() throws InterruptedException {

    when(clock.instant()).thenReturn(Instant.parse("2020-12-01T05:05:23.653Z"));

    this.alarmLauncherService.fireAlarm();

    verify(this.alarmLauncherService, times(0)).setRelayStatus(anyBoolean());
  }
}
