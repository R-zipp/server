package com.mtvs.arzip.domain.dto.token;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {

    private String accessToken;
    private String refreshToken;
}
