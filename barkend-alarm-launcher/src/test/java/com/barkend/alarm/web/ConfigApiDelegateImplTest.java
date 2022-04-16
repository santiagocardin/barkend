package com.barkend.alarm.web;

import com.barkend.alarm.api.ConfigApiController;
import com.barkend.alarm.config.ApplicationConfig;
import com.barkend.alarm.web.mapper.ConfigMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Duration;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ ConfigApiController.class, ConfigApiDelegateImpl.class, ConfigMapper.class })
class ConfigApiDelegateImplTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	ApplicationConfig applicationConfig;

	@Test
	void getConfig() throws Exception {

		when(applicationConfig.getDuration()).thenReturn(Duration.ofSeconds(30));
		when(applicationConfig.getSilenceTimeStart()).thenReturn("23:30");
		when(applicationConfig.getSilenceTimeEnd()).thenReturn("07:30");

		this.mockMvc.perform(get("/scardin/barkend-alarm-launcher/1.0.0/config"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.duration").value(30))
				.andExpect(MockMvcResultMatchers.jsonPath("$.silenceTimeStart").value("23:30"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.silenceTimeEnd").value("07:30"))
				.andExpect(status().isOk());
	}

}