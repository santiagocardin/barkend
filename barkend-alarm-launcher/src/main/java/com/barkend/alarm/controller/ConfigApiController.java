package com.barkend.alarm.controller;

import com.barkend.alarm.api.ConfigApi;
import com.barkend.alarm.config.ApplicationConfig;
import com.barkend.alarm.controller.mapper.ConfigMapper;
import com.barkend.alarm.model.AlarmConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class ConfigApiController implements ConfigApi {

	private final ApplicationConfig applicationConfig;

	private final ConfigMapper configMapper;

	public ConfigApiController(ApplicationConfig applicationConfig, ConfigMapper configMapper) {
		this.applicationConfig = applicationConfig;
		this.configMapper = configMapper;
	}

	@Override
	public ResponseEntity<AlarmConfiguration> getConfig() {
		AlarmConfiguration config = configMapper.toModel(applicationConfig);
		return ResponseEntity.ok(config);
	}

}
