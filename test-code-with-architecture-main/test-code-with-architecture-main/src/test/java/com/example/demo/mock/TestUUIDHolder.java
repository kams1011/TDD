package com.example.demo.mock;

import com.example.demo.common.service.port.UUIDHolder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestUUIDHolder implements UUIDHolder {

    private final String uuid;

    @Override
    public String random() {
        return uuid;
    }
}
