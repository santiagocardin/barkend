package com.barkend.alarm.config;

import com.barkend.alarm.client.RelayManagementClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = RelayManagementClient.class)
public class FeignConfig {

}
