package com.barkend.detector.client;

import com.barkend.detector.client.model.SeldonMessage;
import com.barkend.detector.model.SoundClip;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class SeldonClientIT {

	@Autowired
	SeldonClient seldonClient;

	@Test
	public void predictBarkClip() throws IOException {

		Path audioFile = Paths
				.get("/home/scardin/IdeaProjects/barkend/barkend-bark-detector/src/test/resources/audio/bark.wav");

		byte[] audio = Files.readAllBytes(audioFile);
		SoundClip clip = new SoundClip();
		clip.setContent(audio);
		SeldonMessage prediction = seldonClient.predict(clip);
		Double dog = (Double) prediction.getData().getNdarray().get(0);

		assertNotNull(dog);
	}

}