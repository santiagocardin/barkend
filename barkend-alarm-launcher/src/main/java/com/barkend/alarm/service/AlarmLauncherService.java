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

import java.time.Duration;
import java.time.LocalTime;

import com.barkend.alarm.client.RelayManagementClient;
import com.barkend.alarm.config.ApplicationConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlarmLauncherService {

	private final RelayManagementClient relayManagementClient;

	private final ApplicationConfig applicationConfig;

	public void fireAlarm() throws InterruptedException {

		if (timeWithinFiringPeriod(LocalTime.now())) {

			log.info("Starting alarm...");
			setRelayStatus(Boolean.TRUE);

			playAlarm(this.applicationConfig.getDuration());

			log.info("Stopping alarm...");
			setRelayStatus(Boolean.FALSE);
		}
		else {
			log.info("Time within silence period ({} to {}). Alarm won't be fired.",
					this.applicationConfig.silenceTimeStart(), this.applicationConfig.silenceTimeEnd());
		}
	}

	protected boolean timeWithinFiringPeriod(LocalTime time) {
		if (this.applicationConfig.isSilenceTimeConfigured()) {
			return time.isBefore(this.applicationConfig.silenceTimeStart())
					&& time.isAfter(this.applicationConfig.silenceTimeEnd());
		}

		return true;
	}

	protected void setRelayStatus(boolean status) {

		String bodyFormat = "apikey=%s&value=%d";
		String request = String.format(bodyFormat, this.applicationConfig.getApiKey(), status ? 1 : 0);

		this.relayManagementClient.switchRelay(request);
	}

	private void playAlarm(Duration duration) throws InterruptedException {
		Thread.sleep(duration.toMillis());
	}

}
