package com.barkend.detector.service;

import com.barkend.detector.client.SeldonClient;
import com.barkend.detector.client.model.DefaultData;
import com.barkend.detector.client.model.SeldonMessage;
import com.barkend.detector.listener.BarkingDogEventProducer;
import com.barkend.detector.model.AudioCategory;
import com.barkend.detector.model.SoundClip;
import com.barkend.detector.repository.S3Repository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class BarkDetectorServiceTest {

	@Container
	private static final KafkaContainer kafka = new KafkaContainer(
			DockerImageName.parse("confluentinc/cp-kafka:6.2.0"));

	@DynamicPropertySource
	static void setup(DynamicPropertyRegistry registry) {
		kafka.start();
		registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
	}

	@Autowired
	BarkDetectorService barkDetectorService;

	@MockBean
	S3Repository s3Repository;

	@MockBean
	SeldonClient seldonClient;

	@MockBean
	BarkingDogEventProducer eventProducer;

	@Test
	public void processOneDogBarking() throws IOException {

		SoundClip clip = new SoundClip();
		clip.setName("clip.wav");

		Mockito.when(s3Repository.downloadFile("clip.wav")).thenReturn(clip);
		Mockito.when(seldonClient.predict(clip)).thenReturn(getOneDogBarkingSeldonMessage());

		barkDetectorService.processClip("clip.wav");

		assertEquals(AudioCategory.ONE_DOG_BARKING, clip.getMostProbablePrediction().get().getCategory());
		Mockito.verify(eventProducer, Mockito.times(1)).sendBarkingDogEvent(any());
		Mockito.verify(s3Repository, Mockito.times(1)).addTagToFile(any(), any());
	}

	@Test
	public void processManyDogsBarking() throws IOException {

		SoundClip clip = new SoundClip();
		clip.setName("clip.wav");

		Mockito.when(s3Repository.downloadFile("clip.wav")).thenReturn(clip);
		Mockito.when(seldonClient.predict(clip)).thenReturn(getManyDogsBarkingSeldonMessage());

		barkDetectorService.processClip("clip.wav");

		assertEquals(AudioCategory.MANY_DOGS_BARKING, clip.getMostProbablePrediction().get().getCategory());
		Mockito.verify(eventProducer, Mockito.times(1)).sendBarkingDogEvent(any());
		Mockito.verify(s3Repository, Mockito.times(1)).addTagToFile(any(), any());
	}

	private SeldonMessage getOneDogBarkingSeldonMessage() {
		SeldonMessage message = new SeldonMessage();
		DefaultData data = new DefaultData();
		data.setNames(Arrays.asList("birds_chirping", "dog_barking", "many_dogs_barking", "people_talking", "siren",
				"engine", "chainsaw"));
		data.setNdarray(Arrays.asList(0.1D, 0.99, 0.005, 0.005, 0.005, 0.005, 0.06));
		message.setData(data);
		return message;
	}

	private SeldonMessage getManyDogsBarkingSeldonMessage() {
		SeldonMessage message = new SeldonMessage();
		DefaultData data = new DefaultData();
		data.setNames(Arrays.asList("birds_chirping", "dog_barking", "many_dogs_barking", "people_talking", "siren",
				"engine", "chainsaw"));
		data.setNdarray(Arrays.asList(0.1D, 0.1D, 0.99, 0.005, 0.005, 0.005, 0.06));
		message.setData(data);
		return message;
	}

}