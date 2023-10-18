package com.mtvs.arzip.service;

import com.mtvs.arzip.domain.dto.user.UserDto;
import com.mtvs.arzip.domain.dto.user.UserJoinRequest;
import com.mtvs.arzip.domain.dto.user.UserLoginRequest;
import com.mtvs.arzip.domain.entity.User;
import com.mtvs.arzip.exception.AppException;
import com.mtvs.arzip.exception.ErrorCode;
import com.mtvs.arzip.repository.UserRepository;
import com.mtvs.arzip.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${jwt.token.secret}")
    private String secretKey;
    private Long expiredTimeMs = 1000 * 60 * 60l;

    public UserDto join(UserJoinRequest userJoinRequest) {
        userRepository.findById(userJoinRequest.getId()).ifPresent(user -> {
            throw new AppException(ErrorCode.DUPLICATED_USER_ID, String.format("UserId %s is duplicated", userJoinRequest.getId()));
        });

        User user = userRepository.save(userJoinRequest.toEntity(bCryptPasswordEncoder.encode(userJoinRequest.getPassword())));
        return UserDto.of(user);
    }

    public String login(UserLoginRequest userLoginRequest) {
        User user = userRepository.findById(userLoginRequest.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUNDED));

        if (!bCryptPasswordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }

        return JwtTokenUtil.createJwt(user.getNo(), secretKey, "access", expiredTimeMs);
    }

}
