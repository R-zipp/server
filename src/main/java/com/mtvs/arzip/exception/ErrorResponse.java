package com.mtvs.arzip.exception;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String errorCode;
    private String message;

}
