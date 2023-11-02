package com.mtvs.arzip.domain.dto.user;

import com.mtvs.arzip.domain.entity.User;
import com.mtvs.arzip.domain.enum_class.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinRequest {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String id;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    private Integer age;

    private String gender;

    private Integer height;

    private Integer weight;

    private String nickname;

    public User toEntity(String encodedPassword) {
        return User.builder()
                .id(this.id)
                .password(encodedPassword)
                .name(this.name)
                .age(this.age)
                .gender(this.gender)
                .height(this.height)
                .weight(this.weight)
                .nickname(this.nickname)
                .role(UserRole.ROLE_USER)
                .build();
    }

}
