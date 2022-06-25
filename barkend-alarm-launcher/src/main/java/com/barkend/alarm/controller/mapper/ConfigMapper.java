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

package com.barkend.alarm.controller.mapper;

import java.time.Duration;

import com.barkend.alarm.config.ApplicationConfig;
import com.barkend.alarm.model.AlarmConfiguration;
import org.mapstruct.Mapper;

@Mapper
public interface ConfigMapper {

	default Integer map(Duration value) {
		return Math.toIntExact(value.toSeconds());
	}

	AlarmConfiguration toModel(ApplicationConfig config);

}