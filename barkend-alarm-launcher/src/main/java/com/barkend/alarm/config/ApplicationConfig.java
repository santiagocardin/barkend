package com.barkend.alarm.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
@ConfigurationProperties("application.alarm")
@Data
public class ApplicationConfig {

	private final static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

	private String endpoint;

	private String apiKey;

	private Duration duration;

	private String silenceTimeStart;

	private String silenceTimeEnd;

	public LocalTime silenceTimeStart() {
		return LocalTime.parse(silenceTimeStart, TIME_FORMATTER);
	}

	public LocalTime silenceTimeEnd() {
		return LocalTime.parse(silenceTimeEnd, TIME_FORMATTER);
	}

	public boolean isSilenceTimeConfigured() {
		return (silenceTimeStart != null && silenceTimeEnd != null);
	}

}
