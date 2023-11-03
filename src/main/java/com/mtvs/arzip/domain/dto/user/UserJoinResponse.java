package com.mtvs.arzip.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinResponse {

    private String email;
    private String name;
    private Integer height;
    private Integer weight;
    private String nickname;
    private String role;

    public static UserJoinResponse of(UserDto userDto) {
        return UserJoinResponse.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .height(userDto.getHeight())
                .weight(userDto.getWeight())
                .nickname(userDto.getNickname())
                .role(userDto.getRole())
                .build();
    }


}
