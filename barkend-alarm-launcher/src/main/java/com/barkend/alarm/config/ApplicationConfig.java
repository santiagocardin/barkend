/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.barkend.alarm.config;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Configuration
@ConfigurationProperties("application.alarm")
@Validated
@Data
public class ApplicationConfig {

	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

	@NotBlank
	private String endpoint;

	@NotBlank
	private String apiKey;

	private Duration duration;

	private String silenceTimeStart;

	private String silenceTimeEnd;

	public LocalTime silenceTimeStart() {
		return LocalTime.parse(this.silenceTimeStart, TIME_FORMATTER);
	}

	public LocalTime silenceTimeEnd() {
		return LocalTime.parse(this.silenceTimeEnd, TIME_FORMATTER);
	}

	public boolean isSilenceTimeConfigured() {
		return (this.silenceTimeStart != null && this.silenceTimeEnd != null);
	}

}
