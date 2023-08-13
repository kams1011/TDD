package com.example.demo.post.domain;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PostTest {


    @Test
    public void PostCreate로_게시물을_만들_수_있다(){
        //given
        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("helloworld")
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
        Post post = Post.from(user, postCreate);
        //then
        assertThat(post.getContent()).isEqualTo("helloworld");
        assertThat(post.getWriter().getEmail()).isEqualTo("kams1011@naver.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("kams");
        assertThat(post.getWriter().getAddress()).isEqualTo("Suwon");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo("AAA_AAAA_AAAA");
    }
}
