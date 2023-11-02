package com.mtvs.arzip.domain.dto.user;

import com.mtvs.arzip.domain.entity.User;
import com.mtvs.arzip.domain.enum_class.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
public class UserLoginRequest {

    private String id;
    private String password;

    public User user(PasswordEncoder passwordEncoder) {
        return User.builder()
                .id(id)
                .password(passwordEncoder.encode(password))
                .role(UserRole.ROLE_USER)
                .build();
    }

    public UsernamePasswordAuthenticationToken authenticationToken() {
        return new UsernamePasswordAuthenticationToken(id, password);
    }
}
