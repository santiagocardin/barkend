package com.barkend.alarm.listener;

import com.barkend.alarm.model.TooNoisyEvent;
import com.barkend.alarm.service.AlarmLauncherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TooNoisyEventConsumer {

	private final AlarmLauncherService alarmLauncherService;

	@KafkaListener(topics = "TOO_NOISY")
	public void processNewAudio(TooNoisyEvent event) {

		final String reason = event.getReason();

		log.info("Firing alarm due to {}", reason);

		try {
			alarmLauncherService.fireAlarm();
		}
		catch (Exception e) {
			log.error("Error firing alarm. Reason: {}", e.getLocalizedMessage());
		}
	}

}
