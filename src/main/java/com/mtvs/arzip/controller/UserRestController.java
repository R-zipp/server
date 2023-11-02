package com.mtvs.arzip.controller;

import com.mtvs.arzip.domain.dto.token.TokenDto;
import com.mtvs.arzip.domain.dto.token.TokenRequestDto;
import com.mtvs.arzip.domain.dto.user.UserDto;
import com.mtvs.arzip.domain.dto.user.UserJoinRequest;
import com.mtvs.arzip.domain.dto.user.UserJoinResponse;
import com.mtvs.arzip.domain.dto.user.UserLoginRequest;
import com.mtvs.arzip.exception.Response;
import com.mtvs.arzip.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Api(tags = "회원 가입 및 로그인")
public class UserRestController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest userJoinRequest) {
        UserDto userDto = userService.join(userJoinRequest);
        return Response.success(UserJoinResponse.of(userDto));
    }

    @PostMapping("/login")
    public Response<TokenDto> login(@RequestBody UserLoginRequest userLoginRequest) {
        return Response.success(userService.login(userLoginRequest));
    }

    @PostMapping("/reissue")
    public Response<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return Response.success(userService.reissue(tokenRequestDto));
    }
}
