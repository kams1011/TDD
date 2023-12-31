package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserResponseTest {


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
        UserResponse userResponse = UserResponse.from(user);
        //then
        assertThat(userResponse.getEmail()).isEqualTo("kams1011@naver.com");
        assertThat(userResponse.getNickname()).isEqualTo("kams");
        assertThat(userResponse.getId()).isEqualTo(1L);
        assertThat(userResponse.getStatus()).isEqualTo(UserStatus.PENDING);
    }
}
