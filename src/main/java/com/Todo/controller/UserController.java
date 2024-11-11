package com.Todo.controller;

import com.Todo.dto.Response;
import com.Todo.dto.request.UserJoinRequest;
import com.Todo.dto.request.UserLoginRequest;
import com.Todo.dto.response.UserJoinResponse;
import com.Todo.dto.response.UserLoginResponse;
import com.Todo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Tag(name = "user api", description = "사용자 관리 api")
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController { //todo: error response aop 설정

    private final UserService userService;

    @Operation(summary = "로그인", description = "로그인 api")
    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest){
        String username = userLoginRequest.getUsername();
        String password = userLoginRequest.getPassword();

        log.info("[UserController login] try login username: {} password: {}", username, password);

        return Response.success(userService.login(username, password));
    }

    @Operation(summary = "회원가입", description = "회원가입")
    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest userJoinRequest){
        String username = userJoinRequest.getUsername();
        String password = userJoinRequest.getPassword();
        String email = userJoinRequest.getEmail();
        log.info("[UserController join] try join: {} password: {}, email: {}", username, password, email);

        return Response.success(userService.join(username, password, email));
    }
}
