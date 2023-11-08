package com.mtvs.arzip.controller;

import com.mtvs.arzip.domain.dto.token.TokenDto;
import com.mtvs.arzip.domain.dto.token.TokenRequestDto;
import com.mtvs.arzip.domain.dto.user.*;
import com.mtvs.arzip.exception.Response;
import com.mtvs.arzip.service.EmailService;
import com.mtvs.arzip.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Api(tags = "회원 가입 및 로그인")
@Validated
public class UserRestController {

    private final UserService userService;
    private final EmailService emailService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody @Valid UserJoinRequest userJoinRequest) {
        UserDto userDto = userService.join(userJoinRequest);
        return Response.success(UserJoinResponse.of(userDto));
    }

    @PostMapping("/login")
    public Response<TokenDto> login(@RequestBody @Valid UserLoginRequest userLoginRequest) {
        System.out.println("🏠로그인 한 email : " + userLoginRequest.getEmail());
        System.out.println("🏠로그인 한 password : " + userLoginRequest.getPassword());
        return Response.success(userService.login(userLoginRequest));
    }

    @PostMapping("/reissue")
    public Response<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return Response.success(userService.reissue(tokenRequestDto));
    }

    // 인증 메일 보내기
    @PostMapping("/send-auth-email")
    public Response<String> sendAuthEmail(@RequestBody UserEmailRequest email) throws Exception {
        System.out.println("email = " + email.getEmail());
        return Response.success(emailService.sendLoginAuthMessage(email.getEmail()));
    }

    // 인증 메일 확인 하기
    @PostMapping("/check-auth-email")
    public Response<Boolean> checkAuthEmail(@RequestBody UserCodeRequest request) {
        System.out.println(request.getCode());
        if (emailService.getData(request.getCode()) == null) return Response.success(false);
        else return Response.success(true);
    }

}
