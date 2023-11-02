package com.mtvs.arzip.service;

import com.mtvs.arzip.domain.dto.token.TokenDto;
import com.mtvs.arzip.domain.dto.token.TokenRequestDto;
import com.mtvs.arzip.domain.dto.user.UserDto;
import com.mtvs.arzip.domain.dto.user.UserJoinRequest;
import com.mtvs.arzip.domain.dto.user.UserLoginRequest;
import com.mtvs.arzip.domain.entity.RefreshToken;
import com.mtvs.arzip.domain.entity.User;
import com.mtvs.arzip.exception.AppException;
import com.mtvs.arzip.exception.ErrorCode;
import com.mtvs.arzip.jwt.TokenProvider;
import com.mtvs.arzip.repository.RefreshTokenRepository;
import com.mtvs.arzip.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

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

    public TokenDto login(UserLoginRequest userLoginRequest) {
        User user = userRepository.findById(userLoginRequest.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUNDED));

        if (!bCryptPasswordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }

        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = userLoginRequest.authenticationToken();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {

        // 1. RefreshToken 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 2. AccessToken에서 Id 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 id를 기반으로 RefreshToken 값 가져오기
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.LOGOUT_USER));

        // 4. RefreshToken이 일치 하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new AppException(ErrorCode.TOKEN_NOT_MATCH);
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return tokenDto;
    }


}
