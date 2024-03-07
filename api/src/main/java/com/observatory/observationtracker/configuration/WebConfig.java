package com.observatory.observationtracker.configuration;

import com.observatory.observationtracker.domain.celestialevent.StatusEnumConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Automatically convert the CelestialEventStatus enum to Strings
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StatusEnumConverter());
    }
}
