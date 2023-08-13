package com.example.demo.common.infrastrcuture;

import com.example.demo.common.service.port.UUIDHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SystemUUIDHolder implements UUIDHolder {

    @Override
    public String random(){
        return UUID.randomUUID().toString();
    }
}
