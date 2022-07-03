package com.barkend.detector.listener;

import com.barkend.detector.model.BarkingDogEvent;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BarkingDogEventProducerTest {

	@Autowired
	BarkingDogEventProducer eventProducer;

	@Test
	@Disabled
	void sendBarkingDogEvent() throws InterruptedException {
		eventProducer.sendBarkingDogEvent(BarkingDogEvent.builder().type("many-dogs").build());

	}

}