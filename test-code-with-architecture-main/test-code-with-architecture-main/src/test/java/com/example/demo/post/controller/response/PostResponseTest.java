package com.example.demo.post.controller.response;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PostResponseTest {

    @Test
    public void Post로_응답을_생성할_수_있다(){
        //given
        Post post = Post.builder()
                .content("helloworld")
                .writer(User.builder()
                        .id(1L)
                        .email("kams1011@naver.com")
                        .nickname("kams")
                        .address("Suwon")
                        .status(UserStatus.PENDING)
                        .certificationCode("AAA_AAAA_AAAA")
                        .build())
                .build();

        //when
        PostResponse postResponse = PostResponse.from(post);
        //then
        assertThat(postResponse.getContent()).isEqualTo("helloworld");
        assertThat(postResponse.getWriter().getEmail()).isEqualTo("kams1011@naver.com");
        assertThat(postResponse.getWriter().getNickname()).isEqualTo("kams");
        assertThat(postResponse.getWriter().getStatus()).isEqualTo(UserStatus.PENDING);
    }
}
