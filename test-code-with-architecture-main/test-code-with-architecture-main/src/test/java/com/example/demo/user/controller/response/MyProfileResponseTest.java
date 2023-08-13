package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MyProfileResponseTest {



    @Test
    public void User_로_응답을_생성할_수_있다(){
        //give
        User user = User.builder()
                .id(1L)
                .email("kams1011@naver.com")
                .nickname("kams")
                .address("Suwon")
                .status(UserStatus.PENDING)
                .certificationCode("AAA_AAAA_AAAA")
                .build();
        //when
        MyProfileResponse myProfileResponse = MyProfileResponse.from(user);
        //then
        assertThat(myProfileResponse.getEmail()).isEqualTo("kams1011@naver.com");
        assertThat(myProfileResponse.getNickname()).isEqualTo("kams");
        assertThat(myProfileResponse.getAddress()).isEqualTo("Suwon");
        assertThat(myProfileResponse.getId()).isEqualTo(1L);
        assertThat(myProfileResponse.getStatus()).isEqualTo(UserStatus.PENDING);
    }
}
