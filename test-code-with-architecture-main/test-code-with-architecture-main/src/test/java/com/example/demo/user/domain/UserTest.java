package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUUIDHolder;
import com.example.demo.user.domain.dto.UserCreate;
import com.example.demo.user.domain.dto.UserUpdate;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class UserTest {

    @Test
    public void User_는_UserCreate_객체로_생성할_수_있다(){
        //given
        UserCreate dto = UserCreate.builder()
                .email("kams1013@naver.com")
                .address("Jeonju")
                .nickname("toto")
                .build();
        //when
        User user = User.from(dto, new TestUUIDHolder("aaa-aaa-aaaa"));

        //then
        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("kams1013@naver.com");
        assertThat(user.getAddress()).isEqualTo("Jeonju");
        assertThat(user.getNickname()).isEqualTo("toto");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
    }

    @Test
    public void User_는_UserUpdate_객체로_수정할_수_있다(){
        //given
        UserUpdate dto = UserUpdate.builder()
                .address("JeonjuChange")
                .nickname("totoChange")
                .build();
        User user = User.builder()
                .id(1L)
                .email("kams1011@naver.com")
                .nickname("kams")
                .address("Suwon")
                .status(UserStatus.PENDING)
                .certificationCode("AAA_AAAA_AAAA")
                .build();
        //when
        user = user.update(dto);

        //then
        assertThat(user.getAddress()).isEqualTo("JeonjuChange");
        assertThat(user.getNickname()).isEqualTo("totoChange");
        assertThat(user.getEmail()).isEqualTo("kams1011@naver.com");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("AAA_AAAA_AAAA");
    }

    @Test
    public void User_는_로그인을_할_수_있고_로그인시_마지막_로그인_시간이_변경된다(){
        //given
        User user = User.builder()
                .id(1L)
                .email("kams1011@naver.com")
                .nickname("kams")
                .address("Suwon")
                .status(UserStatus.PENDING)
                .certificationCode("AAA_AAAA_AAAA")
                .build();

        //when
        user = user.login(new TestClockHolder(1678530673958L));

        //then
        assertThat(user.getAddress()).isEqualTo("Suwon");
        assertThat(user.getNickname()).isEqualTo("kams");
        assertThat(user.getEmail()).isEqualTo("kams1011@naver.com");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("AAA_AAAA_AAAA");
        assertThat(user.getLastLoginAt()).isEqualTo(1678530673958L);

    }

    @Test
    public void User_는_유효한_인증_코드로_계정을_활성화_할_수_있다(){
        //given
        User user = User.builder()
                .id(1L)
                .email("kams1011@naver.com")
                .nickname("kams")
                .address("Suwon")
                .status(UserStatus.PENDING)
                .certificationCode("AAA_AAAA_AAAA")
                .build();

        //when
        user = user.certification("AAA_AAAA_AAAA");

        //then
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);

    }

    @Test
    public void User_는_잘못된_인증_코드로_계정을_활성화_하려하면_에러를_던진다(){
        //given
        User user = User.builder()
                .id(1L)
                .email("kams1011@naver.com")
                .nickname("kams")
                .address("Suwon")
                .status(UserStatus.PENDING)
                .certificationCode("AAA_AAAA_AAAA")
                .build();

        //when

        //then
        assertThatThrownBy(() -> {
            user.certification("AAA_AAAA_AAABA");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);

    }

}
