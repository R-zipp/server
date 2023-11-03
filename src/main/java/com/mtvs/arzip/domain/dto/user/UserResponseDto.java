package com.mtvs.arzip.domain.dto.user;

import com.mtvs.arzip.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private String email;

    public static UserResponseDto of(User user) {
        return new UserResponseDto(user.getEmail());
    }
}
