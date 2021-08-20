package com.barkend.processor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConfigurationProperties("application")
@Data
public class ApplicationConfig {

	private Duration timeWindow;

	private Integer maxBarksAllowed;

}
