package com.mtvs.arzip.domain.dto.user;

import com.mtvs.arzip.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long no;
    private String email;
    private String password;
    private String name;
    private Integer height;
    private Integer weight;
    private String nickname;
    private String role;


    public static UserDto of (User user) {
        return UserDto.builder()
                .no(user.getNo())
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .height(user.getHeight())
                .weight(user.getWeight())
                .nickname(user.getNickname())
                .role(user.getRole().name())
                .build();
    }

}
