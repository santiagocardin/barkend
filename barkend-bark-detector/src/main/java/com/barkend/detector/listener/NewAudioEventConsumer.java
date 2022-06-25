package com.barkend.detector.listener;

import com.barkend.detector.model.SoundClip;
import com.barkend.detector.service.BarkDetectorService;
import com.barkend.detector.types.S3NewEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NewAudioEventConsumer {

	private final BarkDetectorService barkDetectorService;

	public NewAudioEventConsumer(BarkDetectorService barkDetectorService) {
		this.barkDetectorService = barkDetectorService;
	}

	@KafkaListener(topics = "AUDIO_CREATED")
	public void processNewAudio(S3NewEvent audio) {

		final String clipName = audio.getKey().replace("audio/", "");

		try {
			barkDetectorService.processClip(clipName);
		}
		catch (Exception e) {
			log.warn("Could no process clip {}. Reason: {}", clipName, e.getLocalizedMessage());
		}
	}

}
