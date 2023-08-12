package com.example.demo.user.domain;

import com.example.demo.user.domain.dto.UserCreate;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class User {

    private Long id;

    private String email;

    private String nickname;

    private String address;

    private String certificationCode;

    private UserStatus status;

    private Long lastLoginAt;

    @Builder
    public User(Long id, String email, String nickname, String address, String certificationCode,
                UserStatus status, Long lastLoginAt) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.address = address;
        this.certificationCode = certificationCode;
        this.status = status;
        this.lastLoginAt = lastLoginAt;
    }

    public static User from(UserCreate userCreate){
        User user = User.builder()
                .email(userCreate.getEmail())
                .nickname(userCreate.getNickname())
                .address(userCreate.getAddress())
                .status(UserStatus.PENDING)
                .certificationCode(UUID.randomUUID().toString())
                .build();
        return user;
    }
}
