package com.barkend.detector.client;

import com.barkend.detector.client.model.SeldonMessage;
import com.barkend.detector.model.SoundClip;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 0)
class SeldonClientTest {

	@Autowired
	SeldonClient seldonClient;

	@Test
	public void predictBarkClip() {

		SoundClip clip = new SoundClip();
		clip.setContent("fakecontent".getBytes());
		SeldonMessage prediction = seldonClient.predict(clip);
		Double dog = (Double) prediction.getData().getNdarray().get(4);

		assertEquals(dog, 0.9999957);
	}

}