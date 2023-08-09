package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserServiceTest {

    @Autowired
    private UserService userService;


    @Test
    void getByEmail_은_ACTIVE_상태인_유저를_찾아올_수_있다(){

        String email = "kams1011@naver.com";

        UserEntity result = userService.getByEmail(email);

        assertThat(result.getNickname()).isEqualTo("kams");

    }

    @Test
    void getByEmail_은_PENDING_상태인_유저를_찾아올_수_없다(){

        String email = "kams1012@naver.com";

        assertThatThrownBy(() -> {
            UserEntity result = userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);

    }


    @Test
    void getById_는_ACTIVE_상태인_유저를_찾아올_수_있다(){

        Long Id = 1L;

        UserEntity result = userService.getById(Id);

        assertThat(result.getNickname()).isEqualTo("kams");

    }

    @Test
    void getById_는_PENDING_상태인_유저를_찾아올_수_없다(){

        Long Id = 2L;
        assertThatThrownBy(() -> {
            UserEntity result = userService.getById(Id);
        }).isInstanceOf(ResourceNotFoundException.class);

    }
}
