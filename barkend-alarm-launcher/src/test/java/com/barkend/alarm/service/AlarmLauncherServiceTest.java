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

import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Testcontainers
@AutoConfigureWireMock(port = 0)
@ActiveProfiles("test")
class AlarmLauncherServiceTest {

	@Container
	private static final KafkaContainer kafka = new KafkaContainer(
			DockerImageName.parse("confluentinc/cp-kafka:6.2.0"));

	@DynamicPropertySource
	static void setup(DynamicPropertyRegistry registry) {
		registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
	}

	@SpyBean
	AlarmLauncherService alarmLauncherService;

	@Test
	void fireAlarm() throws InterruptedException {

		this.alarmLauncherService.fireAlarm();

		verify(this.alarmLauncherService).setRelayStatus(Boolean.TRUE);
		Thread.sleep(5000);
		verify(this.alarmLauncherService).setRelayStatus(Boolean.FALSE);
	}

	@Test
	void timeWithinFiringPeriod() {
		assertThat(this.alarmLauncherService.timeWithinFiringPeriod(LocalTime.of(20, 00))).isTrue();
		assertThat(this.alarmLauncherService.timeWithinFiringPeriod(LocalTime.of(8, 00))).isTrue();
		assertThat(this.alarmLauncherService.timeWithinFiringPeriod(LocalTime.of(16, 00))).isTrue();
		assertThat(this.alarmLauncherService.timeWithinFiringPeriod(LocalTime.of(10, 00))).isTrue();
		assertThat(this.alarmLauncherService.timeWithinFiringPeriod(LocalTime.of(01, 00))).isFalse();
		assertThat(this.alarmLauncherService.timeWithinFiringPeriod(LocalTime.of(07, 00))).isFalse();
	}

}
