package com.barkend.alarm.web;

import com.barkend.alarm.api.ConfigApiDelegate;
import com.barkend.alarm.config.ApplicationConfig;
import com.barkend.alarm.model.AlarmConfiguration;
import com.barkend.alarm.web.mapper.ConfigMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ConfigApiDelegateImpl implements ConfigApiDelegate {

	private final ApplicationConfig applicationConfig;

	private final ConfigMapper configMapper;

	public ConfigApiDelegateImpl(ApplicationConfig applicationConfig, ConfigMapper configMapper) {
		this.applicationConfig = applicationConfig;
		this.configMapper = configMapper;
	}

	@Override
	public ResponseEntity<AlarmConfiguration> getConfig() {
		AlarmConfiguration config = configMapper.toModel(applicationConfig);
		return new ResponseEntity<>(config, HttpStatus.OK);
	}

}
