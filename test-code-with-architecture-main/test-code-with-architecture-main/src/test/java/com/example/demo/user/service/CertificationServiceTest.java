package com.example.demo.user.service;

import com.example.demo.mock.FakeMailSender;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CertificationServiceTest {

    @Test
    public void 이메일과_컨텐츠가_제대로_만들어져서_보내지는지_테스트한다(){
        FakeMailSender fakeMailSender = new FakeMailSender();
        //given
        CertificationService certificationService = new CertificationService(fakeMailSender);
        //when
        certificationService.send("kams1011@naver.com", 1, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa");
        //then
        assertThat(fakeMailSender.email).isEqualTo("kams1011@naver.com");
        assertThat(fakeMailSender.title).isEqualTo("Please certify your email address");
    }
}
