package com.barkend.alarm.web.mapper;

import com.barkend.alarm.config.ApplicationConfig;
import com.barkend.alarm.model.AlarmConfiguration;
import org.mapstruct.Mapper;

import java.time.Duration;

@Mapper
public interface ConfigMapper {

	default Integer map(Duration value) {
		return Math.toIntExact(value.toSeconds());
	}

	AlarmConfiguration toModel(ApplicationConfig config);

}
