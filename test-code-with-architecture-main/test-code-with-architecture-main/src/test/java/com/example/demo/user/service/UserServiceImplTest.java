package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUUIDHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.dto.UserCreate;
import com.example.demo.user.domain.dto.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

public class UserServiceImplTest {

    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void init(){
        FakeMailSender fakeMailSender = new FakeMailSender();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        this.userServiceImpl = UserServiceImpl.builder()
                .clockHolder(new TestClockHolder(1678530673958L))
                .uuidHolder(new TestUUIDHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa"))
                .userRepository(fakeUserRepository)
                .certificationService(new CertificationService(fakeMailSender))
                .build();

        fakeUserRepository.save(User.builder()
                        .id(1L)
                        .email("kams1011@naver.com")
                        .nickname("kams")
                        .address("Seoul")
                        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa")
                        .status(UserStatus.ACTIVE)
                        .lastLoginAt(0L)
                        .build());
        fakeUserRepository.save(User.builder()
                        .id(2L)
                        .email("kams1012@naver.com")
                        .nickname("kams2")
                        .address("Seoul2")
                        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaB")
                        .status(UserStatus.PENDING)
                        .lastLoginAt(0L)
                        .build());
    }

    @Test
    void getByEmail_은_ACTIVE_상태인_유저를_찾아올_수_있다(){

        String email = "kams1011@naver.com";

        User result = userServiceImpl.getByEmail(email);

        assertThat(result.getNickname()).isEqualTo("kams");

    }

    @Test
    void getByEmail_은_PENDING_상태인_유저를_찾아올_수_없다(){

        String email = "kams1012@naver.com";

        assertThatThrownBy(() -> {
            User result = userServiceImpl.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);

    }


    @Test
    void getById_는_ACTIVE_상태인_유저를_찾아올_수_있다(){

        Long Id = 1L;

        User result = userServiceImpl.getById(Id);

        assertThat(result.getNickname()).isEqualTo("kams");

    }

    @Test
    void getById_는_PENDING_상태인_유저를_찾아올_수_없다(){

        Long Id = 2L;
        assertThatThrownBy(() -> {
            User result = userServiceImpl.getById(Id);
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
        // when
        User result = userServiceImpl.create(dto);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa");

    }


    @Test
    void userUpdateDto_를_이용하여_유저를_수정할_수_있다(){
        // given
        UserUpdate dto = UserUpdate.builder()
                .address("Gyeongi")
                .nickname("kams2223")
                .build();
        // when
        userServiceImpl.update(1L, dto);

        User result = userServiceImpl.getById(1L);
        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getAddress()).isEqualTo("Gyeongi");
        assertThat(result.getNickname()).isEqualTo("kams2223");


    }

    @Test
    void user를_로그인_시키면_마지막_로그인_시간이_변경된다(){
        // given
        // when
        userServiceImpl.login(1L);


        // then
        User result = userServiceImpl.getById(1L);
        assertThat(result.getLastLoginAt()).isEqualTo(1678530673958L);
    }

    @Test
    void PENDING_상태의_사용자는_로그인_할_수_없다(){
        // given
        // when
        userServiceImpl.login(2L);

        // then
        User result = userServiceImpl.getById(1L);
        assertThat(result.getLastLoginAt()).isLessThan(1678530673958L);
    }

    @Test
    void PENDING_상태의_사용자는_인증코드로_ACTIVE_시킬_수_있다(){
        // given
//        UserEntity result = userService.getById(2L);
        // when
        userServiceImpl.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaB");
        // then

        User result = userServiceImpl.getById(2L);
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증코드를_받으면_에러를_던진다(){
        // given
        // when

        // then
        assertThatThrownBy(() -> {
            userServiceImpl.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaa4aab");
       }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}
