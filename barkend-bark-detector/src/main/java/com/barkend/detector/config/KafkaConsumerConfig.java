package com.barkend.detector.config;

import com.barkend.detector.types.S3NewEvent;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

	@Bean
	public DefaultKafkaConsumerFactory consumerFactory(KafkaProperties properties) {
		Map<String, Object> props = properties.buildConsumerProperties();
		DefaultKafkaConsumerFactory cf = new DefaultKafkaConsumerFactory(props, new StringDeserializer(),
				new JsonDeserializer<>(S3NewEvent.class).ignoreTypeHeaders());
		return cf;
	}

}
