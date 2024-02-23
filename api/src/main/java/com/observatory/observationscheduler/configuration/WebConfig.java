package com.observatory.observationscheduler.configuration;

import com.observatory.observationscheduler.domain.celestialevent.StatusEnumConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StatusEnumConverter());
    }
}
