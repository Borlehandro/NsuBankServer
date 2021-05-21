package com.sibdever.nsu_bank_system_server.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class TimeConfig {
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
