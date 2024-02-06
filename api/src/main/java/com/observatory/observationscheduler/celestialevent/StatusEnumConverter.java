package com.observatory.observationscheduler.celestialevent;

import org.springframework.core.convert.converter.Converter;

public class StatusEnumConverter implements Converter<String, CelestialEventStatus>
{
    @Override
    public CelestialEventStatus convert(String source) {
        return CelestialEventStatus.valueOf(source.toUpperCase());
    }
}
