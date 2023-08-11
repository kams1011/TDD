package com.example.demo.user.infrastructure;


import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql("/sql/user-repository-test-data.sql")
public class UserJpaRepositoryTest {

    @Autowired
    private UserJpaRepository userJpaRepository;


    @Test
    void findByIdAndStatus_로_유저_데이터를_찾아올_수_있다(){

        //when
        Optional<UserEntity> result = userJpaRepository.findByIdAndStatus(1, UserStatus.ACTIVE);

        //then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByIdAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다(){

        //when
        Optional<UserEntity> result = userJpaRepository.findByIdAndStatus(2, UserStatus.ACTIVE);

        //then
        assertThat(result.isEmpty()).isTrue();
    }


    @Test
    void findByEmailAndStatus_로_유저_데이터를_찾아올_수_있다(){
        //when
        Optional<UserEntity> result = userJpaRepository.findByEmailAndStatus("kams1011@naver.com", UserStatus.ACTIVE);

        //then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByEmailAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다(){
        //when
        Optional<UserEntity> result = userJpaRepository.findByEmailAndStatus("kams1011@naver.com2", UserStatus.ACTIVE);

        //then
        assertThat(result.isEmpty()).isTrue();
    }
}
