package com.mtvs.arzip.domain.dto.user;

import com.mtvs.arzip.domain.entity.User;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long no;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String role;


    public static UserDto of (User user) {
        return UserDto.builder()
                .no(user.getNo())
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .nickname(user.getNickname())
                .role(user.getRole().name())
                .build();
    }

}
