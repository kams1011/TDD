package com.example.demo.user.controller;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.dto.UserUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


public class UserControllerTest {

    @Test
    void 사용자는_특정_유저의_정보를_개인정보는_소거된_채_전달_받을_수_있다() throws Exception{
        //given

        TestContainer testContainer = TestContainer.builder().build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("kok202@naver.com")
                .nickname("kok202")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaab")
                .lastLoginAt(100L)
                .build());

        //when
        ResponseEntity<UserResponse> result = testContainer.userController.getById(1);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getEmail()).isEqualTo("kok202@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("kok202");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);

    }

    @Test
    void 사용자는_존재하지_않는_유저의_아이디로_api_호출할_경우_404_응답을_받는다() throws Exception{
        Long id = 441L;
        //given

        TestContainer testContainer = TestContainer.builder().build();
        //when
        //then
        assertThatThrownBy(() -> {
            testContainer.userController.getById(1);
        }).isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void 사용자는_인증코드로_계정을_활성화_할_수_있다() throws Exception{
        //given

        TestContainer testContainer = TestContainer.builder().build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("kok202@naver.com")
                .nickname("kok202")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaab")
                .lastLoginAt(100L)
                .build());

        //when
        ResponseEntity<Void> result = testContainer.userController.verifyEmail(1, "aaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaab");

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
        assertThat(testContainer.userRepository.getById(1l).getStatus()).isEqualTo(UserStatus.ACTIVE);

    }

    @Test
    void 사용자는_인증코드가_일치하지_않을_경우_권한_없음_에러를_내려준다() throws Exception{
        //given

        TestContainer testContainer = TestContainer.builder().build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("kok202@naver.com")
                .nickname("kok202")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaab")
                .lastLoginAt(100L)
                .build());

        //when
        assertThatThrownBy(() -> {
            testContainer.userController.verifyEmail(1, "aaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaac");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);


    }

    @Test
    void 사용자는_내_정보를_불러올_때_개인정보인_주소도_갖고_올_수_있다() throws Exception{
        //given

        TestContainer testContainer = TestContainer.builder().clockHolder(() -> 1678530673958L).build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("kok202@naver.com")
                .nickname("kok202")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaab")
                .lastLoginAt(100L)
                .build());

        //when
        ResponseEntity<MyProfileResponse> result = testContainer.userController.getMyInfo("kok202@naver.com");

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getEmail()).isEqualTo("kok202@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("kok202");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getAddress()).isEqualTo("Seoul");
    }

    @Test
    void 사용자는_내_정보를_수정할_수_있다() throws Exception{
        //given
        UserUpdate dto = UserUpdate.builder()
                .nickname("newKams")
                .address("Busan")
                .build();
    }
}
