package com.mtvs.arzip.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtil {

    private static final Long accessTokenValidTime = Duration.ofMillis(30).toMillis(); // 만료 시간 30분
    private static final Long refreshTokenValidTime = Duration.ofDays(14).toMillis();  // 만료 시간 2주

    // 회원 정보 조회
    public static Long getUserNo(String token, String secretKey) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("userNo", Long.class);
    }

    // 토큰 유효 및 만료 확인
    public static boolean isExpired(String token, String secretKey) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            // Exception occurred, token is considered expired
            return true;
        }
    }


    // refresh 토큰 확인
    public static boolean isRefreshToken(String token, String secretKey) {

        Header header = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getHeader();

        if (header.get("type").toString().equals("refresh")) {
            return true;
        }
        return false;
    }

    // access 토큰 확인
    public static boolean isAccessToken(String token, String secretKey) {

        Header header = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getHeader();

        if (header.get("type").toString().equals("access")) {
            return true;
        }
        return false;
    }

    // access 토큰 생성
    public static String createAccessToken(Long userNo, String secretKey) {
        return createJwt(userNo, secretKey, "access", accessTokenValidTime);
    }

    // refresh 토큰 생성
    public static String createRefreshToken(Long userNo, String secretKey) {
        return createJwt(userNo, secretKey, "refresh", refreshTokenValidTime);
    }

    public static String createJwt(Long userNo, String secretKey, String type, Long tokenValidTime) {

        Claims claims = Jwts.claims();
        claims.put("userNo", userNo);

        return Jwts.builder()
                .setHeaderParam("type", type)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

    }


}
