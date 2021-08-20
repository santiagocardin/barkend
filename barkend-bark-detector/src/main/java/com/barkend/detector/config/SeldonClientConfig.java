package com.barkend.detector.config;

import com.barkend.detector.client.ApiClient;
import com.barkend.detector.client.api.ExternalAmbassadorApiApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
public class SeldonClientConfig {

	@Value("${application.seldon.endpoint}")
	private String endpoint;

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder.build();
	}

	@Bean
	@Primary
	public ExternalAmbassadorApiApi seldonApi(ApiClient apiClient) {
		apiClient.setBasePath(endpoint);
		return new ExternalAmbassadorApiApi(apiClient);
	}

}
