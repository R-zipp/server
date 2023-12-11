package com.mtvs.arzip.domain.dto.user;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinResponse {

    private String email;
    private String name;
    private String nickname;
    private String role;

    public static UserJoinResponse of(UserDto userDto) {
        return UserJoinResponse.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .nickname(userDto.getNickname())
                .role(userDto.getRole())
                .build();
    }


}
