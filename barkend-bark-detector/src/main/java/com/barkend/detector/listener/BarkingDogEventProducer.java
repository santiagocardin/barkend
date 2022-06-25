package com.barkend.detector.listener;

import com.barkend.detector.model.BarkingDogEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BarkingDogEventProducer {

	private final KafkaTemplate<String, BarkingDogEvent> kafkaTemplate;

	public BarkingDogEventProducer(KafkaTemplate<String, BarkingDogEvent> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendBarkingDogEvent(BarkingDogEvent event) {
		kafkaTemplate.send("BARKING_DOG", "main", event);
	}

}