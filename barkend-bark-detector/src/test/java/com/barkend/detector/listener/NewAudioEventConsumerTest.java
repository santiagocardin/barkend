package com.barkend.detector.listener;

import com.barkend.detector.service.BarkDetectorService;
import com.barkend.detector.types.S3NewEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class NewAudioEventConsumerTest {

	@Container
	private static final KafkaContainer kafka = new KafkaContainer(
			DockerImageName.parse("confluentinc/cp-kafka:6.2.0"));

	@DynamicPropertySource
	static void setup(DynamicPropertyRegistry registry) {
		kafka.start();
		registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
	}

	@MockBean
	private BarkDetectorService barkDetectorService;

	@Test
	void shouldProcessNewAudioClip() {

		S3NewEvent newAudioClip = new S3NewEvent();
		newAudioClip.setEventName("myClip");
		newAudioClip.setKey("audio/myClip");

		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		KafkaProducer<String, S3NewEvent> producer = new KafkaProducer(props);

		producer.send(new ProducerRecord<>("AUDIO_CREATED", newAudioClip));

		await().untilAsserted(() -> verify(barkDetectorService, times(1)).processClip("myClip"));
	}

}