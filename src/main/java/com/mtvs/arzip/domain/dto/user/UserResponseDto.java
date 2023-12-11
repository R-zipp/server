package com.mtvs.arzip.domain.dto.user;

import com.mtvs.arzip.domain.entity.User;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private String email;

    public static UserResponseDto of(User user) {
        return new UserResponseDto(user.getEmail());
    }
}
