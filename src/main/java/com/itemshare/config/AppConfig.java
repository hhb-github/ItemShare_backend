package com.itemshare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableJpaAuditing
@EnableAsync
public class AppConfig {

    public static final String TIME_ZONE = "Asia/Shanghai";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.of(TIME_ZONE));

    @Bean
    public String timezone() {
        return TIME_ZONE;
    }
}
