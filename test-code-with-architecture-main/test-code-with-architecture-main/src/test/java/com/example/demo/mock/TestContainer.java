package com.example.demo.mock;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.common.service.port.UUIDHolder;
import com.example.demo.post.controller.port.PostService;
import com.example.demo.post.service.PostServiceImpl;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.controller.UserController;
import com.example.demo.user.controller.UserControllerTest;
import com.example.demo.user.controller.port.*;
import com.example.demo.user.domain.dto.UserUpdate;
import com.example.demo.user.service.CertificationService;
import com.example.demo.user.service.UserServiceImpl;
import com.example.demo.user.service.port.MailSender;
import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;

import java.util.UUID;

public class TestContainer {

    public final MailSender mailSender;
    public final UserRepository userRepository;
    public final PostRepository postRepository;

    public final UserReadService userReadService;
    public final UserCreateService userCreateService;
    public final UserUpdateService userUpdateService;
    public final AuthenticationService authenticationService;
    public final PostService postService;

    public final CertificationService certificationService;

    public final UserController userController;


    @Builder
    public TestContainer(ClockHolder clockHolder, UUIDHolder uuidHolder){
        this.mailSender = new FakeMailSender();
        this.userRepository = new FakeUserRepository();
        this.postRepository = new FakePostRepository();
        this.certificationService = new CertificationService(mailSender);
        UserServiceImpl userServiceImpl = UserServiceImpl.builder()
                .uuidHolder(uuidHolder)
                .clockHolder(clockHolder)
                .userRepository(this.userRepository)
                .certificationService(this.certificationService)
                .build();
        this.userReadService = userServiceImpl;
        this.userCreateService = userServiceImpl;
        this.userUpdateService = userServiceImpl;
        this.authenticationService = userServiceImpl;
        this.postService = PostServiceImpl.builder()
                .postRepository(postRepository)
                .userRepository(userRepository)
                .clockHolder(clockHolder)
                .build();
        this.userController = UserController.builder()
                .userReadService(userReadService)
                .userUpdateService(userUpdateService)
                .userCreateService(userCreateService)
                .authenticationService(authenticationService)
                .build();
    }

}
