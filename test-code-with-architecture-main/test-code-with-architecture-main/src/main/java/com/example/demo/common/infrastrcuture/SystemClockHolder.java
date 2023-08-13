package com.example.demo.common.infrastrcuture;

import com.example.demo.common.service.port.ClockHolder;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
public class SystemClockHolder implements ClockHolder {
    @Override
    public long mills() {
        return Clock.systemUTC().millis();
    }
}
