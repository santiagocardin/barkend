package com.barkend.alarm.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "sonoff", url = "${application.alarm.endpoint}")
public interface RelayManagementClient {

	@RequestMapping(method = RequestMethod.PUT, value = "/0",
			headers = { "Accept=application/json", "Content-Type=application/x-www-form-urlencoded" })
	void switchRelay(String request);

}
