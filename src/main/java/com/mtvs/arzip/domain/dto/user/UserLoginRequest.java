package com.mtvs.arzip.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginRequest {

    private String id;
    private String password;
}
