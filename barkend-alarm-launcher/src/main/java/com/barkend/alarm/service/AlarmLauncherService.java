package com.barkend.alarm.service;

import com.barkend.alarm.client.RelayManagementClient;
import com.barkend.alarm.config.ApplicationConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlarmLauncherService {

	private final RelayManagementClient relayManagementClient;

	private final ApplicationConfig applicationConfig;

	public void fireAlarm() {

		if (timeWithinFiringPeriod(LocalTime.now())) {

			log.info("Starting alarm...");
			setRelayStatus(Boolean.TRUE);

			playAlarm(applicationConfig.getDuration());

			log.info("Stopping alarm...");
			setRelayStatus(Boolean.FALSE);
		}
		else {
			log.info("Time within silence period ({} to {}). Alarm won't be fired.",
					applicationConfig.silenceTimeStart(), applicationConfig.silenceTimeEnd());
		}
	}

	protected boolean timeWithinFiringPeriod(LocalTime time) {
		if (applicationConfig.isSilenceTimeConfigured()) {
			return time.isBefore(applicationConfig.silenceTimeStart())
					&& time.isAfter(applicationConfig.silenceTimeEnd());
		}

		return true;
	}

	protected void setRelayStatus(boolean status) {

		String bodyFormat = "apikey=%s&value=%d";
		String request = String.format(bodyFormat, applicationConfig.getApiKey(), status ? 1 : 0);

		relayManagementClient.switchRelay(request);
	}

	private void playAlarm(Duration duration) {
		try {
			Thread.sleep(duration.toMillis());
		}
		catch (InterruptedException e) {
			log.error("Error timing out alarm", e);
		}
	}

}
