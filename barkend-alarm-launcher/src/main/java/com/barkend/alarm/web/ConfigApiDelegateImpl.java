package com.barkend.alarm.web;

import com.barkend.alarm.api.ConfigApiDelegate;
import com.barkend.alarm.config.ApplicationConfig;
import com.barkend.alarm.model.AlarmConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ConfigApiDelegateImpl implements ConfigApiDelegate {

	private final ApplicationConfig applicationConfig;

	public ConfigApiDelegateImpl(ApplicationConfig applicationConfig) {
		this.applicationConfig = applicationConfig;
	}

	@Override
	public ResponseEntity<AlarmConfiguration> getConfig() {
		AlarmConfiguration config = new AlarmConfiguration();
		config.setDuration(Math.toIntExact(applicationConfig.getDuration().toSeconds()));
		config.silenceTimeStart(applicationConfig.getSilenceTimeStart());
		config.silenceTimeEnd(applicationConfig.getSilenceTimeEnd());

		return new ResponseEntity<>(config, HttpStatus.OK);

	}

}
