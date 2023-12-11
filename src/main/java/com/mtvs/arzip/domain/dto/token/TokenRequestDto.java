package com.mtvs.arzip.domain.dto.token;

import lombok.*;

@Getter
@NoArgsConstructor
public class TokenRequestDto {
    private String accessToken;
    private String refreshToken;
}
