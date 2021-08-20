package com.barkend.alarm.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureWireMock
@ActiveProfiles("test")
class AlarmLauncherServiceTest {

	@SpyBean
	AlarmLauncherService alarmLauncherService;

	@Test
	void fireAlarm() {

		alarmLauncherService.fireAlarm();

		verify(alarmLauncherService).setRelayStatus(Boolean.TRUE);
		verify(alarmLauncherService).setRelayStatus(Boolean.FALSE);
	}

	@Test
	void timeWithinFiringPeriod() {
		assertTrue(alarmLauncherService.timeWithinFiringPeriod(LocalTime.of(20, 00)));
		assertTrue(alarmLauncherService.timeWithinFiringPeriod(LocalTime.of(8, 00)));
		assertTrue(alarmLauncherService.timeWithinFiringPeriod(LocalTime.of(16, 00)));
		assertTrue(alarmLauncherService.timeWithinFiringPeriod(LocalTime.of(10, 00)));
		assertFalse(alarmLauncherService.timeWithinFiringPeriod(LocalTime.of(01, 00)));
		assertFalse(alarmLauncherService.timeWithinFiringPeriod(LocalTime.of(07, 00)));
	}

}