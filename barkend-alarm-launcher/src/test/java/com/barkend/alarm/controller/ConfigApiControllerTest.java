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

package com.barkend.alarm.controller;

import java.time.Duration;

import com.barkend.alarm.config.ApplicationConfig;
import com.barkend.alarm.controller.mapper.ConfigMapper;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ ConfigApiController.class, ConfigMapper.class })
class ConfigApiControllerTest {

	@MockBean
	ApplicationConfig applicationConfig;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void getConfig() throws Exception {
		// given
		given(this.applicationConfig.getDuration()).willReturn(Duration.ofSeconds(30));
		given(this.applicationConfig.getSilenceTimeStart()).willReturn("23:30");
		given(this.applicationConfig.getSilenceTimeEnd()).willReturn("07:30");

		// when
		// then
		this.mockMvc.perform(get("/alarm-launcher/config"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.duration").value(30))
				.andExpect(MockMvcResultMatchers.jsonPath("$.silenceTimeStart").value("23:30"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.silenceTimeEnd").value("07:30"))
				.andExpect(status().isOk());

	}

}
