package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.dto.UserCreate;
import com.example.demo.user.domain.dto.UserUpdate;
import com.example.demo.user.infrastructure.UserEntity;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private JavaMailSender javaMailSender;


    @Test
    void getByEmail_은_ACTIVE_상태인_유저를_찾아올_수_있다(){

        String email = "kams1011@naver.com";

        User result = userService.getByEmail(email);

        assertThat(result.getNickname()).isEqualTo("kams");

    }

    @Test
    void getByEmail_은_PENDING_상태인_유저를_찾아올_수_없다(){

        String email = "kams1012@naver.com";

        assertThatThrownBy(() -> {
            User result = userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);

    }


    @Test
    void getById_는_ACTIVE_상태인_유저를_찾아올_수_있다(){

        Long Id = 1L;

        User result = userService.getById(Id);

        assertThat(result.getNickname()).isEqualTo("kams");

    }

    @Test
    void getById_는_PENDING_상태인_유저를_찾아올_수_없다(){

        Long Id = 2L;
        assertThatThrownBy(() -> {
            User result = userService.getById(Id);
        }).isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void userCreateDto_를_이용하여_유저를_생성할_수_있다(){
        // given
        UserCreate dto = UserCreate.builder()
                .email("kams1013@naver.com")
                .address("Jeonju")
                .nickname("JeonJu")
                .build();
        BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
        // when
        User result = userService.create(dto);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);

    }


    @Test
    void userUpdateDto_를_이용하여_유저를_수정할_수_있다(){
        // given
        UserUpdate dto = UserUpdate.builder()
                .address("Gyeongi")
                .nickname("kams2223")
                .build();
        // when
        userService.update(1L, dto);

        User result = userService.getById(1L);
        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getAddress()).isEqualTo("Gyeongi");
        assertThat(result.getNickname()).isEqualTo("kams2223");


    }

    @Test
    void user를_로그인_시키면_마지막_로그인_시간이_변경된다(){
        // given
        // when
        userService.login(1L);


        // then
        User result = userService.getById(1L);
        assertThat(result.getLastLoginAt()).isGreaterThan(0L); //FIXME
    }

    @Test
    void PENDING_상태의_사용자는_로그인_할_수_없다(){
        // given
        // when
        userService.login(2L);

        // then
        User result = userService.getById(1L);
        assertThat(result.getLastLoginAt()).isLessThan(1L); //FIXME
    }

    @Test
    void PENDING_상태의_사용자는_인증코드로_ACTIVE_시킬_수_있다(){
        // given
//        UserEntity result = userService.getById(2L);
        // when
        userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaab");
        // then

        User result = userService.getById(2L);
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증코드를_받으면_에러를_던진다(){
        // given
        // when

        // then
        assertThatThrownBy(() -> {
            userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaa4aab");
       }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}
