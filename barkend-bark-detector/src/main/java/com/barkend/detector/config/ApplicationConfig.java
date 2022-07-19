package com.barkend.detector.config;

import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Configuration
@ConfigurationProperties("application")
@Validated
public class ApplicationConfig {

  @NotNull private Double predictionMinScore;
}
